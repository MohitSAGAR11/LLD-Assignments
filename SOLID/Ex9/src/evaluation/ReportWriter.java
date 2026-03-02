package evaluation;

import entities.Submission;

public class ReportWriter implements InReportWriter {

    public String write(Submission s, int plag, int code) {
        // writes to a pretend file name
        return "report-" + s.roll + ".txt";
    }
}
