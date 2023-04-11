package com.sang.nv.education.iam.infrastructure.persistence.repository;


import com.sang.nv.education.iaminfrastructure.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, String> {

    @Query("from UserRoleEntity u where u.deleted = false and u.userId = :userId")
    List<UserRoleEntity> findAllByUserId(@Param("userId") String userId);

    @Query("from UserRoleEntity ur where ur.deleted = false and ur.userId in :userIds")
    List<UserRoleEntity> findAllByUserIds(@Param("userIds") List<String> userIds);

    @Query("from UserRoleEntity ur where ur.deleted = false and ur.roleId in :roleIds")
    List<UserRoleEntity> findAllByRoleIds(List<String> roleIds);
}
