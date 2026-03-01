package utils;

public class DriverAllocator implements InDriverAllocator{
    public String allocate(String studentId) {
        // fake deterministic driver
        return "DRV-17";
    }
}
