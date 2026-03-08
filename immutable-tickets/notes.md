# Immutable Object + Builder Pattern — IncidentTicket

## Table of Contents
- [The Problem](#the-problem)
- [Our Approach](#our-approach-immutability--builder-pattern)
- [Architecture Overview](#architecture-overview)
- [Four Problems We Solved](#four-problems-we-solved)
- [The Pattern in Action](#the-pattern-in-action)
- [Key Takeaways](#key-takeaways)

---

## The Problem

In a real support system, a ticket is a **permanent record** — like a legal document. Once created, its history must be preserved for audits and accountability. The old code treated tickets like sticky notes anyone could rewrite at any time.

### Problem 1 — Setters Allowed Silent Mutation

The ticket had public setters, meaning any part of the codebase could change its fields at any time:

```
Ticket created:  priority = HIGH,  assignee = "Alice"
... some code runs ...
Ticket now:      priority = LOW,   assignee = "Bob"    ← who changed this? when? why?
```

In a support system this is dangerous. Audits rely on the original record being intact. With setters, the original data is simply gone — overwritten with no trace.

### Problem 2 — The Tags List Was a Leak

The `tags` field returned a **direct reference** to the internal list. Anyone holding that reference could modify the ticket's tags from the outside without the `IncidentTicket` class knowing anything about it:

```
List<String> tags = ticket.getTags();
tags.add("CRITICAL");     // mutates the ticket's internal list directly
tags.remove("billing");   // ticket has no idea this happened
```

The class lost control of its own data the moment it handed out a reference.

### Problem 3 — Validation Was Scattered

Rules like "email must be valid" or "ID must not exceed a length limit" were spread across different files. This made it trivially easy to accidentally create a broken ticket:

```
IncidentTicket ticket = new IncidentTicket();
ticket.setEmail("not-an-email");   // no one stopped this
ticket.setId("this-id-is-way-too-long-and-invalid");   // no one stopped this either
```

There was no single enforced checkpoint where all rules were verified before a ticket came into existence.

### Problem 4 — Shared References Caused Surprise Changes

If two different parts of the program held a reference to the same ticket object, one part could change the priority and the other would suddenly see a different value with no warning:

```
Part A holds → ticket (priority: HIGH)
Part B holds → same ticket object
Part B changes priority to LOW
Part A now sees → ticket (priority: LOW)  ← surprised, never asked for this
```

Shared mutable state is one of the most common sources of hard-to-track bugs.

---

## Our Approach: Immutability + Builder Pattern

We solve all four problems with two complementary ideas working together:

- **Immutability** — once a ticket is created, it can never change. Writing in ink, not pencil.
- **Builder Pattern** — all data is collected and validated in one place before the ticket is created.

---

## Architecture Overview

```
Client Code
    ↓
IncidentTicket.Builder          ← collects all fields freely
    ↓
  .build()                      ← validates everything at once
    ↓
IncidentTicket (immutable)      ← all fields final, no setters, defensive copy of tags
    ↓
  .toBuilder()                  ← if update needed, pour data back into a Builder
    ↓
IncidentTicket (new version)    ← fresh ticket, original unchanged
```

The original ticket is **never touched**. Updates produce new tickets.

---

## Four Problems We Solved

### Fix 1 — All Fields Final, No Setters (Immutability)

Every field in `IncidentTicket` is declared `final`. Once the constructor runs, the values are locked in permanently. There are no setters — they simply do not exist.

```
IncidentTicket:
  - final String id
  - final String title
  - final String assignee
  - final Priority priority
  - final String reporterEmail
  - final List<String> tags      ← defensive copy, read-only
```

This is the difference between writing in pencil and writing in ink. The ticket created at 9:00 AM will look exactly the same at 9:00 PM. No silent overwrites. No lost audit trail.

---

### Fix 2 — Defensive Copy + Unmodifiable List (Tag Protection)

The tags list is protected at two levels inside the constructor:

```
Step 1: Create a private copy of the incoming list
        → the original list the caller passed in can now change freely, our copy is safe

Step 2: Wrap the copy in Collections.unmodifiableList()
        → anyone who calls getTags() gets a read-only shell

Step 3: If someone tries to add or remove a tag on the returned list
        → program throws UnsupportedOperationException immediately
```

The `IncidentTicket` class is now the sole owner of its tag data. No outside code can touch it.

---

### Fix 3 — Builder Centralises All Validation

The `Builder` acts as a staging area. It accepts all fields freely with no restrictions — but when `.build()` is called, it runs every validation rule in one place before the ticket is allowed to exist:

```
Builder.build():
  → Is title null or empty?          → throw IllegalArgumentException
  → Is email a valid format?         → throw IllegalArgumentException
  → Is ID within length limit?       → throw IllegalArgumentException
  → Is priority set?                 → throw IllegalArgumentException
  → All checks pass? → create and return the immutable IncidentTicket
```

A broken ticket can never be created. Validation is no longer scattered — it lives in exactly one place and is always enforced.

---

### Fix 4 — toBuilder() for Safe Updates

When an update is needed (like reassigning a ticket), `toBuilder()` pours all the existing ticket's data into a fresh Builder. You change only the one field you care about and call `.build()` to get a new ticket:

```
Original ticket:  id=T001, priority=HIGH, assignee="Alice"

ticket.toBuilder()
  .assignee("Bob")
  .build()

New ticket:       id=T001, priority=HIGH, assignee="Bob"
Original ticket:  id=T001, priority=HIGH, assignee="Alice"  ← completely untouched
```

Both parts of the program that held the original ticket still see the original, unchanged data. There are no surprise mutations. If you want the new version, you explicitly use it.

---

## The Pattern in Action

Here is what happens now for every scenario that caused problems before:

```
Someone tries to change a ticket's priority after creation
  → No setter exists → compile error ✅

Someone tries to add a tag via getTags()
  → Returns unmodifiable list → throws UnsupportedOperationException ✅

Someone creates a ticket with an invalid email
  → Builder.build() catches it → throws IllegalArgumentException ✅

Part A and Part B both hold the same ticket, Part B wants to reassign it
  → Part B calls toBuilder().assignee("Bob").build()
  → Part B gets a new ticket
  → Part A's ticket is completely unchanged ✅

Audit team checks the original ticket
  → Fields are final, no setters ever existed
  → Original record is exactly as it was when created ✅
```

---

## Key Takeaways

| Problem | Root Cause | Fix |
|--------|-----------|-----|
| Silent field mutation | Public setters | All fields `final`, no setters |
| Tags modified from outside | Direct list reference returned | Defensive copy + `unmodifiableList` |
| Broken tickets created | Validation scattered across files | All validation in `Builder.build()` |
| Shared state surprise changes | Same mutable object shared | `toBuilder()` creates a new ticket |
| Lost audit trail | Overwriting original data | Immutability preserves all records |

> **Immutability** means an object's state can never change after construction — making it safe to share, safe to cache, and safe to audit. The **Builder Pattern** is the natural companion: it provides a flexible way to construct complex immutable objects while enforcing all validation rules in one place before the object is allowed to exist.