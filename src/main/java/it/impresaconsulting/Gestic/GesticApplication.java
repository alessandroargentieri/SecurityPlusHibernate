package it.impresaconsulting.Gestic;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import it.impresaconsulting.Gestic.entities.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class GesticApplication {

	@Autowired UtenteDao utenteDao; 			//TODO: da eliminare
	@Autowired ClienteDao clienteDao;			//TODO: da eliminare
	@Autowired PraticaDao praticaDao;			//TODO: da eliminare
	@Autowired DocumentoDao documentoDao;		//TODO: da eliminare

	public static void main(String[] args) {
		SpringApplication.run(GesticApplication.class, args);
	}

	//TODO rimuovere perche' usato per riempire il database a titolo di esempio
	@PostConstruct
	private void fillDataBaseExample(){
		//******
		utenteDao.save(new Utente("RGNLSN87H13D761R", "Alessandro Argentieri", "VIA RIVIERA 1 27100 PAVIA (PV)", new Date("13/06/1987"), "ROLE_ADMIN", "kungfu"));
		utenteDao.save(new Utente("RSSDBR87S67C741M", "Debora Rossini", "VIA RIVIERA 1 27100 PAVIA (PV)", new Date("27/11/1987"), "ROLE_USER", "musica"));
		//******
		clienteDao.save(new Cliente("IDCLIENTE_1", "RAGIONESOCIALE_1", "NOMINATIVO_1", "INDIRIZZO_1", "TELEFONO_1", "EMAIL_1", "SITO_1", "SEGNALATORE_1", "ATTIVITA_1", "INTERESSATOA_1", null, "RGNLSN87H13D761R", null, "NOTE_1"));
		clienteDao.save(new Cliente("IDCLIENTE_2", "RAGIONESOCIALE_2", "NOMINATIVO_2", "INDIRIZZO_2", "TELEFONO_2", "EMAIL_2", "SITO_2", "SEGNALATORE_2", "ATTIVITA_2", "INTERESSATOA_2", null, "RGNLSN87H13D761R", null, "NOTE_2"));
		clienteDao.save(new Cliente("IDCLIENTE_3", "RAGIONESOCIALE_3", "NOMINATIVO_3", "INDIRIZZO_3", "TELEFONO_3", "EMAIL_3", "SITO_3", "SEGNALATORE_3", "ATTIVITA_3", "INTERESSATOA_3", null, "RGNLSN87H13D761R", null, "NOTE_3"));
		//******
		praticaDao.save(new Pratica("IDPRATICA_1", "DESCRIZIONE_1", null, "NOTE_1", null, clienteDao.getOne("IDCLIENTE_1"), "RGNLSN87H13D761R"));
		praticaDao.save(new Pratica("IDPRATICA_2", "DESCRIZIONE_2", null, "NOTE_2", null, clienteDao.getOne("IDCLIENTE_1"), "RGNLSN87H13D761R"));

		praticaDao.save(new Pratica("IDPRATICA_3", "DESCRIZIONE_3", null, "NOTE_3", null, clienteDao.getOne("IDCLIENTE_2"), "RGNLSN87H13D761R"));
		praticaDao.save(new Pratica("IDPRATICA_4", "DESCRIZIONE_4", null, "NOTE_4", null, clienteDao.getOne("IDCLIENTE_2"), "RGNLSN87H13D761R"));

		praticaDao.save(new Pratica("IDPRATICA_5", "DESCRIZIONE_5", null, "NOTE_5", null, clienteDao.getOne("IDCLIENTE_3"), "RGNLSN87H13D761R"));
		praticaDao.save(new Pratica("IDPRATICA_6", "DESCRIZIONE_6", null, "NOTE_6", null, clienteDao.getOne("IDCLIENTE_3"), "RGNLSN87H13D761R"));
		//******
		documentoDao.save(new Documento(null, "DESCRIZIONE_1", "PATH_1", "NOTE_1", praticaDao.getOne("IDPRATICA_1"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_2", "PATH_2", "NOTE_2", praticaDao.getOne("IDPRATICA_1"), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_3", "PATH_3", "NOTE_3", praticaDao.getOne("IDPRATICA_2"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_4", "PATH_4", "NOTE_4", praticaDao.getOne("IDPRATICA_2"), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_5", "PATH_5", "NOTE_5", praticaDao.getOne("IDPRATICA_3"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_6", "PATH_6", "NOTE_6", praticaDao.getOne("IDPRATICA_3"), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_7", "PATH_7", "NOTE_7", praticaDao.getOne("IDPRATICA_4"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_8", "PATH_8", "NOTE_8", praticaDao.getOne("IDPRATICA_4"), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_9", "PATH_9", "NOTE_9", praticaDao.getOne("IDPRATICA_5"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_10", "PATH_10", "NOTE_10", praticaDao.getOne("IDPRATICA_5"), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "DESCRIZIONE_11", "PATH_11", "NOTE_11", praticaDao.getOne("IDPRATICA_6"), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "DESCRIZIONE_12", "PATH_12", "NOTE_12", praticaDao.getOne("IDPRATICA_6"), "RGNLSN87H13D761R", null));

	}
}
