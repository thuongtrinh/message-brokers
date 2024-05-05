package com.txt.eda.engine.repositories;

import com.txt.eda.engine.entities.ActivityLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends CrudRepository<ActivityLog, String> {
}
