package it.epicode.u5w3d2pratica.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrenotazioneDto {
    @NotNull(message = "La data di prenotazione non può essere null")
    private LocalDate dataRichiesta;
    private String notePreferenze;
    @NotNull(message = "L'ID del dipendente è obbligatorio, non può essere nullo")
    private Long dipendenteId;
    @NotNull(message = "L'ID del viaggio è obbligatorio, non può essere nullo")
    private Long viaggioId;
}
