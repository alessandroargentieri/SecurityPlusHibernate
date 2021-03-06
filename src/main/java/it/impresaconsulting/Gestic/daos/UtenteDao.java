package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * interfaccia autoimplementata da Spring che realizza le query per la tabella utenti
 */
public interface UtenteDao extends JpaRepository<Utente, String> {

    /* specificate solo le query custom oltre a quelle gia' implementate da JpaRepository */
   // @Query(value="UPDATE utenti SET password =:password WHERE codicefiscale =:codicefiscale", nativeQuery = true)
   // Optional<Utente> updatePassword(@Param("codicefiscale") String codiceFiscale, @Param("password") String password);
}
