package com.user.userservice.repository;


import com.user.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    String getEmailById(@Param("id") Long id);

    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String getUsernameById(@Param("id") Long id);







}
