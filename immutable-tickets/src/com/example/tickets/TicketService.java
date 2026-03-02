package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        List<String> tags = new ArrayList<>();
        tags.add("NEW");

        // Fluent creation with all initial defaults
        return new IncidentTicket.Builder()
                .id(id)
                .reporterEmail(reporterEmail)
                .title(title)
                .priority("MEDIUM")
                .source("CLI")
                .tags(tags)
                .build();
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        // Create new tag list based on existing ones
        List<String> newTags = new ArrayList<>(t.getTags());
        if (!newTags.contains("ESCALATED")) {
            newTags.add("ESCALATED");
        }

        // Return a fresh copy with updated state
        return t.toBuilder()
                .priority("CRITICAL")
                .tags(newTags)
                .build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        // Simple evolution using toBuilder
        return t.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}