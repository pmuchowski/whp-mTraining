package org.motechproject.whp.mtraining.service.impl;

import org.motechproject.mtraining.domain.*;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.whp.mtraining.domain.CoursePlan;
import org.motechproject.whp.mtraining.domain.ManyToManyRelation;
import org.motechproject.whp.mtraining.domain.ParentType;
import org.motechproject.whp.mtraining.dto.*;
import org.motechproject.whp.mtraining.service.ContentOperationService;
import org.motechproject.whp.mtraining.service.CoursePlanService;
import org.motechproject.whp.mtraining.service.DtoFactoryService;
import org.motechproject.whp.mtraining.service.ManyToManyRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("dtoFactoryService")
public class DtoFactoryServiceImpl implements DtoFactoryService {

    @Autowired
    CoursePlanService coursePlanService;

    @Autowired
    MTrainingService mTrainingService;

    @Autowired
    ContentOperationService contentOperationService;

    @Autowired
    ManyToManyRelationService manyToManyRelationService;

    @Override
    public List<CoursePlanDto> getAllCourseDtosWithChildCollections() {
        List<CoursePlan> allCourses = coursePlanService.getAllCoursePlans();
        List<CoursePlanDto> allCoursesPlanDto = convertCoursePlanListToDtos(allCourses);

        for (CoursePlanDto coursePlanDto: allCoursesPlanDto) {
            setChildCollections(coursePlanDto);
        }
        return allCoursesPlanDto;
    }

    private void setChildCollections(CoursePlanDto coursePlanDto) {
        List<ModuleDto> childModuleDtos;
        List<ChapterDto> childChapterDtos;

        childModuleDtos = convertModuleListToDtos(manyToManyRelationService.getCoursesByParentId(coursePlanDto.getId()));
        for (ModuleDto moduleDto : childModuleDtos) {
            childChapterDtos = convertChapterListToDtos(manyToManyRelationService.getChaptersByParentId(moduleDto.getId()));
            for (ChapterDto chapterDto : childChapterDtos) {
                chapterDto.setLessons(convertLessonListToDtos(manyToManyRelationService.getLessonsByParentId(chapterDto.getId())));
            }
            moduleDto.setChapters(childChapterDtos);
        }
        coursePlanDto.setCourses(childModuleDtos);
    }

    @Override
    public void createOrUpdateFromDto(CourseUnitMetadataDto courseUnitMetadataDto) {
        if (courseUnitMetadataDto.getId() == 0) {
            createCourseUnitMetadataFromDto(courseUnitMetadataDto);
        }
        else {
            updateCourseUnitMetadataFromDto(courseUnitMetadataDto);
        }
    }

    @Override
    public List<CoursePlanDto> getAllCoursePlanDtos() {
        List<CoursePlan> allCourses = coursePlanService.getAllCoursePlans();
        return convertCoursePlanListToDtos(allCourses);
    }

    @Override
    public CoursePlanDto getCoursePlanDtoById(long courseId) {
        return convertCoursePlanToDto(coursePlanService.getCoursePlanById(courseId));
    }

    @Override
    public CoursePlanDto convertCoursePlanToDto(CoursePlan coursePlan) {
        CoursePlanDto coursePlanDto = new CoursePlanDto(coursePlan.getId(), coursePlan.getName(), coursePlan.getState(),
                coursePlan.getCreationDate(), coursePlan.getModificationDate());

        contentOperationService.getFileNameAndDescriptionFromContent(coursePlanDto, coursePlan.getContent());

        return coursePlanDto;
    }

    @Override
    public List<CoursePlanDto> convertCoursePlanListToDtos(List<CoursePlan> coursePlans) {
        List<CoursePlanDto> coursePlanDtos = new ArrayList<>();

        for (CoursePlan coursePlan : coursePlans) {
            coursePlanDtos.add(convertCoursePlanToDto(coursePlan));
        }
        return coursePlanDtos;
    }


    @Override
    public List<ModuleDto> getAllModuleDtos() {
        List<Course> allCourses = mTrainingService.getAllCourses();
        return convertModuleListToDtos(allCourses);
    }

    @Override
    public ModuleDto getModuleDtoById(long moduleId) {
        return convertModuleToDto(mTrainingService.getCourseById(moduleId));
    }

    @Override
    public ModuleDto convertModuleToDto(Course module) {
        ModuleDto moduleDto = new ModuleDto(module.getId(), module.getName(), module.getState(),
                module.getCreationDate(), module.getModificationDate());

        contentOperationService.getFileNameAndDescriptionFromContent(moduleDto, module.getContent());

        moduleDto.setParentIds(convertToIdSet(manyToManyRelationService.getCoursePlansByChildId(module.getId())));

        return moduleDto;
    }

    @Override
    public List<ModuleDto> convertModuleListToDtos(List<Course> modules) {
        List<ModuleDto> moduleDtos = new ArrayList<>();

        for (Course module : modules) {
            moduleDtos.add(convertModuleToDto(module));
        }
        return moduleDtos;
    }


    @Override
    public List<ChapterDto> getAllChapterDtos() {
        List<Chapter> allCourses = mTrainingService.getAllChapters();
        return convertChapterListToDtos(allCourses);
    }

    @Override
    public ChapterDto getChapterDtoById(long chapterId) {
        return convertChapterToDto(mTrainingService.getChapterById(chapterId));
    }

    @Override
    public ChapterDto convertChapterToDto(Chapter chapter) {
        ChapterDto chapterDto = new ChapterDto(chapter.getId(), chapter.getName(), chapter.getState(),
                chapter.getCreationDate(), chapter.getModificationDate());

        contentOperationService.getFileNameAndDescriptionFromContent(chapterDto, chapter.getContent());

        chapterDto.setParentIds(convertToIdSet(manyToManyRelationService.getCoursesByChildId(chapter.getId())));

        return chapterDto;
    }

    @Override
    public List<ChapterDto> convertChapterListToDtos(List<Chapter> chapters) {
        List<ChapterDto> chapterDtos = new ArrayList<>();

        for (Chapter chapter : chapters) {
            chapterDtos.add(convertChapterToDto(chapter));
        }
        return chapterDtos;
    }


    @Override
    public List<LessonDto> getAllLessonDtos() {
        List<Lesson> allCourses = mTrainingService.getAllLessons();
        return convertLessonListToDtos(allCourses);
    }

    @Override
    public LessonDto getLessonDtoById(long lessonId) {
        return convertLessonToDto(mTrainingService.getLessonById(lessonId));
    }

    @Override
    public LessonDto convertLessonToDto(Lesson lesson) {
        LessonDto lessonDto = new LessonDto(lesson.getId(), lesson.getName(), lesson.getState(),
                lesson.getCreationDate(), lesson.getModificationDate());

        contentOperationService.getFileNameAndDescriptionFromContent(lessonDto, lesson.getContent());

        lessonDto.setParentIds(convertToIdSet(manyToManyRelationService.getChaptersByChildId(lesson.getId())));

        return lessonDto;
    }

    @Override
    public List<LessonDto> convertLessonListToDtos(List<Lesson> lessons) {
        List<LessonDto> lessonDtos = new ArrayList<>();

        for (Lesson lesson : lessons) {
            lessonDtos.add(convertLessonToDto(lesson));
        }
        return lessonDtos;
    }


    @Override
    public List<QuizDto> getAllQuizDtos() {
        List<Quiz> allCourses = mTrainingService.getAllQuizzes();
        return convertQuizListToDtos(allCourses);
    }

    @Override
    public QuizDto getQuizDtoById(long quizId) {
        return convertQuizToDto(mTrainingService.getQuizById(quizId));
    }

    @Override
    public QuizDto convertQuizToDto(Quiz quiz) {
        QuizDto quizDto = new QuizDto(quiz.getId(), quiz.getName(), quiz.getState(),
                quiz.getCreationDate(), quiz.getModificationDate(), quiz.getPassPercentage(), convertQuestionListToDtos(quiz.getQuestions()));

        contentOperationService.getFileNameAndDescriptionFromContent(quizDto, quiz.getContent());

        return quizDto;
    }

    @Override
    public List<QuizDto> convertQuizListToDtos(List<Quiz> quizzes) {
        List<QuizDto> quizDtos = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            quizDtos.add(convertQuizToDto(quiz));
        }
        return quizDtos;
    }

    @Override
    public QuestionDto convertQuestionToDto(Question question) {
        QuestionDto questionDto = new QuestionDto();
        contentOperationService.getQuestionNameAndDescriptionFromQuestion(questionDto, question.getQuestion());
        contentOperationService.getAnswersAndFilesNamesFromAnswer(questionDto, question.getAnswer());
        return questionDto;
    }

    @Override
    public List<QuestionDto> convertQuestionListToDtos(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
            questionDtos.add(convertQuestionToDto(question));
        }
        return questionDtos;
    }

    @Override
    public List<Question> convertDtosToQuestionList(List<QuestionDto> questionDtos) {
        List<Question> questions = new ArrayList<>();

        for (QuestionDto questionDto : questionDtos) {
            questions.add(convertDtoToQuestion(questionDto));
        }
        return questions;
    }

    private Question convertDtoToQuestion(QuestionDto questionDto) {
        String question = contentOperationService.codeQuestionNameAndDescriptionIntoQuestion(questionDto.getName(), questionDto.getDescription());
        String answer = contentOperationService.codeAnswersAndFilesNamesIntoAnswer(questionDto.getCorrectAnswer(), questionDto.getOptions(),
                questionDto.getFilename(), questionDto.getExplainingAnswerFilename());
        Question questionObject = new Question(question, answer);
        return questionObject;
    }

    private void createCourseUnitMetadataFromDto(CourseUnitMetadataDto courseUnitMetadataDto) {
        if (courseUnitMetadataDto instanceof CoursePlanDto) {
            CoursePlan coursePlan =  new CoursePlan(courseUnitMetadataDto.getName(), courseUnitMetadataDto.getState(),
                    contentOperationService.codeFileNameAndDescriptionIntoContent(courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()));
            coursePlanService.createCoursePlan(coursePlan);
        }

        else if (courseUnitMetadataDto instanceof ModuleDto) {
            Course module = new Course(courseUnitMetadataDto.getName(), courseUnitMetadataDto.getState(),
                    contentOperationService.codeFileNameAndDescriptionIntoContent(courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()));
            module = mTrainingService.createCourse(module);
            createRelation(module, courseUnitMetadataDto);
        }

        else if (courseUnitMetadataDto instanceof ChapterDto) {
            Chapter chapter = new Chapter(courseUnitMetadataDto.getName(), courseUnitMetadataDto.getState(),
                    contentOperationService.codeFileNameAndDescriptionIntoContent(courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()));
            chapter = mTrainingService.createChapter(chapter);
            createRelation(chapter, courseUnitMetadataDto);
        }

        else if (courseUnitMetadataDto instanceof LessonDto) {
            Lesson lesson = new Lesson(courseUnitMetadataDto.getName(), courseUnitMetadataDto.getState(),
                    contentOperationService.codeFileNameAndDescriptionIntoContent(courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()));
            lesson = mTrainingService.createLesson(lesson);
            createRelation(lesson, courseUnitMetadataDto);
        }

        else {
            Quiz quiz = new Quiz(courseUnitMetadataDto.getName(), courseUnitMetadataDto.getState(),
                    contentOperationService.codeFileNameAndDescriptionIntoContent(courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()),
                    convertDtosToQuestionList(((QuizDto) courseUnitMetadataDto).getQuestions()), ((QuizDto) courseUnitMetadataDto).getPassPercentage());
            mTrainingService.createQuiz(quiz);
        }
    }

    private void updateCourseUnitMetadataFromDto (CourseUnitMetadataDto courseUnitMetadataDto) {
        if (courseUnitMetadataDto instanceof CoursePlanDto) {
            CoursePlan coursePlan = coursePlanService.getCoursePlanById(courseUnitMetadataDto.getId());
            populateCourseUnitMetadataFields(coursePlan, courseUnitMetadataDto);
            coursePlanService.updateCoursePlan(coursePlan);
        }

        else if (courseUnitMetadataDto instanceof ModuleDto) {
            Course module = mTrainingService.getCourseById(courseUnitMetadataDto.getId());
            populateCourseUnitMetadataFields(module, courseUnitMetadataDto);
            mTrainingService.updateCourse(module);
            manyToManyRelationService.deleteRelationsByChildId(ParentType.CoursePlan, module.getId());
            createRelation(module, courseUnitMetadataDto);
        }

        else if (courseUnitMetadataDto instanceof ChapterDto) {
            Chapter chapter = mTrainingService.getChapterById(courseUnitMetadataDto.getId());
            populateCourseUnitMetadataFields(chapter, courseUnitMetadataDto);
            mTrainingService.updateChapter(chapter);
            manyToManyRelationService.deleteRelationsByChildId(ParentType.Course, chapter.getId());
            createRelation(chapter, courseUnitMetadataDto);
        }

        else if (courseUnitMetadataDto instanceof LessonDto) {
            Lesson lesson = mTrainingService.getLessonById(courseUnitMetadataDto.getId());
            populateCourseUnitMetadataFields(lesson, courseUnitMetadataDto);
            mTrainingService.updateLesson(lesson);
            manyToManyRelationService.deleteRelationsByChildId(ParentType.Chapter, lesson.getId());
            createRelation(lesson, courseUnitMetadataDto);
        }

        else {
            Quiz quiz = mTrainingService.getQuizById(courseUnitMetadataDto.getId());
            populateCourseUnitMetadataFields(quiz, courseUnitMetadataDto);
            quiz.setPassPercentage(((QuizDto) courseUnitMetadataDto).getPassPercentage());
            quiz.setQuestions(convertDtosToQuestionList(((QuizDto) courseUnitMetadataDto).getQuestions()));
            mTrainingService.updateQuiz(quiz);
        }
    }

    private void createRelation(CourseUnitMetadata courseUnitMetadata, CourseUnitMetadataDto courseUnitMetadataDto) {
        if (courseUnitMetadataDto instanceof ModuleDto) {
            for (Long id : ((ModuleDto) courseUnitMetadataDto).getParentIds()) {
                ManyToManyRelation relation = new ManyToManyRelation(id, courseUnitMetadata.getId(), ParentType.CoursePlan);
                manyToManyRelationService.createRelation(relation);
            }
        }

        else if (courseUnitMetadataDto instanceof ChapterDto) {
            for (Long id : ((ChapterDto) courseUnitMetadataDto).getParentIds()) {
                ManyToManyRelation relation = new ManyToManyRelation(id, courseUnitMetadata.getId(), ParentType.Course);
                manyToManyRelationService.createRelation(relation);
            }
        }

        else if (courseUnitMetadataDto instanceof LessonDto) {
            for (Long id : ((LessonDto) courseUnitMetadataDto).getParentIds()) {
                ManyToManyRelation relation = new ManyToManyRelation(id, courseUnitMetadata.getId(), ParentType.Chapter);
                manyToManyRelationService.createRelation(relation);
            }
        }
    }

    private void populateCourseUnitMetadataFields (CourseUnitMetadata courseUnitMetadata, CourseUnitMetadataDto courseUnitMetadataDto) {
        courseUnitMetadata.setName(courseUnitMetadataDto.getName());
        courseUnitMetadata.setState(courseUnitMetadataDto.getState());
        courseUnitMetadata.setContent(contentOperationService.codeFileNameAndDescriptionIntoContent
                (courseUnitMetadataDto.getFilename(), courseUnitMetadataDto.getDescription()));
    }

    private Set<Long> convertToIdSet(List<?> courseUnitMetadata) {
        Set<Long> ids = new LinkedHashSet<Long>();
        for(Object metadata : courseUnitMetadata) {
            ids.add(((CourseUnitMetadata) metadata).getId());
        }
        return ids;
    }
}
