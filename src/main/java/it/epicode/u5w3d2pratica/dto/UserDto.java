package it.epicode.u5w3d2pratica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "Il nome non può essere vuoto")
    private String nome;
    @NotEmpty(message = "Il cognome non può essere vuoto")
    private String cognome;
    @Email(message = " Inserisci un indirizzo email valido es: 'indirizzo@gmail.com '")
    private String email;
    @NotEmpty(message = "La password non può essere vuota")
    private String password;
}
