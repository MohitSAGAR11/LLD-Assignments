import service.ClubConsole;
import utils.BudgetLedger;
import utils.EventPlanner;
import utils.MinutesBook;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Club Admin ===");
        BudgetLedger ledger = new BudgetLedger();
        MinutesBook minutes = new MinutesBook();
        EventPlanner events = new EventPlanner();

        ClubConsole console = new ClubConsole(ledger, minutes, events);
        console.run();
    }
}
