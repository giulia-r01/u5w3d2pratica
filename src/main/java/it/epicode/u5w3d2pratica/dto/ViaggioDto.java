package it.epicode.u5w3d2pratica.dto;

import it.epicode.u5w3d2pratica.enumeration.StatoViaggio;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ViaggioDto {
    @NotEmpty(message = " Il campo 'destinazione' non può essere vuoto o null")
    private String destinazione;
    @NotNull(message = "La data del viaggio non può essere null")
    private LocalDate dataViaggio;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Lo stato del viaggio non può essere null")
    private StatoViaggio statoViaggio;
}
