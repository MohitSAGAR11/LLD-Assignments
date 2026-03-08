import parsers.StudentParser;
import printer.TextTable;
import repository.impl.FakeDb;
import services.OnboardingService;
import services.StudentOnboardingService;
import validator.StudentDataValidator;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");

        FakeDb db = new FakeDb();
        StudentOnboardingService svc = new StudentOnboardingService(
                new StudentParser(),
                new StudentDataValidator(),
                db
        );

        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.register(raw);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(db.all()));
    }
}