package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Scadenza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScadenzaDao extends JpaRepository<Scadenza, Integer>{
    List<Scadenza> findByRegistratoDa(String registratoDa);
}
