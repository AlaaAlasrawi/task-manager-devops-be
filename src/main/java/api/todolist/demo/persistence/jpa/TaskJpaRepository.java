package api.todolist.demo.persistence.jpa;

import api.todolist.demo.persistence.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE task t SET " +
            "t.title = COALESCE(:#{#taskEntity.title}, t.title), " +
            "t.updated_at = COALESCE(:#{#taskEntity.updatedAt}, t.updated_at) " +
            "WHERE t.id = :id", nativeQuery = true)
    void updateTaskById(@Param("id") Long id, @Param("taskEntity") TaskEntity taskEntity);


    Page<TaskEntity> findAllByAppUser_Id(Long appUserId, Specification<TaskEntity> spec, Pageable pageable);

    void deleteAllByAppUser_Id(Long userId);

    void deleteAllByAppUser_IdAndIsCompletedTrue(Long appUserId);

    void deleteAllByIdInAndAppUser_Id(List<Long> ids, Long userId);

    Optional<TaskEntity> findByIdAndAppUser_Id(Long id, Long appUserId);

}
