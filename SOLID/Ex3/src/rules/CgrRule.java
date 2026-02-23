package rules;

import entities.StudentProfile;
import javafx.css.Rule;

public class CgrRule implements EligibilityRule {
    private final double minCgr;

    public CgrRule(double minCgr) {
        this.minCgr = minCgr;
    }


    @Override
    public String check(StudentProfile s) {
        return s.cgr < minCgr ? "CGR below " + minCgr : null;
    }
}
