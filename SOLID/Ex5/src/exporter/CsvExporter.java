package exporter;

import dto.ExportRequest;
import dto.ExportResult;

import java.nio.charset.StandardCharsets;

public class CsvExporter extends Exporter {
    @Override
    protected ExportResult doExport(ExportRequest req) {
        String csv = "title,body\n" + csvField(req.title) + "," + csvField(req.body) + "\n";
        return new ExportResult("text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    private String csvField(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}