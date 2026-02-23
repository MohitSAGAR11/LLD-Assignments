package printer;

import dto.EligibilityResult;
import entities.StudentProfile;
import enums.LegacyFlags;

public class ReportPrinter {
    public void print(StudentProfile s, EligibilityResult r) {
        System.out.printf("Student: %s (CGR=%.2f, attendance=%d, credits=%d, flag=%s)%n",
                s.name, s.cgr, s.attendancePct, s.earnedCredits, s.disciplinaryFlag);
        System.out.println("RESULT: " + r.status);
        for (String reason : r.reasons) {
            System.out.println("- " + reason);
        }
    }
}