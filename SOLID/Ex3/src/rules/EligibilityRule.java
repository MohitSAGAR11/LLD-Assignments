package rules;

import entities.StudentProfile;

public interface EligibilityRule {
    String check(StudentProfile s);
}
