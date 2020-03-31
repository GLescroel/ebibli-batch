package com.ebibli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDto {

    private Integer id;
    private String role;

    public RoleDto roleAbonne() {
        return new RoleDto().builder().id(2).role("Abonne").build();
    }
}
