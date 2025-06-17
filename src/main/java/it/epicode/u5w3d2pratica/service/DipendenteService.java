package it.epicode.u5w3d2pratica.service;

import com.cloudinary.Cloudinary;
import it.epicode.u5w3d2pratica.dto.DipendenteDto;
import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.model.Dipendente;
import it.epicode.u5w3d2pratica.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public Dipendente saveDipendente(DipendenteDto dipendenteDto){
        Dipendente dipendente = new Dipendente();
        dipendente.setNome(dipendenteDto.getNome());
        dipendente.setCognome(dipendenteDto.getCognome());
        dipendente.setEmail(dipendenteDto.getEmail());
        dipendente.setUsername(dipendenteDto.getUsername());

        sendMail("girzzo@gmail.com", dipendente);

        return dipendenteRepository.save(dipendente);
    }

    public Dipendente getDipendente(Long id) throws NotFoundException {
        return dipendenteRepository.findById(id).orElseThrow(()->
                new NotFoundException("Nessun dipendente trovato con id " + id));
    }

    public Page<Dipendente> getAllDipendenti(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return dipendenteRepository.findAll(pageable);
    }

    public Dipendente updateDipendente(Long id, DipendenteDto dipendenteDto) throws NotFoundException {
        Dipendente dipendenteDaAggiornare = getDipendente(id);

        dipendenteDaAggiornare.setNome(dipendenteDto.getNome());
        dipendenteDaAggiornare.setCognome(dipendenteDto.getCognome());
        dipendenteDaAggiornare.setUsername(dipendenteDto.getUsername());
        dipendenteDaAggiornare.setEmail(dipendenteDto.getEmail());

        return dipendenteRepository.save(dipendenteDaAggiornare);
    }

    public String patchDipendente(Long id, MultipartFile file) throws NotFoundException, IOException {
        Dipendente dipendenteDaPatchare = getDipendente(id);

        String url = (String)cloudinary.uploader().upload(file.getBytes(),
                Collections.emptyMap()).get("url");

        dipendenteDaPatchare.setImmagineProfilo(url);

        dipendenteRepository.save(dipendenteDaPatchare);

        return url;
    }

    private void sendMail(String email, Dipendente dipendente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione Dipendente");
        message.setText("Benvenut* " + dipendente.getNome() +", la tua registrazione è avvenuta con successo!");

        javaMailSender.send(message);
    }

    public void deleteDipendente(Long id) throws NotFoundException {
        Dipendente dipendenteDaEliminare = getDipendente(id);

        dipendenteRepository.delete(dipendenteDaEliminare);
    }

}
