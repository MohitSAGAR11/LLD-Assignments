package service;

import entities.Notification;
import store.AuditLog;

public abstract class NotificationSender {
    protected final AuditLog audit;

    protected NotificationSender(AuditLog audit) {
        if (audit == null) throw new IllegalArgumentException("audit must not be null");
        this.audit = audit;
    }

    public final void send(Notification n) {
        if (n == null) throw new IllegalArgumentException("Notification must not be null");
        doSend(n);
    }

    protected abstract void doSend(Notification n);
}