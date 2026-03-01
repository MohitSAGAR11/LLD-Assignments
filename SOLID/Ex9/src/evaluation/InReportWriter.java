package evaluation;

import entities.Submission;

public interface InReportWriter {
    String write(Submission submission , int plag, int code);
}
