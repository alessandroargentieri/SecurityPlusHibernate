package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Pratica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PraticaDao extends JpaRepository<Pratica, String> {
    List<Pratica> findByFkCliente(String fkCliente);

    @Query(value="DELETE FROM pratiche WHERE fkcliente =:fkcliente", nativeQuery = true)
    void deletePraticaPerCliente(@Param("fkcliente") String fkcliente);
}
