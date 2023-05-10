package com.sang.nv.education.iam.infrastructure.persistence.repository;


import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.repository.custom.UserEntityRepositoryCustom;
import com.sang.nv.education.iam.infrastructure.persistence.repository.readmodel.StatisticUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String>, UserEntityRepositoryCustom {
    @Query("from UserEntity u where u.deleted = false and lower(u.username) = lower(:username)")
    List<UserEntity> findAllByUsername(@Param("username") String username);

    default Optional<UserEntity> findByUsername(String username) {
        return findAllByUsername(username).stream().findFirst();
    }

    @Query("from UserEntity u where u.deleted = false and u.code in :codes")
    List<UserEntity> findAllByCodes(@Param("codes") List<String> codes);

    @Query("from UserEntity u where u.deleted = false and u.phoneNumber = :phoneNumber")
    List<UserEntity> findByAllPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("from UserEntity u where u.deleted = false and u.email = :email")
    Optional<UserEntity> findByAllEmail(@Param("email") String email);


    default Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        return findByAllPhoneNumber(phoneNumber).stream().findFirst();
    }

    @Query("from UserEntity u where u.deleted = false and lower(u.email) = lower(:email)")
    Optional<UserEntity> findByEmail(@Param("email") String email);


    @Query("from UserEntity u where u.deleted = false and u.id in :ids")
    List<UserEntity> findByIds(List<String> ids);

    @Query("from UserEntity u where u.deleted = false and u.classId in :classIds")
    List<UserEntity> findAllByClassIds(List<String> classIds);


    @Query("select new com.sang.nv.education.iam.infrastructure.persistence.repository.readmodel.StatisticUser(u.userType, MONTH(u.createdAt), COUNT(*))" +
            " from   UserEntity u where u.deleted = false and YEAR(u.createdAt) = :year group by u.userType, MONTH(u.createdAt) ")
    List<StatisticUser> statisticsUser(@Param("year") Integer year);


}
