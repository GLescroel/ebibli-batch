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
public class UtilisateurDto {

    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private RoleDto role;
}
