package tools;

import utils.EventPlanner;

public class EventLeadTool implements EventCreationTool, EventCountTool {
    private final EventPlanner planner;

    public EventLeadTool(EventPlanner planner) {
        this.planner = planner;
    }

    @Override
    public void createEvent(String name, double budget) {
        planner.create(name, budget);
    }

    public int getEventCount() {
        return planner.count();
    }

}
