package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteDao extends JpaRepository<Cliente, String> {

    List<Cliente> findByRagioneSociale(String ragioneSociale);
    List<Cliente> findByNominativo(String nominativo);
    List<Cliente> findByAttivita(String attivita);
    List<Cliente> findBySegnalatore(String segnalatore);
    List<Cliente> findByInteressatoA(String interessatoA);

    //seems it doesnt work
    @Query(value="UPDATE clienti SET registratoda =:newid WHERE registratoda =:oldid", nativeQuery = true)
    void updateRegistratoDa(@Param("oldid")String oldId, @Param("newid") String newId);

    List<Cliente> findByRegistratoDa(String registratoDa);


}

