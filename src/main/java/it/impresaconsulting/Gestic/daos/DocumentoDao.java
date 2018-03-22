package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoDao extends JpaRepository<Documento, String> {
}
