# Proxy Pattern — Secure & Lazy-Load Reports

## Table of Contents
- [The Problem](#the-problem)
- [Our Approach](#our-approach-the-proxy-pattern)
- [Architecture Overview](#architecture-overview)
- [Step-by-Step Implementation](#step-by-step-implementation)
- [The Proxy in Action](#the-proxy-in-action)
- [Key Takeaways](#key-takeaways)

---

## The Problem

CampusVault is a CLI tool that opens internal reports for different users. The original design had `ReportViewer` and `App` talking **directly** to `ReportFile` — no middleman, no checks, no memory.

### 1. No Security

Any user — student, faculty, or admin — could open any report. There were zero permission checks. A student could freely read confidential internal reports meant only for admins or faculty.

```
Student  →  ReportFile.display()  →  reads confidential report ✅ (should be ❌)
Admin    →  ReportFile.display()  →  reads confidential report ✅
```

Everyone gets in. No one is stopped.

### 2. Eager Loading (Wasteful)

Every time `display()` was called, the system did a full expensive disk read — regardless of who was asking or whether they even had permission:

```
User calls display()
  → immediately loads entire file from disk  ← heavy, slow
  → then checks if user can see it           ← too late, already paid the cost
```

The expensive work happened **before** knowing if it was even necessary.

### 3. No Caching

The system had no memory. If the same authorized user viewed the same report twice in a row, the full disk-loading process ran all over again from scratch — completely redundant work.

```
Admin views Report A  →  full disk load  (1st time)
Admin views Report A  →  full disk load  (2nd time) ← forgot it just did this
Admin views Report A  →  full disk load  (3rd time) ← forgot again
```

### 4. Violates Dependency Inversion Principle (DIP)

`ReportViewer` and `App` (high-level modules) were directly depending on `ReportFile` (a low-level concrete class). DIP says high-level modules should depend on **abstractions**, not concrete implementations.

```
App.java          →  depends on →  ReportFile   ❌ (concrete class)
ReportViewer.java →  depends on →  ReportFile   ❌ (concrete class)
```

Any change to `ReportFile` would ripple up and force changes in `App` and `ReportViewer`.

---

## Our Approach: The Proxy Pattern

We introduce a `ReportProxy` — a middleman that sits between the client and the real report. Clients never touch `RealReport` directly. Everything goes through the proxy.

The proxy handles three responsibilities before ever touching the real file:

1. **Check permissions** — is this user allowed?
2. **Lazy load** — only load the file if access is granted
3. **Cache the result** — never load the same report twice

---

## Architecture Overview

```
App / ReportViewer
       ↓
   Report (interface)          ← high-level modules depend on this
       ↓
  ReportProxy
    ├── AccessControl.check(user, report)
    │       ↓ unauthorized → stop immediately, no disk load
    │       ↓ authorized
    ├── RealReport reference null?
    │       ↓ yes (first time) → load from disk, store reference
    │       ↓ no  (repeat)     → reuse cached reference
    └── RealReport.display()
```

The real disk-loading logic stays inside `RealReport`. The proxy just decides **when** (and **whether**) to use it.

---

## Step-by-Step Implementation

### Step 1 — Report Interface (The Abstraction)

We introduce a `Report` interface so that high-level modules depend on an abstraction, not a concrete class. This fixes the DIP violation.

```java
public interface Report {
    void display(User user);
}
```

Both `RealReport` and `ReportProxy` implement this interface. `App` and `ReportViewer` now only ever reference `Report` — they have no idea whether they're talking to a proxy or a real file.

---

### Step 2 — RealReport (The Heavy Lifter)

`RealReport` contains the expensive disk-reading logic. Its only job is to load and display the file content — nothing else.

```
RealReport:
  - filename
  - content (loaded from disk)

  constructor → loads file from disk immediately (heavy operation)
  display()   → prints content to console
```

Because loading is expensive, we want `RealReport` to be constructed **as rarely as possible** — only when truly needed.

---

### Step 3 — ReportProxy (The Middleman)

The proxy holds:
- The report metadata (filename, allowed roles)
- An `AccessControl` reference for permission checks
- A **nullable** `RealReport` reference — starts as `null`, filled only on first authorized access

```
ReportProxy:
  - filename
  - allowedRoles
  - accessControl
  - realReport = null         ← lazy, starts empty

  display(user):
    1. accessControl.check(user, allowedRoles)
         → denied?  log "Access Denied", return immediately — no disk load
         → granted? continue
    2. realReport == null?
         → yes: create RealReport (triggers disk load), store it
         → no:  reuse the already-loaded reference (cache hit)
    3. realReport.display()
```

The `RealReport` is **never created during proxy construction** — only on the first valid `display()` call. Every subsequent call reuses the same instance.

---

### Step 4 — AccessControl (The Gatekeeper)

A dedicated `AccessControl` class handles all permission logic. It checks whether a user's role is in the report's list of allowed roles.

```
AccessControl.canAccess(user, allowedRoles):
  → true  if user's role is in allowedRoles
  → false otherwise
```

Keeping this in a separate class means permission rules can change without touching `ReportProxy` or `RealReport`.

---

### Step 5 — App and ReportViewer Use the Interface

Before:
```
App.java          →  new ReportFile(...)     ❌ concrete dependency
ReportViewer.java →  ReportFile.display()    ❌ concrete dependency
```

After:
```
App.java          →  new ReportProxy(...)    ✅ still concrete at wiring point
ReportViewer.java →  Report.display()        ✅ depends only on interface
```

`ReportViewer` now accepts a `Report` interface — it works with any implementation. The proxy is wired up once in `App` and injected in.

---

## The Proxy in Action

Here's what happens for different users viewing the same report:

```
Student  calls display()
  → AccessControl: Student not in [ADMIN, FACULTY]
  → "Access Denied" — stops here, no disk load

Faculty  calls display()  (1st time)
  → AccessControl: Faculty is allowed ✅
  → realReport is null → load from disk, store reference
  → display content

Faculty  calls display()  (2nd time)
  → AccessControl: Faculty is allowed ✅
  → realReport already loaded → reuse cached reference
  → display content  (no disk load this time)

Faculty  calls display()  (3rd time)
  → same as 2nd time — instant, from cache
```

Console logs make it clear whether a real load happened or a cached version was served.

---

## Key Takeaways

| Concept | Before (Bad) | After (Good) |
|--------|-------------|-------------|
| Access control | None — everyone gets in | `AccessControl` checks before any load |
| File loading | Eager — on every `display()` call | Lazy — only on first authorized access |
| Caching | None — reloads every time | `RealReport` stored after first load |
| DIP compliance | ❌ Violated — depends on `ReportFile` | ✅ Satisfied — depends on `Report` interface |
| Unauthorized cost | Pays full disk-load cost anyway | Zero cost — stopped at permission check |
| Adding new report types | Requires changing `App` and `ReportViewer` | Just implement `Report` interface |

> **The Proxy Pattern** provides a surrogate or placeholder for another object to control access to it. It lets you add security, lazy initialization, caching, or logging without changing the real subject or the client code.