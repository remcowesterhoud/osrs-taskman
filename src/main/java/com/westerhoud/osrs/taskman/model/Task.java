package com.westerhoud.osrs.taskman.model;

import com.westerhoud.osrs.taskman.dto.TaskDto;
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
    private String type;
    private String category;

    @OneToMany(mappedBy = "task")
    private List<AccountTask> accountTasks;

    public TaskDto toDto() {
        return TaskDto.builder()
                .id(id)
                .name(name)
                .tier(tier)
                .info(info)
                .build();
    }
}
