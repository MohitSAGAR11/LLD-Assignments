package services;

import dto.StudentData;
import entities.StudentRecord;
import parsers.Parser;
import printer.StudentOnboardingPrinter;
import repository.StudentRepository;
import util.IdUtil;
import validator.Validator;

import java.util.List;

public class StudentOnboardingService implements OnboardingService<StudentRecord> {
    private final Parser<StudentData> parser;
    private final Validator<StudentData> validator;
    private final StudentRepository repository;
    private final StudentOnboardingPrinter printer;

    public StudentOnboardingService(Parser<StudentData> parser, Validator<StudentData> validator, StudentRepository repository) {
        this.parser = parser;
        this.validator = validator;
        this.repository = repository;
        this.printer = new StudentOnboardingPrinter();
    }

    @Override
    public void register(String raw) {
        printer.printInput(raw);

        StudentData studentData = parser.parse(raw);
        List<String> errors = validator.validate(studentData);

        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(repository.count());
        StudentRecord rec = new StudentRecord(id, studentData.name(), studentData.email(), studentData.phone(), studentData.program());

        repository.save(rec);

        printer.printSuccess(rec, repository.count());
    }
}