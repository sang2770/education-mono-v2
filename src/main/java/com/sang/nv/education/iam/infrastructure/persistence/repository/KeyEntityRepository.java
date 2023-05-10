package com.sang.nv.education.iam.infrastructure.persistence.repository;


import com.sang.nv.education.iam.infrastructure.persistence.entity.KeyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyEntityRepository extends JpaRepository<KeyEntity, String> {
    @Query("from KeyEntity u where u.deleted = false and ( :keyword is null or ( u.name like :keyword))")
    Page<KeyEntity> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("from KeyEntity e where e.deleted = false and e.id = :keyId ")
    Optional<KeyEntity> findById(@Param("keyId") String keyId);

    @Query("from KeyEntity e where e.deleted = false and e.id in :keyIds ")
    List<KeyEntity> findByIds(@Param("keyIds") List<String> keyIds);
}
