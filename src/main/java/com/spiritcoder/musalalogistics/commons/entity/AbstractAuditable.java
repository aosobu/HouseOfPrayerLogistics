package com.spiritcoder.musalalogistics.commons.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractAuditable {

    public AbstractAuditable(LocalDateTime created, LocalDateTime updated, String creator, String updater) {
        this.created = created;
        this.updated = updated;
        this.creator = creator;
        this.updater = updater;
    }

    @Column(updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updated;

    @Column
    private String creator;

    @Column
    private String updater;
}
