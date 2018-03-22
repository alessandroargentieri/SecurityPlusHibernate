package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Pratica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PraticaDao extends JpaRepository<Pratica, String> {
}
