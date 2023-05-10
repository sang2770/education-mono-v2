package com.sang.nv.education.iam.infrastructure.persistence.repository;


import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;
import com.sang.nv.education.iam.infrastructure.persistence.repository.custom.ClassesEntityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesEntityRepository extends JpaRepository<ClassEntity, String>, ClassesEntityRepositoryCustom {
    @Query("from ClassEntity e where e.deleted = false and e.departmentId = :departmentId ")
    List<ClassEntity> findByDepartmentId(@Param("departmentId") String departmentId);

    @Query("from ClassEntity e where e.deleted = false and e.departmentId in :departmentIds ")
    List<ClassEntity> findAllByDepartmentIds(@Param("departmentIds") List<String> departmentId);

    @Query("from ClassEntity e where e.deleted = false and e.code in :codes ")
    List<ClassEntity> findAllByCode(@Param("codes") List<String> codes);


    @Query("from ClassEntity e where e.deleted = false")
    List<ClassEntity> findAll();

    @Query("from ClassEntity e where e.deleted = false and (:ids is null or e.id in :ids) and (:departmentIds is null or e.departmentId in :departmentIds) and (:keyIds is null or e.id in :keyIds)")
    List<ClassEntity> findAll(@Param("ids") List<String> ids, @Param("departmentIds") List<String> departmentIds, @Param("keyIds") List<String> keyIds);

    @Query("from ClassEntity u where u.deleted = false and "
            + " (:keyword is null or ( u.name like :keyword or"
            + " u.code like :keyword))")
    Page<ClassEntity> autoComplete(
            @Param("keyword") String keyword, Pageable pageable);


}
