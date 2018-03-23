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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

		List<Documento> documenti = new ArrayList<>();
		List<Pratica> pratiche = new ArrayList<>();

	    //--------------------------
		Cliente c1 = new Cliente("IDCLIENTE_1", "RAGIONESOCIALE_1", "NOMINATIVO_1", "INDIRIZZO_1", "TELEFONO_1", "EMAIL_1", "SITO_1", "SEGNALATORE_1", "ATTIVITA_1", "INTERESSATOA_1", null, "RGNLSN87H13D761R", null, "NOTE_1");
		  Pratica p1 = new Pratica(null, "DESCRIZIONE_1", null, "NOTE_1", null, c1, "RGNLSN87H13D761R");
		    Documento d1 = new Documento(null, "DESCRIZIONE_1", "PATH_1", "NOTE_1", p1, "RGNLSN87H13D761R", null);
		    Documento d2 = new Documento(null, "DESCRIZIONE_2", "PATH_2", "NOTE_2", p1, "RGNLSN87H13D761R", null);
		   documenti.clear();
		   documenti.add(d1);
		   documenti.add(d2);
		  p1.setDocumenti(documenti);
		  Pratica p2 = new Pratica(null, "DESCRIZIONE_2", null, "NOTE_2", null, c1, "RGNLSN87H13D761R");
		    Documento d3 = new Documento(null, "DESCRIZIONE_3", "PATH_3", "NOTE_3", p2, "RGNLSN87H13D761R", null);
		    Documento d4 = new Documento(null, "DESCRIZIONE_4", "PATH_4", "NOTE_4", p2, "RGNLSN87H13D761R", null);
		   documenti.clear();
		   documenti.add(d3);
		   documenti.add(d4);
		  p2.setDocumenti(documenti);
		  pratiche.clear();
		  pratiche.add(p1);
		  pratiche.add(p2);
		c1.setPratiche(pratiche);

		clienteDao.save(c1);
		praticaDao.save(p1);
		praticaDao.save(p2);
		documentoDao.save(d1);
		documentoDao.save(d2);
		documentoDao.save(d3);
		documentoDao.save(d4);

		//********

		Cliente c2 = new Cliente("IDCLIENTE_2", "RAGIONESOCIALE_2", "NOMINATIVO_2", "INDIRIZZO_2", "TELEFONO_2", "EMAIL_2", "SITO_2", "SEGNALATORE_2", "ATTIVITA_2", "INTERESSATOA_2", null, "RGNLSN87H13D761R", null, "NOTE_2");
		Pratica p3 = new Pratica(null, "DESCRIZIONE_3", null, "NOTE_3", null, c2, "RGNLSN87H13D761R");
		Documento d5 = new Documento(null, "DESCRIZIONE_5", "PATH_5", "NOTE_5", p3, "RGNLSN87H13D761R", null);
		Documento d6 = new Documento(null, "DESCRIZIONE_6", "PATH_6", "NOTE_6", p3, "RGNLSN87H13D761R", null);
		documenti.clear();
		documenti.add(d5);
		documenti.add(d6);
		p3.setDocumenti(documenti);
		Pratica p4 = new Pratica(null, "DESCRIZIONE_4", null, "NOTE_5", null, c2, "RGNLSN87H13D761R");
		Documento d7 = new Documento(null, "DESCRIZIONE_7", "PATH_7", "NOTE_7", p4, "RGNLSN87H13D761R", null);
		Documento d8 = new Documento(null, "DESCRIZIONE_6", "PATH_6", "NOTE_6", p4, "RGNLSN87H13D761R", null);
		documenti.clear();
		documenti.add(d7);
		documenti.add(d8);
		p4.setDocumenti(documenti);

		pratiche.clear();
		pratiche.add(p3);
		pratiche.add(p4);
		c2.setPratiche(pratiche);

		clienteDao.save(c2);
		praticaDao.save(p3);
		praticaDao.save(p4);
		documentoDao.save(d5);
		documentoDao.save(d6);
		documentoDao.save(d7);
		documentoDao.save(d8);

		Pratica p5 = new Pratica(null, "DESCRIZIONE_5X", null, "NOTE_5", null, clienteDao.getOne("IDCLIENTE_2"), "RGNLSN87H13D761R");
		praticaDao.save(p5);
		Documento dx = new Documento(null, "DESCRIZIONE_7X", "PATH_xx", "NOTE_xx", p5, "RGNLSN87H13D761R", null);
		documentoDao.save(dx);

	}
}
