package rules;

import entities.StudentProfile;
import enums.LegacyFlags;

public class DisciplinaryRule implements EligibilityRule {
    public String check(StudentProfile s) {
        return s.disciplinaryFlag != LegacyFlags.NONE
                ? "disciplinary flag present"
                : null;
    }
}