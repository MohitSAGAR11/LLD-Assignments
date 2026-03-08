# Adapter Pattern — Payments (Refactoring)

## Table of Contents
- [The Problem](#the-problem)
- [Our Approach](#our-approach-the-adapter-pattern)
- [Architecture Overview](#architecture-overview)
- [Step-by-Step Implementation](#step-by-step-implementation)
- [Adding a New Provider](#adding-a-new-provider)
- [Key Takeaways](#key-takeaways)

---

## The Problem

`OrderService` was doing **way too much work** — it knew the internal details of every payment SDK and had to manually handle each one differently.

### 1. Mismatched Interfaces

Each SDK has completely different method names and logic:

```java
// FastPay → single step
fastPayClient.payNow(customerId, amountCents);

// SafeCash → two steps
SafeCashPayment p = safeCashClient.createPayment(customerId, amountCents);
safeCashClient.confirm(p);
```

`OrderService` had to know all of this internally — messy and fragile.

### 2. Provider Branching

The service used `if/else` blocks just to decide *who* to call:

```java
if (provider.equals("fastpay")) {
    fastPayClient.payNow(...);
} else if (provider.equals("safecash")) {
    SafeCashPayment p = safeCashClient.createPayment(...);
    safeCashClient.confirm(p);
}
```

This logic doesn't belong in `OrderService` at all.

### 3. Violates Open-Closed Principle (OCP)

Adding a new provider like PayPal meant **opening `OrderService` and editing it** — exactly what OCP says you should never have to do.

> **Open-Closed Principle:** A class should be *open for extension* but *closed for modification*.

---

## Our Approach: The Adapter Pattern

We introduce a **middle layer** — Adapters — that wrap each messy SDK and make them all look identical to `OrderService`.

Think of it like a **power plug adapter** 🔌

> You have one standard socket (your app), but plugs from different countries (FastPay, SafeCash). The adapter lets them all fit **without changing the socket**.

---

## Architecture Overview

```
OrderService  →  PaymentGateway  (interface: charge)
                       ↑                   ↑
              FastPayAdapter        SafeCashAdapter
                       ↑                   ↑
              FastPayClient         SafeCashClient
               .payNow()        .createPayment() + .confirm()
```

`OrderService` only ever sees `PaymentGateway`. The SDKs are completely hidden behind adapters.

---

## Step-by-Step Implementation

### Step 1 — The Common Interface (The Contract)

```java
public interface PaymentGateway {
    String charge(String customerId, int amountCents);
}
```

This is the **only thing `OrderService` needs to understand**. One method, one contract, used by everyone.

---

### Step 2 — The Adapters (The Translators)

Each adapter implements `PaymentGateway` and handles SDK-specific logic internally.

**FastPayAdapter** — wraps FastPay's single-step SDK:

```java
public class FastPayAdapter implements PaymentGateway {
    private final FastPayClient client;

    public FastPayAdapter(FastPayClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public String charge(String customerId, int amountCents) {
        return client.payNow(customerId, amountCents); // translates charge → payNow
    }
}
```

**SafeCashAdapter** — wraps SafeCash's two-step SDK:

```java
public class SafeCashAdapter implements PaymentGateway {
    private final SafeCashClient client;

    public SafeCashAdapter(SafeCashClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public String charge(String customerId, int amountCents) {
        // Handles complex two-step logic — OrderService never sees this
        SafeCashPayment payment = client.createPayment(customerId, amountCents);
        return client.confirm(payment);
    }
}
```

Both adapters expose the same `charge()` method to the outside world. The messy SDK differences are hidden inside.

---

### Step 3 — Clean OrderService (No SDK Knowledge)

```java
public class OrderService {
    private final PaymentGateway gateway;

    public OrderService(PaymentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    public void processOrder(String customerId, int amountCents) {
        String txId = gateway.charge(customerId, amountCents); // clean, simple, done
        System.out.println("Transaction ID: " + txId);
    }
}
```

- No `if/else` blocks
- No SDK imports
- No provider branching
- Just one clean `.charge()` call

---

### Step 4 — Registry in App.java (The Selector)

```java
public class App {
    public static void main(String[] args) {
        // Build the registry — map provider names to their adapters
        Map<String, PaymentGateway> registry = new HashMap<>();
        registry.put("fastpay",  new FastPayAdapter(new FastPayClient()));
        registry.put("safecash", new SafeCashAdapter(new SafeCashClient()));

        // FastPay order
        OrderService order1 = new OrderService(registry.get("fastpay"));
        order1.processOrder("customer-01", 5000);

        // SafeCash order
        OrderService order2 = new OrderService(registry.get("safecash"));
        order2.processOrder("customer-02", 3000);
    }
}
```

The `Map` acts as a **registry** — a lookup table that maps a provider name to its adapter. `OrderService` receives the correct adapter via **constructor injection** and never needs to know which one it is.

---

## Adding a New Provider

Want to add PayPal tomorrow? Here's all you need to do:

| Step | Action |
|------|--------|
| 1 | Create `PayPalAdapter implements PaymentGateway` |
| 2 | Add `registry.put("paypal", new PayPalAdapter(...))` in `App.java` |
| 3 | ✅ Done — `OrderService` is completely untouched |

This is the **Open-Closed Principle in action** — you *extend* the system by adding new adapters without ever *modifying* the core service.

---

## Key Takeaways

| Concept | Before (Bad) | After (Good) |
|--------|-------------|-------------|
| `OrderService` depends on | Concrete SDKs | `PaymentGateway` interface |
| Adding a provider | Modify `OrderService` | Add a new Adapter only |
| SDK complexity | Leaks into service | Hidden inside adapters |
| OCP compliance | ❌ Violated | ✅ Satisfied |
| Testability | Hard (real SDKs) | Easy (mock the interface) |

> **The Adapter Pattern** converts the interface of a class into another interface that clients expect. It lets classes work together that couldn't otherwise because of incompatible interfaces.