package com.example.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 1. MUST be volatile for Double-Checked Locking to work correctly in Java
    private static volatile MetricsRegistry INSTANCE;

    private final Map<String, Long> counters = new HashMap<>();

    // 2. Private constructor
    private MetricsRegistry() {
        // 3. Reflection Guard
        if (INSTANCE != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance.");
        }
    }

    // 4. Double-Checked Locking (Lazy & Thread-Safe)
    public static MetricsRegistry getInstance() {
        if (INSTANCE == null) { // First check (no locking)
            synchronized (MetricsRegistry.class) {
                if (INSTANCE == null) { // Second check (with locking)
                    INSTANCE = new MetricsRegistry();
                }
            }
        }
        return INSTANCE;
    }

    // Instance methods for metrics
    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }

    // 5. Serialization Guard
    @Serial
    protected Object readResolve() {
        return getInstance();
    }
}