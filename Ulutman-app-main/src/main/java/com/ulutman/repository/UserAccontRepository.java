package com.ulutman.repository;


import com.ulutman.model.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccontRepository extends JpaRepository<UserAccount, Long> {

}
