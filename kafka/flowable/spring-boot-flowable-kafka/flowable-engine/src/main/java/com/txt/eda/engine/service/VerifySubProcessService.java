package com.txt.eda.engine.service;


import org.flowable.engine.delegate.DelegateExecution;

public interface VerifySubProcessService {

    public void initVariable(DelegateExecution execution);

    public void returnProcess(DelegateExecution execution);
}
