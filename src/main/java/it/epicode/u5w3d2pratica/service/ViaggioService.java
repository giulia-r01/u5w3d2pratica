package it.epicode.u5w3d2pratica.service;

import it.epicode.u5w3d2pratica.dto.ViaggioDto;
import it.epicode.u5w3d2pratica.enumeration.StatoViaggio;
import it.epicode.u5w3d2pratica.exception.InvalidStatoViaggioException;
import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.model.Viaggio;
import it.epicode.u5w3d2pratica.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    public Viaggio saveViaggio(ViaggioDto viaggioDto){
        Viaggio viaggio = new Viaggio();
        viaggio.setDataViaggio(viaggioDto.getDataViaggio());
        viaggio.setStatoViaggio(viaggioDto.getStatoViaggio());
        viaggio.setDestinazione(viaggioDto.getDestinazione());

        return viaggioRepository.save(viaggio);
    }

    public Viaggio getViaggio(Long id) throws NotFoundException {
        return viaggioRepository.findById(id).orElseThrow(()->
                new NotFoundException("Nessun viaggio trovato con id " + id));
    }

    public Page<Viaggio> getAllViaggi(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return viaggioRepository.findAll(pageable);
    }

    public Viaggio updateViaggio(Long id, ViaggioDto viaggioDto) throws NotFoundException {
        Viaggio viaggioDaAggiornare = getViaggio(id);

        viaggioDaAggiornare.setDestinazione(viaggioDto.getDestinazione());
        viaggioDaAggiornare.setDataViaggio(viaggioDto.getDataViaggio());
        viaggioDaAggiornare.setStatoViaggio(viaggioDto.getStatoViaggio());

        return viaggioRepository.save(viaggioDaAggiornare);
    }

    public Viaggio aggiornaStatoViaggio(Long id, String statoStr) throws NotFoundException, InvalidStatoViaggioException {
        Viaggio viaggio = getViaggio(id);

        boolean statoValido = Arrays.stream(StatoViaggio.values())
                .anyMatch(s -> s.name().equalsIgnoreCase(statoStr));

        if (!statoValido) {
            throw new InvalidStatoViaggioException("Stato non valido: " + statoStr +
                    ". Valori ammessi: " + Arrays.toString(StatoViaggio.values()));
        }

        StatoViaggio nuovoStato = StatoViaggio.valueOf(statoStr.toUpperCase());
        viaggio.setStatoViaggio(nuovoStato);
        return viaggioRepository.save(viaggio);
    }

    public void deleteViaggio(Long id) throws NotFoundException {
        Viaggio viaggioDaEliminare = getViaggio(id);

        viaggioRepository.delete(viaggioDaEliminare);
    }
}
