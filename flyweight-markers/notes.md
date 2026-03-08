# Flyweight Pattern — Deduplicate Map Marker Styles

## Table of Contents
- [The Problem](#the-problem)
- [Our Approach](#our-approach-the-flyweight-pattern)
- [Architecture Overview](#architecture-overview)
- [Step-by-Step Implementation](#step-by-step-implementation)
- [The Cache in Action](#the-cache-in-action)
- [Key Takeaways](#key-takeaways)

---

## The Problem

GeoDash renders **30,000 map pins** on a screen. The original code stores a full style object — shape, color, size, filled — inside **every single marker**.

### The Memory Blow-Up

Imagine 10,000 markers that all look identical — red, circle, size 12, filled:

```
Marker #1     → style { RED, CIRCLE, 12, filled }   ← own copy
Marker #2     → style { RED, CIRCLE, 12, filled }   ← own copy
Marker #3     → style { RED, CIRCLE, 12, filled }   ← own copy
...
Marker #10000 → style { RED, CIRCLE, 12, filled }   ← own copy
```

The computer creates **10,000 identical objects** in memory storing the exact same data. This is pure waste — the application burns far more RAM than it actually needs, and it only gets worse as markers scale up.

### Root Cause

Every marker creation did this:

```java
new MapMarker(lat, lng, label, new MarkerStyle("CIRCLE", "RED", 12, true));
//                               ^^^^^^^^^^^ brand new object every single time
```

No sharing. No reuse. Just thousands of duplicates.

---

## Our Approach: The Flyweight Pattern

The core idea is simple:

> **Stop duplicating data that's identical across objects — create it once and share it.**

We split every `MapMarker` into two parts:

| Part | Data | Shared? |
|------|------|---------|
| **Intrinsic State** | shape, color, size, filled | ✅ Yes — shared across many markers |
| **Extrinsic State** | lat, lng, label | ❌ No — unique to each marker |

The style (intrinsic state) is extracted into an immutable `MarkerStyle` object that lives in a cache and is **reused** by every marker that needs it.

---

## Architecture Overview

```
MapDataSource (creates markers)
       ↓
       asks MarkerStyleFactory for a style
       ↓
MarkerStyleFactory
  ├── checks cache: "CIRCLE|RED|12|F"
  ├── if found  → returns existing MarkerStyle ✅
  └── if not    → creates once, saves, returns it

MapMarker
  ├── lat, lng, label   (extrinsic — unique per marker)
  └── → MarkerStyle     (intrinsic — shared reference)
```

Instead of 10,000 objects, only **one** `MarkerStyle` is created for red circle markers — and all 10,000 markers point to the same one.

---

## Step-by-Step Implementation

### Step 1 — Immutable MarkerStyle (Intrinsic State)

`MarkerStyle` is made **immutable** — all fields are `final` with no setters. This is critical because the object is shared. If one marker could change the style, it would accidentally change the style for thousands of others.

```java
public final class MarkerStyle {
    private final String shape;
    private final String color;
    private final int size;
    private final boolean filled;

    public MarkerStyle(String shape, String color, int size, boolean filled) {
        this.shape  = Objects.requireNonNull(shape);
        this.color  = Objects.requireNonNull(color);
        this.size   = size;
        this.filled = filled;
    }

    // getters only — no setters
}
```

Once created, a `MarkerStyle` can never be modified. It is permanently safe to share.

---

### Step 2 — MarkerStyleFactory (The Cache)

The factory holds a `Map<String, MarkerStyle>` cache. Every style is stored under a unique key built from its properties — for example `"PIN|RED|12|F"`.

```
When a marker needs a style:
  1. Factory builds the key → "CIRCLE|RED|12|F"
  2. Checks the cache
     → Found?     Return the existing instance
     → Not found? Create it, store it, return it
```

This guarantees that **identical style configurations always return the exact same object instance** — not a copy, the actual same reference in memory.

---

### Step 3 — Updated MapMarker (Extrinsic State Only)

`MapMarker` no longer owns its style data. It only stores what is **unique to it** — its position and label. The style is injected via the constructor as a shared reference:

```
MapMarker:
  - lat     ← unique
  - lng     ← unique
  - label   ← unique
  - style   ← shared reference → points to cached MarkerStyle
```

The marker doesn't create a style. It just receives one from the factory and holds a lightweight pointer to it.

---

### Step 4 — MapDataSource Uses the Factory

The marker creation pipeline no longer calls `new MarkerStyle(...)` directly. Instead it asks the factory:

```
Before:  new MapMarker(lat, lng, label, new MarkerStyle("CIRCLE", "RED", 12, true))
After:   new MapMarker(lat, lng, label, factory.getStyle("CIRCLE", "RED", 12, true))
```

The factory handles all the caching logic. `MapDataSource` doesn't need to think about it.

---

## The Cache in Action

Say we render 30,000 markers but only 96 unique style combinations exist:

```
30,000 MapMarker objects created
       ↓
Only 96 MarkerStyle objects exist in memory
       ↓
29,904 markers simply reuse an existing style reference
```

A `QuickCheck` on the factory confirms this — instead of reporting 30,000 style instances, it reports just **96 unique styles**.

---

## Key Takeaways

| Concept | Before (Bad) | After (Good) |
|--------|-------------|-------------|
| Style objects in memory | 30,000 (one per marker) | 96 (one per unique style) |
| Style data duplication | Massive | None |
| `MarkerStyle` mutability | Mutable | Immutable (`final` fields) |
| Style creation | `new MarkerStyle(...)` everywhere | Only inside factory |
| Marker stores | Full style data | Reference to shared style |
| Memory usage | Scales with marker count | Scales with unique style count |

> **The Flyweight Pattern** reduces memory usage by sharing as much data as possible with similar objects. It splits object state into intrinsic (shared, immutable) and extrinsic (unique per instance) — storing only what's truly unique inside each object.