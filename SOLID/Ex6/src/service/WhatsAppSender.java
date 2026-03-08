package service;

import entities.Notification;
import service.NotificationSender;
import store.AuditLog;

public class WhatsAppSender extends NotificationSender {
    public WhatsAppSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        if (!isE164(n.phone)) {
            System.out.println("WA ERROR: phone must start with + and country code");
            audit.add("WA failed");
            return;
        }
        System.out.println("WA -> to=" + n.phone + " body=" + n.body);
        audit.add("wa sent");
    }

    private boolean isE164(String phone) {
        return phone != null && phone.startsWith("+") && phone.length() > 1;
    }
}