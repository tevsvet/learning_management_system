package com.program.lms.service;

import com.program.lms.dao.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleCleanupService {

    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "${app.cleanup-cron}")
    @Transactional
    public void cleanupOldSchedule() {

        log.info("schedule-cleanup: started");

        LocalDateTime threshold = LocalDateTime.now().minusYears(1);
        int deleted = scheduleRepository.deleteOlderThan(threshold);

        log.info("schedule-cleanup: finished, deleted={} records", deleted);
    }
}
