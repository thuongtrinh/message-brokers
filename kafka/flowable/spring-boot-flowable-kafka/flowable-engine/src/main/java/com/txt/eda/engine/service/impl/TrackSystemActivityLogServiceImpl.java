package com.txt.eda.engine.service.impl;

import com.txt.eda.engine.entities.ActivityLog;
import com.txt.eda.engine.repositories.ActivityLogRepository;
import com.txt.eda.engine.service.TrackSystemActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("trackSystemActivityLogService")
@RequiredArgsConstructor
@Slf4j
public class TrackSystemActivityLogServiceImpl implements TrackSystemActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Value("${syspro-version}")
    private String version;

    static final String SYSTEM_NAME = "Automation Bot";

    @Override
    public void trackInProgressEvent(DelegateExecution execution) {
        log.info("System action, track by event listener");
        ActivityLog activityLog = ActivityLog.builder()
                .processInstanceId(execution.getProcessInstanceId())
                .workFlowStage("ProgressEvent")
                .user(SYSTEM_NAME)
                .version(version)
                .build();
        activityLogRepository.save(activityLog);
    }

    @Override
    public void trackCompleteEvent(DelegateExecution execution) {
        log.info("System action, Completed track by event listener");
        ActivityLog activityLog = ActivityLog.builder()
                .processInstanceId(execution.getProcessInstanceId())
                .workFlowStage("CompleteEvent")
                .user(SYSTEM_NAME)
                .version(version)
                .build();
        activityLogRepository.save(activityLog);
    }

    @Override
    public void trackAQ(DelegateExecution execution) {
        log.info("System action, trackAQ track by event listener");
        ActivityLog activityLog = ActivityLog.builder()
                .processInstanceId(execution.getProcessInstanceId())
                .workFlowStage("trackAQ")
                .user(SYSTEM_NAME)
                .version(version)
                .build();
        activityLogRepository.save(activityLog);
    }

}
