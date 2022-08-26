package com.westerhoud.osrs.taskman.model;

import com.westerhoud.osrs.taskman.dto.site.AccountTaskDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AccountTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private Date startTime;
    private Date endTime;

    public AccountTaskDto toDto() {
        return AccountTaskDto.builder()
                .account(account.toDto())
                .task(task.toDto())
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
