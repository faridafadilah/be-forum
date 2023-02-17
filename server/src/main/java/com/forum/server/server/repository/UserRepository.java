package com.forum.server.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forum.server.server.models.User;
import com.forum.server.server.payload.response.DtoUserRole;
import com.forum.server.server.payload.response.ListUserResponse;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByUsername(String username);
  Boolean existsByUsername(String username); 
  Boolean existsByEmail(String email);

  @Query(value = "SELECT u.username as usernameUser, r.name as roleUser FROM users u INNER JOIN user_roles ur ON ur.user_id = u.id INNER JOIN roles r ON r.id = ur.role_id " +
    "WHERE u.username = :username AND r.name = 'ROLE_USER'", nativeQuery = true)
  List<ListUserResponse> findByUsernameAndRole(@Param("username") String username);

  @Query(value = "SELECT u.username as usernameUser, r.name as roleUser FROM users u INNER JOIN user_roles ur ON ur.user_id = u.id INNER JOIN roles r ON r.id = ur.role_id " +
  "WHERE u.username = :username AND r.name = 'ROLE_ADMIN'", nativeQuery = true)
  List<ListUserResponse> findByUserAndRoleAdmin(@Param("username") String username);

  @Query(value = "SELECT u.username as usernameUser, r.name as roleUser FROM users u INNER JOIN user_roles ur ON ur.user_id = u.id INNER JOIN roles r ON r.id = ur.role_id " +
  "WHERE u.username = :username AND r.name = 'ROLE_SUPER_ADMIN'", nativeQuery = true)
  List<ListUserResponse> findByUserAndRoleSuper(@Param("username") String username);

  @Query(value = "SELECT a.username as usernameUser, a.id as idUser, b.role_id as roleIdUser, c.name as roleUser FROM users a "+
    "INNER JOIN user_roles b ON a.id = b.user_id INNER JOIN roles c ON b.role_id = c.id ORDER BY a.id", nativeQuery = true) 
  List<DtoUserRole> getAllUserRole();
}