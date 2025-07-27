package com.txt.eda.engine.service;

import org.flowable.engine.delegate.DelegateExecution;

public interface TrackSystemActivityLogService {

    void trackInProgressEvent(DelegateExecution execution);
    void trackCompleteEvent(DelegateExecution execution);
    void trackAQ(DelegateExecution execution);
    void trackAssignment(DelegateExecution execution);
}
