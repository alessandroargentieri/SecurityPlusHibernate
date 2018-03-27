package it.impresaconsulting.Gestic;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import it.impresaconsulting.Gestic.entities.Utente;
import it.impresaconsulting.Gestic.utilities.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
public class GesticApplication {

	@Autowired UtenteDao utenteDao; 			//TODO: da eliminare
	@Autowired ClienteDao clienteDao;			//TODO: da eliminare
	@Autowired PraticaDao praticaDao;			//TODO: da eliminare
	@Autowired DocumentoDao documentoDao;		//TODO: da eliminare

	@Autowired EncryptionUtils encryptionUtils;

	/* fa partire tomcat embedded e tutto l'applicativo basato su Spring: i bean vengono istanziati */
	public static void main(String[] args) {
		SpringApplication.run(GesticApplication.class, args);
	}

	//TODO rimuovere perche' usato per riempire il database a titolo di esempio
	@PostConstruct
	private void fillDataBaseExample(){

		//******

		String encryptedPwd = encryptionUtils.encrypt("kungfu");
		utenteDao.save(new Utente("RGNLSN87H13D761R", "Alessandro Argentieri", "VIA RIVIERA 1 27100 PAVIA (PV)", "435324367", "alessandro.argentieri@gestic.it", new Date("13/06/1987"), "ROLE_ADMIN", encryptedPwd));
		encryptedPwd = encryptionUtils.encrypt("musica");
		utenteDao.save(new Utente("RSSDBR87S67C741M", "Debora Rossini", "VIA RIVIERA 1 27100 PAVIA (PV)", "423236543", "debora.rossini@gestic.it", new Date("27/11/1987"), "ROLE_USER", encryptedPwd));

		//******

		Cliente cliente1 = new Cliente("IDCLIENTE_1", "RAGIONESOCIALE_1", "NOMINATIVO_1", "INDIRIZZO_1", "TELEFONO_1", "EMAIL_1", "SITO_1", "SEGNALATORE_1", "ATTIVITA_1", "INTERESSATOA_1",  "RGNLSN87H13D761R", null, "NOTE_1");
		Cliente cliente2 = new Cliente("IDCLIENTE_2", "RAGIONESOCIALE_2", "NOMINATIVO_2", "INDIRIZZO_2", "TELEFONO_2", "EMAIL_2", "SITO_2", "SEGNALATORE_2", "ATTIVITA_2", "INTERESSATOA_2", "RGNLSN87H13D761R", null, "NOTE_2");
		Cliente cliente3 = new Cliente("IDCLIENTE_3", "RAGIONESOCIALE_3", "NOMINATIVO_3", "INDIRIZZO_3", "TELEFONO_3", "EMAIL_3", "SITO_3", "SEGNALATORE_3", "ATTIVITA_3", "INTERESSATOA_3", "RGNLSN87H13D761R", null, "NOTE_3");

		clienteDao.save(cliente1);
		clienteDao.save(cliente2);
		clienteDao.save(cliente3);

		//******

		Pratica p1 = new Pratica(null, "DESCRIZIONE_1", "NOTE_1", null, "IDCLIENTE_1", "RGNLSN87H13D761R");
		Pratica p2 = new Pratica(null, "DESCRIZIONE_2", "NOTE_2", null, "IDCLIENTE_1", "RGNLSN87H13D761R");
		Pratica p3 = new Pratica(null, "DESCRIZIONE_3", "NOTE_3", null, "IDCLIENTE_2", "RGNLSN87H13D761R");
		Pratica p4 = new Pratica(null, "DESCRIZIONE_4", "NOTE_4", null, "IDCLIENTE_2", "RGNLSN87H13D761R");
		Pratica p5 = new Pratica(null, "DESCRIZIONE_5", "NOTE_5", null, "IDCLIENTE_3", "RGNLSN87H13D761R");
		Pratica p6 = new Pratica(null, "DESCRIZIONE_6", "NOTE_6", null,"IDCLIENTE_3", "RGNLSN87H13D761R");

		praticaDao.save(p1);
		praticaDao.save(p2);
		praticaDao.save(p3);
		praticaDao.save(p4);
		praticaDao.save(p5);
		praticaDao.save(p6);

		//******

		documentoDao.save(new Documento(null, "DESCRIZIONE_1", "PATH_1", "NOTE_1", p1.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_2", "PATH_2", "NOTE_2", p1.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_3", "PATH_3", "NOTE_3", p2.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_4", "PATH_4", "NOTE_4", p2.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_5", "PATH_5", "NOTE_5", p3.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_6", "PATH_6", "NOTE_6", p3.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_7", "PATH_7", "NOTE_7", p4.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_8", "PATH_8", "NOTE_8", p4.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_9", "PATH_9", "NOTE_9", p5.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_10", "PATH_10", "NOTE_10", p5.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_11", "PATH_11", "NOTE_11", p6.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_12", "PATH_12", "NOTE_12", p6.getIdPratica(), "RGNLSN87H13D761R", null));
	}
}


/*
* form per la registrazione di un nuovo utente
* form per il cambio della password dell'utente
*
* form per salvare/modificare un cliente
* form per salvare/modificare una pratica
* form per salvare/modificare un documento
*
* lista di utenti    (x)
* lista di clienti   (x)
* lista di pratiche  (x)
* lista di documenti (x)
* (per eliminare devi essere un amministratore e l'eliminazione viene inserita nei log)
*
* form per inserire una nuova scadenza
* lista di tutte le scandenze non scadute
* (le scadenze scadute si autoeliminano)
* */