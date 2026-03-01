package tools;

import utils.MinutesBook;

public class SecretaryTool implements MinutesTool, EventCountTool {
    private final MinutesBook book;

    public SecretaryTool(MinutesBook book) {
        this.book = book;
    }

    @Override
    public void addMinutes(String text) {
        book.add(text);
    }

    @Override
    public int getEventCount() {
        return 0;
    }
}
