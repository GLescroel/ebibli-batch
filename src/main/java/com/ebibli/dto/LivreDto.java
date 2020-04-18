package com.ebibli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivreDto {

    private Integer id;
    private OuvrageDto ouvrage;
    private BibliothequeDto bibliotheque;
    private Boolean disponible;
}
