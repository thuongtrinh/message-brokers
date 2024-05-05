package com.txt.eda.rest.entities.postgres;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "act_hi_procinst")
public class HistoricProcessInstance {
    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "proc_inst_id_")
    private String processInstanceId;

    @Column(name = "business_key_")
    private String businessKey;

    @Column(name = "proc_def_id_")
    private String processDefinitionId;

    @Column(name = "start_time_")
    private Date startTime;

    @Column(name = "end_time_")
    private Date endTime;

    @Column(name = "duration_")
    private Double duration;

    @Column(name = "tenant_id_")
    private String tenantId;

    @OneToMany(mappedBy = "processInstanceId")
    private List<VariableProcessInstance> variableProcessInstanceList;

    public List<VariableProcessInstance> getVariableProcessInstanceList() {
        return variableProcessInstanceList;
    }

    public void setVariableProcessInstanceList(List<VariableProcessInstance> variableProcessInstanceList) {
        this.variableProcessInstanceList = variableProcessInstanceList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
