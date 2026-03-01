package evaluation;

import entities.Submission;

public class PlagiarismChecker implements InPlagiarismChecker{
    public int check(Submission s) {
        // fake score: lower is "better", but pipeline adds it anyway (smell)
        return (s.code.contains("class") ? 12 : 40);
    }
}
