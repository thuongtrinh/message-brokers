package com.txt.eda.rest.dto.task;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskActionRequestDTO {

    public static final String SUSPEND_OUTCOME = "suspend";
    public static final String COMPLETE_OUTCOME = "complete";
    public static final String REJECT_OUTCOME = "reject";
    public static final String TRANSFER_OUTCOME = "transfer";
    public static final String END_OUTCOME = "end";

    @Schema(description = "Person who performs task action", example = "userEmail", required = true)
    protected String author;

    @Schema(description = "Action to perform: Either complete or assign", example = "complete/assign", required = true)
    protected String action;

    @Schema(description = "Use this parameter to set the assignee associated ", example = "userWhoAssignTo")
    protected String assignee;

    @Schema(description = "Use this parameter to set the group assignee associated ", example = "userWhoGroupTo")
    protected String assigneeGroup;

    @Schema(description = "Comment for task action", example = "Assign task")
    protected String comment;

    @Schema(description = "Reason for task action", example = "reason code")
    protected String reason;
}
