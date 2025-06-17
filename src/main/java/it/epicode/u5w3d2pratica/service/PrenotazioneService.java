package it.epicode.u5w3d2pratica.service;

import it.epicode.u5w3d2pratica.dto.PrenotazioneDto;
import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.exception.ValidationException;
import it.epicode.u5w3d2pratica.model.Dipendente;
import it.epicode.u5w3d2pratica.model.Prenotazione;
import it.epicode.u5w3d2pratica.model.Viaggio;
import it.epicode.u5w3d2pratica.repository.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public Prenotazione savePrenotazione(PrenotazioneDto prenotazioneDto) throws NotFoundException, ValidationException {
        Dipendente dipendente = dipendenteService.getDipendente(prenotazioneDto.getDipendenteId());
        Viaggio viaggio = viaggioService.getViaggio(prenotazioneDto.getViaggioId());

        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository
                .findByDipendenteIdAndDataRichiesta(dipendente.getId(), prenotazioneDto.getDataRichiesta());

        if (!prenotazioniEsistenti.isEmpty()) {
            throw new ValidationException("Il dipendente ha già una prenotazione per questa data");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDataRichiesta(prenotazioneDto.getDataRichiesta());
        prenotazione.setNotePreferenze(prenotazioneDto.getNotePreferenze());
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);

        sendMail("girzzo@gmail.com", dipendente);

        return prenotazioneRepository.save(prenotazione);
    }

    public Prenotazione getPrenotazione(long id) throws NotFoundException {
        return prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non trovata"));
    }

    public Page<Prenotazione> getAllPrenotazioni(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione updatePrenotazione(Long id, PrenotazioneDto prenotazioneDto) throws NotFoundException {
        Prenotazione prenotazioneDaAggiornare = getPrenotazione(id);

        prenotazioneDaAggiornare.setDataRichiesta(prenotazioneDto.getDataRichiesta());
        prenotazioneDaAggiornare.setNotePreferenze(prenotazioneDto.getNotePreferenze());

        if (!prenotazioneDaAggiornare.getDipendente().getId().equals(prenotazioneDto.getDipendenteId())) {
            Dipendente nuovoDipendente = dipendenteService.getDipendente(prenotazioneDto.getDipendenteId());
            prenotazioneDaAggiornare.setDipendente(nuovoDipendente);

        }

        if (!prenotazioneDaAggiornare.getViaggio().getId().equals(prenotazioneDto.getViaggioId())){
            Viaggio nuovoViaggio = viaggioService.getViaggio(prenotazioneDto.getViaggioId());
            prenotazioneDaAggiornare.setViaggio(nuovoViaggio);
        }

        return prenotazioneRepository.save(prenotazioneDaAggiornare);
    }

    private void sendMail(String email, Dipendente dipendente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Prenotazione registrata");
        message.setText("Ciao " + dipendente.getNome() +
                "! \nLa registrazione della tua prenotazione è avvenuta con successo");

        javaMailSender.send(message);
    }

    public void deletePrenotazione(Long id) throws NotFoundException {
        Prenotazione prenotazioneDaRimuovere = getPrenotazione(id);
        prenotazioneRepository.delete(prenotazioneDaRimuovere);
    }
}
