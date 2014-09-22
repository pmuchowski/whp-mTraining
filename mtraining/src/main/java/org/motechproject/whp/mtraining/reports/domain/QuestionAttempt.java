package org.motechproject.whp.mtraining.reports.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mtraining.domain.MdsEntity;
import org.motechproject.whp.mtraining.domain.ContentIdentifier;
import org.motechproject.mds.annotations.Entity;

@Entity
public class QuestionAttempt extends MdsEntity {

    @Field
    private QuizAttempt quizAttempt;

    @Field
    private ContentIdentifier question;

    @Field
    private String invalidInputs;

    @Field
    private String selectedOption;

    @Field
    private Boolean isCorrectAnswer;

    @Field
    private Boolean invalidAttempt;

    @Field
    private Boolean timeOut;

    public QuestionAttempt(QuizAttempt quizAttempt, ContentIdentifier question, String invalidInputs, String selectedOption, Boolean isCorrectAnswer, Boolean invalidAttempt, Boolean timeOut) {
        this.quizAttempt = quizAttempt;
        this.question = question;
        this.invalidInputs = invalidInputs;
        this.selectedOption = selectedOption;
        this.isCorrectAnswer = isCorrectAnswer;
        this.invalidAttempt = invalidAttempt;
        this.timeOut = timeOut;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public ContentIdentifier getQuestion() {
        return question;
    }

    public void setQuestion(ContentIdentifier question) {
        this.question = question;
    }

    public String getInvalidInputs() {
        return invalidInputs;
    }

    public void setInvalidInputs(String invalidInputs) {
        this.invalidInputs = invalidInputs;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Boolean getIsCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(Boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public Boolean getInvalidAttempt() {
        return invalidAttempt;
    }

    public void setInvalidAttempt(Boolean invalidAttempt) {
        this.invalidAttempt = invalidAttempt;
    }

    public Boolean getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Boolean timeOut) {
        this.timeOut = timeOut;
    }
}
