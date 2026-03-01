package evaluation;

import config.Rubric;
import entities.Submission;

public class CodeGrader implements InCodeGrader {
    public int grade(Submission s, Rubric r) {
        // fake scoring (but deterministic)
        int base = Math.min(80, 50 + s.code.length() % 40);
        return base + r.bonus;
    }
}
