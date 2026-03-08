# Singleton Pattern — Thread-Safe MetricsRegistry

## Table of Contents
- [The Problem](#the-problem)
- [Our Approach](#our-approach-the-singleton-pattern)
- [Architecture Overview](#architecture-overview)
- [Three Doors We Closed](#three-doors-we-closed)
- [The Fix in Action](#the-fix-in-action)
- [Key Takeaways](#key-takeaways)

---

## The Problem

The starter code *intended* to be a Singleton — one single `MetricsRegistry` for the entire application. But it left **three major doors open** through which extra instances could sneak in and silently break the design.

### Door 1 — Unprotected getInstance() (Thread Safety)

If two threads asked for the instance at the exact same moment, both would see `INSTANCE` as `null` and both would independently create their own separate objects:

```
Thread A: checks INSTANCE → null → starts creating...
Thread B: checks INSTANCE → null → starts creating...   ← race condition

Result: Two separate MetricsRegistry objects exist
```

This is a classic **race condition**. The Singleton guarantee breaks the moment more than one thread is involved.

### Door 2 — Public Constructor (Direct Instantiation)

The constructor was `public`, meaning anyone anywhere in the codebase could simply write:

```java
MetricsRegistry r1 = new MetricsRegistry();
MetricsRegistry r2 = new MetricsRegistry();
MetricsRegistry r3 = new MetricsRegistry();
```

No rules, no restrictions. The Singleton pattern was purely decorative.

And even if the constructor were made `private`, Java's **Reflection API** could force it open:

```java
Constructor<MetricsRegistry> c = MetricsRegistry.class.getDeclaredConstructor();
c.setAccessible(true);  // bypasses private
MetricsRegistry illegal = c.newInstance();  // creates a second instance anyway
```

Reflection completely ignores access modifiers — `private` alone is not enough.

### Door 3 — Serialization Creates a New Copy

When Java saves an object to a file (serialization) and reads it back (deserialization), it **bypasses the constructor entirely** and creates a brand new instance from scratch — completely ignoring Singleton rules:

```
MetricsRegistry saved to file   →  one instance in memory
MetricsRegistry loaded from file →  a second, separate instance created
```

The deserialized object is a different object in memory, silently breaking the Singleton guarantee.

---

## Our Approach: The Singleton Pattern

We close all three doors with targeted fixes — each one addressing exactly one vulnerability.

```
Door 1 (Threads)        →  Double-Checked Locking + volatile
Door 2 (Constructor)    →  Private constructor + Reflection Guard
Door 3 (Serialization)  →  readResolve() hook
```

---

## Architecture Overview

```
MetricsLoader
    ↓
    calls MetricsRegistry.getInstance()
    ↓
MetricsRegistry
  ├── private static volatile INSTANCE = null
  ├── private constructor (+ reflection guard)
  ├── getInstance() with double-checked locking
  └── readResolve() for deserialization protection

Result: Always the same single instance, no matter how it's accessed
```

`MetricsLoader` never uses `new MetricsRegistry()` — it always goes through `getInstance()`, and `getInstance()` guarantees it returns the one and only instance.

---

## Three Doors We Closed

### Fix 1 — Double-Checked Locking + volatile (Thread Safety)

The `volatile` keyword ensures that when one thread writes the instance to memory, all other threads immediately see the updated value — no stale cached reads.

Double-checked locking adds two layers of protection:

```
getInstance():
  1. First check  → if INSTANCE exists, return it immediately (fast path, no lock)
  2. Lock the class  → only one thread can enter at a time
  3. Second check → check again inside the lock in case another thread
                    just finished creating the instance while we were waiting
  4. If still null → create the instance now
  5. Return instance
```

The second check inside the lock is critical — without it, two threads could both pass the first check, then both wait at the lock, and the second one would create a duplicate instance the moment the first one released the lock.

---

### Fix 2 — Private Constructor + Reflection Guard

Making the constructor `private` blocks direct instantiation with `new`. But since Reflection can bypass `private`, we add an **internal guard** inside the constructor itself:

```
constructor():
  → Is INSTANCE already not null?
      → YES: throw RuntimeException("Instance already exists!")
      → NO:  proceed normally (this is the very first call)
```

The constructor checks its own precondition. If someone uses Reflection to force a second construction, the code refuses and throws an exception before any new instance is created.

---

### Fix 3 — readResolve() Hook (Serialization Protection)

`readResolve()` is a special Java hook that intercepts the deserialization process. Instead of letting Java create a new object from the file, we hijack the process and return our existing in-memory instance:

```
Normal deserialization:
  load from file → create new object → return it   ❌ (breaks Singleton)

With readResolve():
  load from file → readResolve() intercepts → return existing INSTANCE ✅
```

The deserialized data is discarded. The application always ends up with the same one instance it started with.

---

## The Fix in Action

Here is what happens now for every possible attack on the Singleton:

```
Two threads call getInstance() simultaneously
  → Thread A locks, creates instance, releases lock
  → Thread B enters lock, second check finds instance already exists
  → Thread B returns the same instance ✅

Someone calls new MetricsRegistry()
  → Compile error: constructor is private ✅

Someone uses Reflection to force the constructor
  → Constructor checks: INSTANCE already exists
  → Throws RuntimeException ✅

MetricsRegistry is serialized and deserialized
  → readResolve() intercepts
  → Returns existing INSTANCE, discards deserialized copy ✅

MetricsLoader needs the registry
  → Calls MetricsRegistry.getInstance()
  → Always gets the one and only instance ✅
```

---

## Key Takeaways

| Vulnerability | Problem | Fix |
|--------------|---------|-----|
| Race condition | Two threads create two instances | `volatile` + double-checked locking |
| Public constructor | Anyone calls `new MetricsRegistry()` | `private` constructor |
| Reflection bypass | Reflection forces constructor open | Internal guard throws `RuntimeException` |
| Serialization | Deserialization creates a new copy | `readResolve()` returns existing instance |
| DIP in MetricsLoader | Used `new` keyword directly | Uses `MetricsRegistry.getInstance()` |

> **The Singleton Pattern** ensures a class has only one instance and provides a global access point to it. A truly robust Singleton must defend against threads, reflection, and serialization — not just hide its constructor.