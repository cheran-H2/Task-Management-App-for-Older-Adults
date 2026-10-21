package com.eldercare.taskapp.repository;

import com.eldercare.taskapp.model.TaskActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskActivityLogRepository extends JpaRepository<TaskActivityLog, Long> {
    List<TaskActivityLog> findByUserIdOrderByLoggedAtDesc(Long userId);
    long countByUserIdAndActionType(Long userId, String actionType);
}
