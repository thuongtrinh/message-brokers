package com.txt.eda.engine.config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.logging.LoggingListener;
import org.flowable.common.engine.impl.logging.LoggingSessionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class FlowableLoggingListener implements LoggingListener {

    private final ExecutorService executorService;

    @Autowired
    public FlowableLoggingListener(@Lazy @Qualifier("fixedThreadPool") ExecutorService executorService
    ) {
        this.executorService = executorService;
    }


    @Override
    public void loggingGenerated(List<ObjectNode> loggingNodes) {

        if (CollectionUtils.isEmpty(loggingNodes)) return;

        Runnable runnableAuditLogTask = () -> {
            auditLogParser(loggingNodes);
        };
        executorService.execute(runnableAuditLogTask);

        Boolean error = false;
        // Dont take error
        for (ObjectNode jsonNodes : loggingNodes) {
            if (jsonNodes.get("type") != null && jsonNodes.get("type").asText().equals("commandContextCloseFailure")) {
                error = true;
            }
        }
        if (!error) {
            for (ObjectNode jsonNodes : loggingNodes) {
                log.debug("loggingGenerated - loggingNodes: {}", jsonNodes.toString());
                if (jsonNodes.get("elementId") != null // get stage not null (ex: in-progress, complete)
                        && jsonNodes.get("scopeId") != null // process instance id not null
                        && jsonNodes.get("__logNumber").asInt() == 1 // Only the first log
                        && jsonNodes.get("message").asText().startsWith("Executing")) { // Only run when it is executing event

                    Runnable runnableTask = () -> {
                        String elementId = jsonNodes.get("elementId").asText();
                        String processInstanceId = jsonNodes.get("scopeId").asText();
                        String elementType = jsonNodes.get("elementType").asText();
                    };
                    executorService.execute(runnableTask);
                }
            }
        }
    }

    public void auditLogParser(List<ObjectNode> loggingNodes) {

        for (ObjectNode item : loggingNodes) {
            String type = item.get("type") != null ? item.get("type").asText() : null;

            if (LoggingSessionConstants.TYPE_VARIABLE_CREATE.equalsIgnoreCase(type)
                    || LoggingSessionConstants.TYPE_VARIABLE_UPDATE.equalsIgnoreCase(type)
                    || LoggingSessionConstants.TYPE_VARIABLE_DELETE.equalsIgnoreCase(type)) {
                //skip
            } else {
                String processInstanceId = item.get("scopeId") != null ? item.get("scopeId").asText() : null;
                String elementType = item.get("elementType").asText("elementType");
                String elementName = item.get("elementName").asText("elementName");
                String message = item.get("message").asText("message");
                String startDate = item.get("__timeStamp").asText("__timeStamp");//2022-07-10T22:34:6.451Z

                log.info("processInstanceId={}, step={}, stepType={}, type={}, message={}, timeStamp={}", processInstanceId, elementName, elementType, type, message, startDate);
            }
        }
    }

    Comparator<ObjectNode> compareBylogNumber = new Comparator<ObjectNode>() {
        @Override
        public int compare(ObjectNode o1, ObjectNode o2) {
            Integer __logNumber1 = o1.get("__logNumber").asInt(0);
            Integer __logNumber2 = o2.get("__logNumber").asInt(0);
            return __logNumber1.compareTo(__logNumber2);
        }
    };
}
