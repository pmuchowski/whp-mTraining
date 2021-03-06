package org.motechproject.whp.mtraining.web.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionRequest {
    @JsonProperty
    private String questionId;
    @JsonProperty
    private Integer version;
    @JsonProperty
    private List<String> invalidInputs;
    @JsonProperty
    private String selectedOption;
    @JsonProperty
    private Boolean timeOut = false;
    @JsonProperty
    private Boolean invalidAttempt = false;

    public QuestionRequest() {
    }

    public QuestionRequest(String questionId, Integer version, List<String> invalidInputs, String selectedOption, Boolean timeOut, Boolean invalidAttempt) {
        this.questionId = questionId;
        this.version = version;
        this.invalidInputs = invalidInputs;
        this.selectedOption = selectedOption;
        this.timeOut = timeOut;
        this.invalidAttempt = invalidAttempt;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (questionId == null || version == null)
            errors.add(new ValidationError(ResponseStatus.MISSING_QUESTION_ID));
        return errors;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public String getQuestionId() {
        return questionId;
    }

    public List<String> getInvalidInputs() {
        return invalidInputs;
    }

    public Integer getVersion() {
        return version;
    }

    public Boolean getTimeOut() {
        return timeOut;
    }

    public Boolean getInvalidAttempt() {
        return invalidAttempt;
    }
}
