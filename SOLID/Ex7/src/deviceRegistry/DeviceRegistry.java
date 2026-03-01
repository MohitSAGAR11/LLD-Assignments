package deviceRegistry;

import smartclassroomdevices.SmartClassroomDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceRegistry {
    private final java.util.List<SmartClassroomDevice> devices = new ArrayList<>();

    public void add(SmartClassroomDevice d) {
        devices.add(d);
    }

    public <T> List<T> getAllOfType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (SmartClassroomDevice d : devices) {
            if (type.isInstance(d)) {
                result.add(type.cast(d));
            }
        }
        return result;
    }

    public <T> T getFirstOfType(Class<T> type) {
        for (SmartClassroomDevice d : devices) {
            if (type.isInstance(d)) {
                return type.cast(d);
            }
        }
        throw new IllegalArgumentException("Missing: " + type.getSimpleName());
    }
}