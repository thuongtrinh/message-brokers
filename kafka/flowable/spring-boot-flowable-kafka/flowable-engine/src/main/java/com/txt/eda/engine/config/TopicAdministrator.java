package com.txt.eda.engine.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class TopicAdministrator {

    @Value("${kafka.topic.num-partitions}")
    private String numberOfPartitions;

    @Value("${kafka.topic.replication-factor}")
    private String replicationFactor;

    @Value("${trigger.topic}")
    private String triggerTopic;

    @Value("${trigger.cs-topic}")
    private String csTriggerTopic;

    @Bean
    public NewTopic trigger() {
        return TopicBuilder.name(triggerTopic)
                .partitions(Integer.valueOf(numberOfPartitions))
                .replicas(Integer.valueOf(replicationFactor))
                .build();
    }

    @Bean
    public NewTopic triggerCS() {
        return TopicBuilder.name(csTriggerTopic)
                .partitions(Integer.valueOf(numberOfPartitions))
                .replicas(Integer.valueOf(replicationFactor))
                .build();
    }

}
