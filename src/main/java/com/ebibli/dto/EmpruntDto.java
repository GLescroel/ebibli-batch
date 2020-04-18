package com.ebibli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntDto {

    private Integer id;
    private LivreDto livre;
    private UtilisateurDto emprunteur;
    private Date dateEmprunt;
    private Date dateRetourPrevu;
    private Date dateRetour;
    private Boolean prolonge;
    private Boolean encours;
    private Boolean enRetard;

}
