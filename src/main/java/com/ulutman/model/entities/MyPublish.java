package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "myPublishes")
@Getter
@Setter
public class MyPublish {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publish_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mypublish_publish", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (publish_id) REFERENCES publishes(id) ON DELETE CASCADE"))
    private Publish publish;
}
