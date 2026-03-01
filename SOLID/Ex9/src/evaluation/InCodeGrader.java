package evaluation;

import config.Rubric;
import entities.Submission;

public interface InCodeGrader {
    public int grade(Submission s, Rubric r);
}
