package com.psybergate.resoma.time.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"status", "statusReason", "timeStamp"}, callSuper = false)
@Entity(name = "StatusHistory")
public class StatusHistory extends BaseEntity {

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_update_reason")
    private String statusReason;

    @ManyToOne
    @JoinColumn(name = "entry_id", referencedColumnName = "id")
    private TimeEntry timeEntry;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    public StatusHistory() {
    }

    public StatusHistory(TimeEntry timeEntry) {
        this.status = timeEntry.getStatus();
        this.statusReason = timeEntry.getStatusReason();
        this.timeEntry = timeEntry;
        this.timeStamp = LocalDateTime.now();
        super.setId(UUID.randomUUID());
        super.setCreatedDate(LocalDateTime.now());
    }
}
