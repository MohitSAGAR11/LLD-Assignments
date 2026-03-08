package service;

import tools.EventLeadTool;
import tools.SecretaryTool;
import tools.TreasurerTool;
import utils.BudgetLedger;
import utils.EventPlanner;
import utils.MinutesBook;

public class ClubConsole {
    private final BudgetLedger ledger;
    private final MinutesBook minutes;
    private final EventPlanner events;

    public ClubConsole(BudgetLedger ledger, MinutesBook minutes, EventPlanner events) {
        this.ledger = ledger; this.minutes = minutes; this.events = events;
    }

    public void run() {
        TreasurerTool treasurer = new TreasurerTool(ledger);
        SecretaryTool secretary = new SecretaryTool(minutes);
        EventLeadTool lead = new EventLeadTool(events);

        treasurer.addIncome(5000, "sponsor");
        secretary.addMinutes("Meeting at 5pm");
        lead.createEvent("HackNight", 2000);

        System.out.println("Summary: ledgerBalance=" + ledger.balanceInt() + ", minutes=" + minutes.count() + ", events=" + lead.getEventCount());
    }
}
