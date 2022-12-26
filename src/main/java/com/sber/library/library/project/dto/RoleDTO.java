package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getRoleTitle();
    }
}
