package repository;

import java.util.HashMap;
import java.util.Map;

public class FakeEligibilityStore implements EligibilityRepository {
    private final Map<String, String> store = new HashMap<>();

    public void save(String rollNo, String status) {
        store.put(rollNo, status);
        System.out.println("Saved evaluation for roll=" + rollNo);
    }
}