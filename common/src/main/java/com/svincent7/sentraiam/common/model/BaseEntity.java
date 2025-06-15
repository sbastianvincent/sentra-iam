package com.svincent7.sentraiam.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 138172389778892L;

    @Column(name = "created_timestamp", nullable = false)
    private Long createdTimestamp;

    @Column(name = "updated_timestamp", nullable = false)
    private Long updatedTimestamp;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdTimestamp == null) {
            createdTimestamp = System.currentTimeMillis();
        }
        if (this.updatedTimestamp == null) {
            updatedTimestamp = System.currentTimeMillis();
        }
        entityPersist();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTimestamp = System.currentTimeMillis();
        entityUpdate();
    }

    protected void entityPersist() {

    }

    protected void entityUpdate() {

    }
}
