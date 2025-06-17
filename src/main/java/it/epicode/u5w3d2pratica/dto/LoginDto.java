package it.epicode.u5w3d2pratica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
        @Email(message = " Inserisci un indirizzo email valido es: 'indirizzo@gmail.com '")
        private String email;
        @NotEmpty(message = "La password non può essere vuota")
        private String password;
    }

