package com.ebibli.domain;

import com.ebibli.dto.LivreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Emprunteur {

    private String nom;
    private String prenom;
    private String email;
    List<LivreDto> emprunts;
}
