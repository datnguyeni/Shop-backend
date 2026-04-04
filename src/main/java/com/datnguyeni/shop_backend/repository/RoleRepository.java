package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

   Optional<Role> findByRoleName(String name);


}
