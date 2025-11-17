package com.dpatrones.proyecto.repository;

import com.dpatrones.proyecto.domain.model.UserRole;
import com.dpatrones.proyecto.domain.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId")
    List<UserRole> findByRoleId(@Param("roleId") Long roleId);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

}
