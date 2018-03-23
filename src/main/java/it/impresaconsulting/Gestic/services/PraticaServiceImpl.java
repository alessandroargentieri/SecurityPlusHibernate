package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.entities.Pratica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PraticaServiceImpl implements PraticaService {

    @Autowired
    PraticaDao praticaDao;

    @Override
    public Pratica updatePratica(Pratica pratica){
        praticaDao.deleteById(pratica.getIdPratica());  //TODO vedere se a tale pratica dobbiamo associare la lista di documenti estraendola dalla pratica da cancellare e inserendola nella nuova. Vedere anche se la nuova pratica va inserita in tutti i documenti della pratica vecchia cancellata.
        return praticaDao.save(pratica);
    }

}
