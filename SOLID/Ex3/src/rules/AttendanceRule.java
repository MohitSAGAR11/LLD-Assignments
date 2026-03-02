package rules;

import entities.StudentProfile;

public class AttendanceRule implements EligibilityRule {
    private final int minAttendance;

    public AttendanceRule(int minAttendance) { this.minAttendance = minAttendance; }

    public String check(StudentProfile s) {
        return s.attendancePct < minAttendance
                ? "attendance below " + minAttendance
                : null;
    }
}