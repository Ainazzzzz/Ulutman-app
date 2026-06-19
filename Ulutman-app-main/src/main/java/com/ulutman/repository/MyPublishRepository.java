package com.ulutman.repository;

import com.ulutman.model.entities.MyPublish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPublishRepository extends JpaRepository<MyPublish,Long> {

    @Modifying
    @Query("DELETE FROM MyPublish mp WHERE mp.publish.id IN :publishIds")
    void deleteAllByPublishIds(@Param("publishIds") List<Long> publishIds);
}
