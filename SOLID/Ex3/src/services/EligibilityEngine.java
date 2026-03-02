package services;

import dto.EligibilityResult;
import entities.StudentProfile;
import printer.ReportPrinter;
import repository.EligibilityRepository;
import rules.EligibilityRule;

import java.util.ArrayList;
import java.util.List;

public class EligibilityEngine {
    private final List<EligibilityRule> rules;
    private final EligibilityRepository repository;
    private final ReportPrinter printer;

    public EligibilityEngine(List<EligibilityRule> rules,
                             EligibilityRepository repository,
                             ReportPrinter printer) {
        this.rules = rules;
        this.repository = repository;
        this.printer = printer;
    }

    public EligibilityResult evaluate(StudentProfile s) {
        List<String> reasons = new ArrayList<>();
        for (EligibilityRule rule : rules) {
            String reason = rule.check(s);
            if (reason != null) reasons.add(reason);
        }
        String status = reasons.isEmpty() ? "ELIGIBLE" : "NOT_ELIGIBLE";
        return new EligibilityResult(status, reasons);
    }

    public void runAndPrint(StudentProfile s) {
        EligibilityResult result = evaluate(s);
        printer.print(s, result);
        repository.save(s.rollNo, result.status);
    }
}