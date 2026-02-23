import entities.StudentProfile;
import enums.LegacyFlags;
import printer.ReportPrinter;
import repository.FakeEligibilityStore;
import rules.EligibilityRule;
import services.EligibilityEngine;
import util.RuleLoader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");

        List<EligibilityRule> rules = RuleLoader.defaultRules();

        EligibilityEngine engine = new EligibilityEngine(
                rules,
                new FakeEligibilityStore(),
                new ReportPrinter()
        );

        StudentProfile ayaan = new StudentProfile(
                "23BCS1001", "Ayaan",
                9.10, 92, 28, LegacyFlags.NONE
        );

        engine.runAndPrint(ayaan);
    }
}