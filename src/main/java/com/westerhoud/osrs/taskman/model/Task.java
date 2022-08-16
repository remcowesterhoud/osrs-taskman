package com.westerhoud.osrs.taskman.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Tier tier;
    private String info;

    @OneToMany(mappedBy = "task")
    private List<AccountTask> accountTasks;
}
