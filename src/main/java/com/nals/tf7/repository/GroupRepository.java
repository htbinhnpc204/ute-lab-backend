package com.nals.tf7.repository;

import com.nals.tf7.domain.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository
    extends JpaRepository<Lab, Long> {

//    boolean existsByName(String name);
//
//    @Query("SELECT c"
//        + " FROM Class c"
//        + " WHERE (:name IS NULL OR c.name LIKE %:name%)"
//        + " ORDER BY c.createdDate DESC")
//    Page<Class> searchByName(Pageable pageable,
//                             @Param("name") String name);
//
//    @Query("SELECT c"
//        + " FROM Class c"
//        + " JOIN GroupUser gu ON c.id = gu.groupId"
//        + " WHERE gu.userId = :userId AND gu.type = :type")
//    Page<Class> searchByUserIdAndType(@Param("userId") Long userId,
//                                      @Param("type") GroupUserType type,
//                                      Pageable pageable);
}
