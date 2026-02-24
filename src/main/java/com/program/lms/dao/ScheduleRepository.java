package com.program.lms.dao;

import com.program.lms.model.ScheduleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    @EntityGraph(attributePaths = {"group", "course", "teacher"})
    List<ScheduleEntity> findAll();

    @EntityGraph(attributePaths = {"group", "course", "teacher"})
    List<ScheduleEntity> findByGroupId(Long groupId);

    @EntityGraph(attributePaths = {"group", "course", "teacher"})
    List<ScheduleEntity> findByTeacherId(Long teacherId);

    @Query(value = """
            select exists(
                select 1
                from lessons s
                where s.group_id = :groupId
                  and s.date_time < :newEnd
                  and s.date_time + make_interval(mins => (:duration)::int) > :newStart
            )
            """, nativeQuery = true)
    boolean groupHasTimeConflict(@Param("groupId") Long groupId,
                                 @Param("newStart") LocalDateTime newStart,
                                 @Param("newEnd") LocalDateTime newEnd,
                                 @Param("duration") long duration);

    @Query(value = """
            select exists(
                select 1
                from lessons s
                where s.teacher_id = :teacherId
                  and s.date_time < :newEnd
                  and s.date_time + make_interval(mins => (:duration)::int) > :newStart
            )
            """, nativeQuery = true)
    boolean teacherHasTimeConflict(@Param("teacherId") Long teacherId,
                                   @Param("newStart") LocalDateTime newStart,
                                   @Param("newEnd") LocalDateTime newEnd,
                                   @Param("duration") long duration);

    @Query(value = """
            select exists(
                select 1
                from lessons s
                where s.group_id = :groupId
                  and s.id <> :lessonId
                  and s.date_time < :newEnd
                  and s.date_time + make_interval(mins => (:duration)::int) > :newStart
            )
            """, nativeQuery = true)
    boolean groupHasTimeConflictForUpdate(@Param("lessonId") Long lessonId,
                                          @Param("groupId") Long groupId,
                                          @Param("newStart") LocalDateTime newStart,
                                          @Param("newEnd") LocalDateTime newEnd,
                                          @Param("duration") long duration);

    @Query(value = """
            select exists(
                select 1
                from lessons s
                where s.teacher_id = :teacherId
                  and s.id <> :lessonId
                  and s.date_time < :newEnd
                  and s.date_time + make_interval(mins => (:duration)::int) > :newStart
            )
            """, nativeQuery = true)
    boolean teacherHasTimeConflictForUpdate(@Param("lessonId") Long lessonId,
                                            @Param("teacherId") Long teacherId,
                                            @Param("newStart") LocalDateTime newStart,
                                            @Param("newEnd") LocalDateTime newEnd,
                                            @Param("duration") long duration);
}