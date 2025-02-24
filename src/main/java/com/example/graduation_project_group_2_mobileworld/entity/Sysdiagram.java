package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sysdiagrams")
public class Sysdiagram {
    @Id
    @Column(name = "diagram_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "principal_id", nullable = false)
    private Integer principalId;

    @Column(name = "version")
    private Integer version;
    @Column(name = "definition")
    private byte[] definition;

/*
    TODO [JPA Buddy] create field to map the 'name' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "name", columnDefinition = "sysname(0, 0) not null")
    private Object name;
*/
}