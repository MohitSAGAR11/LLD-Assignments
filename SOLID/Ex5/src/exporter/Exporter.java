package exporter;

import dto.ExportRequest;
import dto.ExportResult;

public abstract class Exporter {
    public final ExportResult export(ExportRequest req) {
        if (req == null) throw new IllegalArgumentException("req must not be null");
        ExportResult result = doExport(req);
        if (result == null)
            throw new IllegalStateException(getClass().getSimpleName() + ".doExport() returned null");
        if (result.contentType == null || result.contentType.isEmpty())
            throw new IllegalStateException(getClass().getSimpleName() + " returned blank mimeType");
        if (result.bytes == null)
            throw new IllegalStateException(getClass().getSimpleName() + " returned null data");

        return result;
    }

    protected abstract ExportResult doExport(ExportRequest req);
}