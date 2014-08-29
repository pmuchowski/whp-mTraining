package org.motechproject.whp.mtraining.service;

import org.motechproject.whp.mtraining.dto.CourseUnitMetadataDto;
import org.motechproject.whp.mtraining.dto.QuestionDto;
import org.motechproject.whp.mtraining.domain.ContentIdentifier;

import java.util.List;
import java.util.UUID;

/**
 * Includes operation over String content value
 * and other variable manipulation
 */
public interface ContentOperationService {

    void getFileNameAndDescriptionFromContent(CourseUnitMetadataDto courseUnitMetadataDto, String content);

    String codeIntoContent(String filename, String description, UUID uuid);


    void getQuestionNameAndDescriptionFromQuestion(QuestionDto questionDto, String question);

    String codeIntoQuestion(String questionName, String description, UUID uuid);

    void getAnswersAndFilesNamesFromAnswer(QuestionDto questionDto, String answer);

    String codeAnswersAndFilesNamesIntoAnswer(String correctOption, List<Integer> options, String filename, String explainingAnswerFilename);


    UUID getUuidFromJsonString(String content);

    ContentIdentifier getContentIdentifierFromString(String content);

    String codeContentIdentifierIntoString(ContentIdentifier content);
}
