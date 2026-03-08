package smartclassroomdevices;

public class AttendanceScanner implements Powerable, AttendanceScannable, SmartClassroomDevice {
    @Override
    public void powerOn() { /* ok */ }

    @Override
    public void powerOff() { /* no output */ }

    @Override
    public int scanAttendance() {
        return 3;
    }


    @Override
    public String getClassSimpleName() {
        return AttendanceScanner.class.getSimpleName();
    }
}