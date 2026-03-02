package exporter;

import dto.ExportRequest;
import dto.ExportResult;

import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
    private static final int BODY_LIMIT = 20;

    @Override
    protected ExportResult doExport(ExportRequest req) {
        String body = req.body;
        if (body.length() > BODY_LIMIT) {
            throw new IllegalArgumentException("PDF cannot handle content > " + BODY_LIMIT + " chars");
        }
        String fakePdf = "PDF(" + req.title + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}