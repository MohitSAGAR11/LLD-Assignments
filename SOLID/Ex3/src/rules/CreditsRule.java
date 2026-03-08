package rules;

import entities.StudentProfile;

public class CreditsRule implements EligibilityRule {
    private final int minCredits;

    public CreditsRule(int minCredits) { this.minCredits = minCredits; }

    public String check(StudentProfile s) {
        return s.earnedCredits < minCredits
                ? "credits below " + minCredits
                : null;
    }
}