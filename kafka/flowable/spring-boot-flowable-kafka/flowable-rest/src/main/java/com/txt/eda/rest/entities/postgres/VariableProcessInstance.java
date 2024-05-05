package com.txt.eda.rest.entities.postgres;

import javax.persistence.*;

@Entity
@Table(name = "act_hi_varinst")
public class VariableProcessInstance {
    @Id
    @Column(name = "id_")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proc_inst_id_")
    private HistoricProcessInstance processInstanceId;

    @Column(name = "execution_id_")
    private String excutionId;

    @Column(name = "task_id_")
    private String taskId;

    @Column(name = "name_")
    private String name;

    @Column(name = "var_type_")
    private String varType;

    @Column(name = "double_")
    private Double varDouble;

    @Column(name = "long_")
    private Long varLong;

    @Column(name = "text_")
    private String text;

    @Column(name = "text2_")
    private String text2;

    @OneToOne
    @JoinColumn(name = "bytearray_id_")
    private VariableByteArray bytearray;

    public VariableByteArray getBytearray() {
        return bytearray;
    }

    public void setBytearray(VariableByteArray bytearray) {
        this.bytearray = bytearray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HistoricProcessInstance getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(HistoricProcessInstance processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExcutionId() {
        return excutionId;
    }

    public void setExcutionId(String excutionId) {
        this.excutionId = excutionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

    public Double getVarDouble() {
        return varDouble;
    }

    public void setVarDouble(Double varDouble) {
        this.varDouble = varDouble;
    }

    public Long getVarLong() {
        return varLong;
    }

    public void setVarLong(Long varLong) {
        this.varLong = varLong;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
