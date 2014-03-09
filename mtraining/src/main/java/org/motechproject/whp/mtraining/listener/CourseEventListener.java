package org.motechproject.whp.mtraining.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mtraining.constants.MTrainingEventConstants;
import org.motechproject.whp.mtraining.ivr.CoursePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CourseEventListener {


    private CoursePublisher coursePublisher;

    @Autowired
    public CourseEventListener(CoursePublisher coursePublisher) {
        this.coursePublisher = coursePublisher;
    }

    @MotechListener(subjects = MTrainingEventConstants.COURSE_CREATION_EVENT)
    public void courseAdded(MotechEvent event) {
        Map<String, Object> eventData = event.getParameters();
        UUID courseId = (UUID) eventData.get(MTrainingEventConstants.CONTENT_ID);
        Integer version = (Integer) eventData.get(MTrainingEventConstants.VERSION);
        coursePublisher.publish(courseId, version);
    }

}