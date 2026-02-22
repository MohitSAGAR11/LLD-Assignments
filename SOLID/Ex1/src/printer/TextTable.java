package printer;

import entities.StudentRecord;

import java.util.List;

public class TextTable {
    public static String render3(List<StudentRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("| ID             | NAME | PROGRAM |\n");
        for (StudentRecord r : records) {
            sb.append(String.format("| %-14s | %-4s | %-7s |\n", r.getId(), r.getName(), r.getProgram()));
        }
        return sb.toString();
    }
}
