package util;

import rules.*;

import java.util.List;

public class RuleLoader {
    public static List<EligibilityRule> defaultRules() {
        return List.of(
                new DisciplinaryRule(),
                new CgrRule(8.0),
                new AttendanceRule(75),
                new CreditsRule(20)
        );
    }
}