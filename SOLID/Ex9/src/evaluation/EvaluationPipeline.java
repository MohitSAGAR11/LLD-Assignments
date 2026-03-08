package evaluation;

import config.Rubric;
import entities.Submission;
import printer.SimpleConsole;

public class EvaluationPipeline {
    // DIP violation: high-level module constructs concretes directly

    private final InCodeGrader codeGrader;
    private final InPlagiarismChecker plagiarismChecker;
    private final InReportWriter reportWriter;
    private final Rubric rubric;
    private final SimpleConsole console;

    public EvaluationPipeline(InCodeGrader codeGrader,
                              InPlagiarismChecker plagiarismChecker,
                              InReportWriter reportWriter,
                              Rubric rubric,
                              SimpleConsole console) {
        this.codeGrader = codeGrader;
        this.plagiarismChecker = plagiarismChecker;
        this.reportWriter = reportWriter;
        this.rubric = rubric;
        this.console = console;
    }

    public void evaluate(Submission sub) {

        int plag = plagiarismChecker.check(sub);
        console.log("PlagiarismScore=" + plag);

        int code = codeGrader.grade(sub, rubric);
        console.log("CodeScore=" + code);

        String reportName = reportWriter.write(sub, plag, code);
        console.log("Report written: " + reportName);

        int total = plag + code;
        String result = (total >= 90) ? "PASS" : "FAIL";
        console.log("FINAL: " + result + " (total=" + total + ")");
    }
}
