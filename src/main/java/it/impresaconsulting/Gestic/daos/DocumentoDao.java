package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DocumentoDao extends JpaRepository<Documento, String> {

    @Query(value="DELETE FROM documenti WHERE fkpratica =:fkpratica", nativeQuery = true)
    void deleteDocumentiPerPratica(@Param("fkpratica") String fkpratica);
}
