package it.epicode.u5w3d2pratica.repository;

import it.epicode.u5w3d2pratica.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ViaggioRepository extends JpaRepository<Viaggio, Long>,
        PagingAndSortingRepository<Viaggio, Long> {
}
