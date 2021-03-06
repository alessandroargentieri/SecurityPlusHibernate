package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteDao extends JpaRepository<Cliente, String> {

    List<Cliente> findByRagioneSociale(String ragioneSociale);
    List<Cliente> findByNominativo(String nominativo);
    List<Cliente> findByAttivita(String attivita);
    List<Cliente> findBySegnalatore(String segnalatore);
    List<Cliente> findByInteressatoA(String interessatoA);
    List<Cliente> findByRegistratoDa(String registratoDa);

    //seems it doesnt work
    //@Query(value="UPDATE clienti SET registratoda =:newid WHERE registratoda =:oldid", nativeQuery = true)
    //void updateRegistratoDa(@Param("oldid")String oldId, @Param("newid") String newId);




}

