package com.nals.tf7.repository;

import com.nals.tf7.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
    extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    @Query("SELECT new User(u.id, u.name, u.email, u.phone, u.address, u.gender,"
        + "                 u.dob, u.avatar, u.role)"
        + " FROM User u"
        + " WHERE u.id = :id AND u.activated = TRUE")
    User getBasicInfoById(@Param("id") Long id);
}
