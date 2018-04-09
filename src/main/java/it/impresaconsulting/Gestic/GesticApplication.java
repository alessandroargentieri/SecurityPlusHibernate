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
import org.springframework.context.annotation.Bean;

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
		utenteDao.save(new Utente("RGNLSN87H13D761R", "Alessandro Argentieri", "VIA RIVIERA 1 27100 PAVIA (PV)", "435324367", "alessandro.argentieri@gestic.it", new Date("06/13/1987"), "ROLE_ADMIN", encryptedPwd));
		encryptedPwd = encryptionUtils.encrypt("musica");
		utenteDao.save(new Utente("RSSDBR87S67C741M", "Debora Rossini", "VIA RIVIERA 1 27100 PAVIA (PV)", "423236543", "debora.rossini@gestic.it", new Date("11/27/1987"), "ROLE_USER", encryptedPwd));

		//******

		Cliente cliente1 = new Cliente("08652300156", "S.S.C. Società Sviluppo Commerciale s.r.l", "Francesco Manelli", "Milano, via Caldera 21", "3986532132", "info@sncc.com", "www.ccns.it", "Maurizio Iurlaro", "Ricerca e sviluppo", "Bando provinciale",  "RGNLSN87H13D761R", null, "Da confermare le competenze territoriali.");
		Cliente cliente2 = new Cliente("12683790153", "GS S.p.A.", "Lucia Distante", "Lecce, via dei mille 24", "3276534212", "info@gscommercial.com", "www.gs.com", "Candida Fornaro", "Edilizia", "Bando europeo", "RGNLSN87H13D761R", null, "La sede commerciale e' in via dei Giovi 21.");
		Cliente cliente3 = new Cliente("12002340151", "DI PER DI s.r.l. (a socio unico)", "Marco Viola", "Lucca, viale Monza 32", "3658795678", "info@diperdi.it", "www.diperdi.it", "Ottavio Luppoli", "Commercio", "Bando Horizon 2020", "RGNLSN87H13D761R", null, "Valutare l'inserimento in altri settori produttivi.");

		clienteDao.save(cliente1);
		clienteDao.save(cliente2);
		clienteDao.save(cliente3);

		//******

		Pratica p1 = new Pratica("rk235646332", "Rifacimento sezioni", "nota a caso", null, "08652300156", "RGNLSN87H13D761R");
		Pratica p2 = new Pratica("rh235646367", "Bandi regionali", "nota a caso", null, "08652300156", "RGNLSN87H13D761R");
		Pratica p3 = new Pratica("rl235612308", "Business Plan aziendale", "nota a caso", null, "12683790153", "RGNLSN87H13D761R");
		Pratica p4 = new Pratica("rs243435452", "Ottenimento fondo", "nota a caso", null, "12683790153", "RGNLSN87H13D761R");
		Pratica p5 = new Pratica("rm343221337", "Valutazione rischi", "nota a caso", null, "12002340151", "RGNLSN87H13D761R");
		Pratica p6 = new Pratica("rj235646454", "Vecchi bandi", "nota a caso", null,"12002340151", "RGNLSN87H13D761R");

		praticaDao.save(p1);
		praticaDao.save(p2);
		praticaDao.save(p3);
		praticaDao.save(p4);
		praticaDao.save(p5);
		praticaDao.save(p6);

		//******

		documentoDao.save(new Documento(null, "Documento di presentazione", "/documentazione/08652300156/rk235646332/documento.pdf", "NOTE_1", Pratica.STEP_AMMISSIONE, p1.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Valutazione rischi", "/documentazione/08652300156/rk235646332/documento.pdf", "NOTE_2", Pratica.STEP_AMMISSIONE, p1.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "Calcolo fatturati", "/documentazione/08652300156/rh235646367/documento.pdf", "NOTE_3", Pratica.STEP_COLLAUDO, p2.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Esenzione tasse", "/documentazione/08652300156/rh235646367/documento.pdf", "NOTE_4", Pratica.STEP_PRESENTAZIONE,  p2.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "Calcolo redditi", "/documentazione/12683790153/rl235612308/documento.pdf", "NOTE_5", Pratica.STEP_RENDICONTAZIONE, p3.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Bilancio in corso", "/documentazione/12683790153/rl235612308/documento.pdf",  "NOTE_6", Pratica.STEP_COLLAUDO, p3.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "Rimborso spese", "/documentazione/12683790153/rs243435452/documento.pdf",  "NOTE_7", Pratica.STEP_RENDICONTAZIONE, p4.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Processi produttivi", "/documentazione/12683790153/rs243435452/documento.pdf",  "NOTE_8", Pratica.STEP_PRESENTAZIONE, p4.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "Business Plan", "/documentazione/12002340151/rm343221337/documento.pdf", "NOTE_9", Pratica.STEP_AMMISSIONE, p5.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Progettazione sistema", "/documentazione/12002340151/rm343221337/documento.pdf", "NOTE_10", Pratica.STEP_COLLAUDO, p5.getIdPratica(), "RGNLSN87H13D761R", null));

		documentoDao.save(new Documento(null, "Collocazione immobili", "/documentazione/12002340151/rj235646454/documento.pdf", "NOTE_11", Pratica.STEP_PRESENTAZIONE, p6.getIdPratica(), "RGNLSN87H13D761R", null));
		documentoDao.save(new Documento(null, "Parametri di valutazione", "/documentazione/12002340151/rj235646454/documento.pdf", "NOTE_12", Pratica.STEP_AMMISSIONE, p6.getIdPratica(), "RGNLSN87H13D761R", null));
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