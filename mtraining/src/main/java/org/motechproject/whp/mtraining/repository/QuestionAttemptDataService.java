package org.motechproject.whp.mtraining.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.whp.mtraining.reports.domain.QuestionAttempt;

public interface QuestionAttemptDataService extends MotechDataService<QuestionAttempt> {

    @Lookup
    QuestionAttempt findQuestionAttemptById(@LookupField(name = "id") long id);

}