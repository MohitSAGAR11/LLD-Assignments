package printer;

import java.util.List;

public class ConsolePrinter {
    public void print(List<String> lines) {
        lines.forEach(System.out::println);
    }
}
