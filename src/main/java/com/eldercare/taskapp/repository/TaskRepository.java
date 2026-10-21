package com.eldercare.taskapp.repository;

import com.eldercare.taskapp.model.Priority;
import com.eldercare.taskapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_IdOrderByDueDateAscDueTimeAsc(Long userId);

    List<Task> findByUser_IdAndDueDateOrderByDueTimeAsc(Long userId, LocalDate dueDate);

    List<Task> findByUser_IdAndIsCompletedOrderByDueDateAsc(Long userId, boolean isCompleted);

    List<Task> findByUser_IdAndPriorityOrderByDueDateAscDueTimeAsc(Long userId, Priority priority);

    long countByUser_Id(Long userId);

    long countByUser_IdAndIsCompleted(Long userId, boolean isCompleted);

    long countByUser_IdAndDueDate(Long userId, LocalDate dueDate);

    long countByUser_IdAndDueDateAndIsCompleted(Long userId, LocalDate dueDate, boolean isCompleted);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.reminderEnabled = true AND t.isCompleted = false AND t.dueDate = :today")
    List<Task> findTodayActiveReminders(@Param("userId") Long userId, @Param("today") LocalDate today);
}
