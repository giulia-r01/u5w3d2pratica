package it.epicode.u5w3d2pratica.repository;

import it.epicode.u5w3d2pratica.model.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DipendenteRepository extends JpaRepository<Dipendente, Long>,
        PagingAndSortingRepository<Dipendente, Long> {
}
