package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
        SELECT user FROM User user WHERE
        (:names IS NULL OR LOWER(user.name) LIKE LOWER(CONCAT(:names, '%')))
        OR (LOWER(user.name) LIKE LOWER(CONCAT(CONCAT(:names, '%'), '%')))
        """)
    List<User> findByUserName(@Param("names") String names);

    @Query("SELECT u FROM User u WHERE (:roles IS NULL OR u.role IN :roles)")
    List<User> findByRole(@Param("roles") List<Role> role);

    @Query("SELECT u FROM User u WHERE (:createDates IS NULL OR u.createDate IN :createDates)")
    List<User> findByCreateDate(@Param("createDates") List<LocalDate>createDates);

    @Query("SELECT u FROM User u WHERE (:statuses IS NULL OR u.status IN :statuses)")
    List<User> findByStatus(@Param("statuses")List< Status> statuses);

    @Query("SELECT DISTINCT u FROM User u JOIN u.mailings m")
    List<User> findAllUsersWithMailings();

    @Query("select app from Publish app join app.user user where user.id = :id")
    List<Publish> getAllPublishByUserId(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
