package validator;

import dto.StudentData;
import enums.Program;

import java.util.ArrayList;
import java.util.List;

public class StudentDataValidator implements Validator<StudentData> {
    @Override
    public List<String> validate(StudentData data) {
        List<String> errors = new ArrayList<>();

        if (data.name().isBlank()) errors.add("Name is required");
        if (data.email().isBlank() || !data.email().contains("@")) errors.add("Email is invalid");
        if (data.phone().isBlank() || !data.phone().chars().allMatch(Character::isDigit)) errors.add("phone is invalid");
        if (!Program.isValid(data.program())) errors.add("program is invalid");

        return errors;
    }
}
