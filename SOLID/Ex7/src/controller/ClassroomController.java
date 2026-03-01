package controller;

import deviceRegistry.DeviceRegistry;
import smartclassroomdevices.*;


public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) {
        this.reg = reg;
    }

    public void startClass() {

        for (Powerable powerable : reg.getAllOfType(Powerable.class)) {
            powerable.powerOn();
        }

        InputConnectable connectable = reg.getFirstOfType(InputConnectable.class);
        connectable.connectInput("HDMI-1");

        Illuminable lights = reg.getFirstOfType(LightsPanel.class);
        lights.setBrightness(60);

        TemperatureControllable ac = reg.getFirstOfType(AirConditioner.class);
        ac.setTemperature(24);

        AttendanceScannable scan = reg.getFirstOfType(AttendanceScanner.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        for (Powerable powerable : reg.getAllOfType(Powerable.class)) {
            powerable.powerOff();
        }
    }
}