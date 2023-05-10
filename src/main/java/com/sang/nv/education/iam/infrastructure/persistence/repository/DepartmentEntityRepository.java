package com.sang.nv.education.iam.infrastructure.persistence.repository;


import com.sang.commonpersistence.repository.custom.BaseRepositoryCustom;
import com.sang.nv.education.iam.infrastructure.persistence.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, String>, BaseRepositoryCustom<DepartmentEntity> {
    @Query("from DepartmentEntity e where e.deleted = false and e.id = :departmentId ")
    Optional<DepartmentEntity> findById(@Param("departmentId") String departmentId);

    @Query("from DepartmentEntity e where e.deleted = false and e.id in :departmentIds ")
    List<DepartmentEntity> findByIds(@Param("departmentIds") List<String> departmentIds);

    @Query("from DepartmentEntity u where u.deleted = false and "
            + " (:keyword is null or ( u.name like :keyword or"
            + " u.code like :keyword))")
    Page<DepartmentEntity> autoComplete(
            @Param("keyword") String keyword, Pageable pageable);

}
