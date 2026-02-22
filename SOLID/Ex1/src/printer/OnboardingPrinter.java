package printer;

import entities.StudentRecord;

import java.util.List;

public interface OnboardingPrinter {
    void printInput(String raw);
    void printSuccess(StudentRecord record , int totalCount);
    void printErrors(List<String> errors);
}
