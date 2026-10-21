package com.eldercare.taskapp;

import com.eldercare.taskapp.model.*;
import com.eldercare.taskapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                  UserPreferenceRepository preferenceRepository, 
                                  TaskRepository taskRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Initialize default senior user
                User eleanor = new User();
                eleanor.setFullName("Eleanor Vance");
                eleanor.setEmail("eleanor@eldercare.test");
                eleanor.setPassword("pass123");
                eleanor.setPhoneNumber("(555) 234-5678");
                eleanor.setEmergencyContactName("Sarah Vance (Daughter)");
                eleanor.setEmergencyContactPhone("(555) 876-5432");
                eleanor = userRepository.save(eleanor);

                // Initialize accessibility preferences
                UserPreference prefs = new UserPreference(eleanor.getId());
                prefs.setFontSize("large");
                prefs.setTheme("cream");
                prefs.setSpeechEnabled(true);
                prefs.setSpeechSpeed(0.85);
                prefs.setSoundEffectsEnabled(true);
                prefs.setSimplifiedView(false);
                preferenceRepository.save(prefs);

                // Initialize realistic older adult tasks
                Task t1 = new Task();
                t1.setUser(eleanor);
                t1.setTitle("Take Morning Heart Medication (Metoprolol 25mg)");
                t1.setDescription("Take 1 tablet with a full glass of water right after breakfast.");
                t1.setCategory(Category.MEDICATION);
                t1.setPriority(Priority.HIGH);
                t1.setDueDate(LocalDate.now());
                t1.setDueTime(LocalTime.of(8, 30));
                t1.setReminderEnabled(true);
                t1.setReminderMinutesBefore(15);
                t1.setCompleted(true);
                t1.setRecurringPattern("DAILY");
                t1.setNotes("Prescribed by Dr. Harrison. Stored in blue weekly pill organizer.");
                taskRepository.save(t1);

                Task t2 = new Task();
                t2.setUser(eleanor);
                t2.setTitle("Dr. Harrison - Annual Vision & Eye Checkup");
                t2.setDescription("Appointment at Maplewood Senior Medical Center, Room 204. Daughter Sarah is driving.");
                t2.setCategory(Category.APPOINTMENT);
                t2.setPriority(Priority.HIGH);
                t2.setDueDate(LocalDate.now());
                t2.setDueTime(LocalTime.of(14, 0));
                t2.setReminderEnabled(true);
                t2.setReminderMinutesBefore(30);
                t2.setCompleted(false);
                t2.setRecurringPattern("NONE");
                t2.setNotes("Remember to bring reading glasses and current insurance card.");
                taskRepository.save(t2);

                Task t3 = new Task();
                t3.setUser(eleanor);
                t3.setTitle("Gentle 15-Minute Garden Walk & Stretching");
                t3.setDescription("Walk around the backyard patio and do breathing exercises in the warm sun.");
                t3.setCategory(Category.HEALTH);
                t3.setPriority(Priority.MEDIUM);
                t3.setDueDate(LocalDate.now());
                t3.setDueTime(LocalTime.of(10, 30));
                t3.setReminderEnabled(true);
                t3.setReminderMinutesBefore(10);
                t3.setCompleted(true);
                t3.setRecurringPattern("DAILY");
                t3.setNotes("Wear supportive walking shoes and take walking stick.");
                taskRepository.save(t3);

                Task t4 = new Task();
                t4.setUser(eleanor);
                t4.setTitle("Water the Lavender & Porch Petunias");
                t4.setDescription("Fill the light green watering can once and gently water the porch planters.");
                t4.setCategory(Category.ROUTINE);
                t4.setPriority(Priority.MEDIUM);
                t4.setDueDate(LocalDate.now());
                t4.setDueTime(LocalTime.of(16, 30));
                t4.setReminderEnabled(true);
                t4.setReminderMinutesBefore(15);
                t4.setCompleted(false);
                t4.setRecurringPattern("DAILY");
                t4.setNotes("Check soil moisture before watering.");
                taskRepository.save(t4);

                Task t5 = new Task();
                t5.setUser(eleanor);
                t5.setTitle("Call Granddaughter Sarah for Birthday Chat");
                t5.setDescription("Wish Sarah a happy 18th birthday! Sarah loves talking about her college prep.");
                t5.setCategory(Category.SOCIAL);
                t5.setPriority(Priority.LOW);
                t5.setDueDate(LocalDate.now());
                t5.setDueTime(LocalTime.of(17, 30));
                t5.setReminderEnabled(true);
                t5.setReminderMinutesBefore(15);
                t5.setCompleted(false);
                t5.setRecurringPattern("NONE");
                t5.setNotes("Sarah's cell number: (555) 876-5432.");
                taskRepository.save(t5);

                Task t6 = new Task();
                t6.setUser(eleanor);
                t6.setTitle("Read 2 Chapters of Mystery Novel in Sunroom");
                t6.setDescription("Cozy reading time with warm chamomile tea.");
                t6.setCategory(Category.LEISURE);
                t6.setPriority(Priority.LOW);
                t6.setDueDate(LocalDate.now());
                t6.setDueTime(LocalTime.of(19, 0));
                t6.setReminderEnabled(false);
                t6.setReminderMinutesBefore(0);
                t6.setCompleted(false);
                t6.setRecurringPattern("NONE");
                t6.setNotes("Novel: The Thursday Murder Club.");
                taskRepository.save(t6);

                System.out.println(">>> Demo data for Eleanor Vance initialized successfully.");
            }
        };
    }
}
