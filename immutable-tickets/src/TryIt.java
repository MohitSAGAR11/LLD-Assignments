import com.example.tickets.*;

public class TryIt {
    public static void main(String[] args) {
        TicketService service = new TicketService();

        // 1. Creation
        IncidentTicket t1 = service.createTicket("INC-101", "mohit@example.com", "System Offline");
        System.out.println("Original: " + t1);

        // 2. Escalation (Returns a NEW object)
        IncidentTicket t2 = service.escalateToCritical(t1);
        System.out.println("Escalated: " + t2);

        // Proof of immutability
        System.out.println("t1 is still: " + t1.getPriority()); // Still MEDIUM

        // 3. Immutability check on collections
        try {
            t1.getTags().add("HACK");
        } catch (UnsupportedOperationException e) {
            System.out.println("Success: Cannot modify tags list directly!");
        }

        // 4. Validation check
        try {
            new IncidentTicket.Builder().id("INVALID_ID_#").build();
        } catch (IllegalArgumentException e) {
            System.out.println("Success: Caught invalid ID: " + e.getMessage());
        }
    }
}