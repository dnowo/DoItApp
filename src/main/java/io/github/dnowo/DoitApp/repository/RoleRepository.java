package io.github.dnowo.DoitApp.repository;

import io.github.dnowo.DoitApp.model.ERole;
import io.github.dnowo.DoitApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
