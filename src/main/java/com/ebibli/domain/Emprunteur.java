package com.ebibli.domain;

import com.ebibli.dto.EmpruntDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    List<EmpruntDto> emprunts = new ArrayList<>();
    List<EmpruntDto> empruntsRetard = new ArrayList<>();

    public void addEmprunt(EmpruntDto emprunt) {
        emprunts.add(emprunt);
    }
    public void addEmpruntRetard(EmpruntDto emprunt) {
        empruntsRetard.add(emprunt);
    }
}
