package com.nals.tf7.repository;

import com.nals.tf7.domain.Group;
import com.nals.tf7.enums.GroupUserType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository
    extends JpaRepository<Group, Long> {

    boolean existsByName(String name);

    @Query("SELECT g"
        + " FROM Group g"
        + " WHERE (:name IS NULL OR g.name LIKE %:name%)"
        + " ORDER BY g.createdDate DESC")
    Page<Group> searchByName(Pageable pageable,
                             @Param("name") String name);

    @Query("SELECT g"
        + " FROM Group g"
        + " JOIN GroupUser gu ON g.id = gu.groupId"
        + " WHERE gu.userId = :userId AND gu.type = :type")
    Page<Group> searchByUserIdAndType(@Param("userId") Long userId,
                                      @Param("type") GroupUserType type,
                                      Pageable pageable);
}
