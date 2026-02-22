package printer;

import entities.StudentRecord;

import java.util.List;

public class StudentOnboardingPrinter implements OnboardingPrinter{
    @Override
    public void printInput(String raw) {
        System.out.println("INPUT: " + raw);
    }

    @Override
    public void printSuccess(StudentRecord record, int totalCount) {
        System.out.println("OK: created student " + record.getId());
        System.out.println("Saved. Total students: " + totalCount);
        System.out.println("Confirmation:");
        System.out.println(record);
    }

    @Override
    public void printErrors(List<String> errors) {
        System.out.println("ERROR: cannot register");
        for (String e : errors) {
            System.out.println("- " + e);
        }
    }
}
