package it.epicode.u5w3d2pratica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DipendenteDto {
    @NotEmpty(message = " Il campo 'username' non può essere vuoto o null")
    private String username;
    @NotEmpty(message = " Il campo 'nome' non può essere vuoto o null")
    private String nome;
    @NotEmpty(message = " Il campo 'cognome' non può essere vuoto o null")
    private String cognome;
    @Email(message = " Inserisci un indirizzo email valido es: 'indirizzo@gmail.com '")
    private String email;
}
