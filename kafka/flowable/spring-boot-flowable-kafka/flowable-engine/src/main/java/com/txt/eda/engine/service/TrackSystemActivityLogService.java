package com.txt.eda.engine.service;

import org.flowable.engine.delegate.DelegateExecution;

public interface TrackSystemActivityLogService {

    void trackInProgressEvent(DelegateExecution execution);
    void trackCompleteEvent(DelegateExecution execution);
    void trackAQ(DelegateExecution execution);

//    void trackReviewEndEvent(DelegateExecution execution);
//    void trackRejectEvent(DelegateExecution execution);
//    void trackSuspendEvent(DelegateExecution execution);
//    void trackUnsuspendEvent(DelegateExecution execution);
//    void trackAutoEndEvent(DelegateExecution execution);
}
