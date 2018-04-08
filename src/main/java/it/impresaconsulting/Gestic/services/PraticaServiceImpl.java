package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PraticaServiceImpl implements PraticaService {

    @Autowired PraticaDao   praticaDao;
    @Autowired DocumentoDao documentoDao;

    @Override
    public Pratica updatePratica(String registratoDa, String oldId, Pratica pratica){
        pratica.setRegistratoDa(registratoDa);

        if(oldId !=null && !"".equals(oldId) && !pratica.getIdPratica().equals(oldId)) {
            //stiamo aggiornando il cliente cambiandogli proprio la primary Key!
            List<Documento> documenti = documentoDao.findByFkPratica(oldId);
            for(Documento d : documenti){
                d.setFkPratica(pratica.getIdPratica());
                documentoDao.deleteById(d.getIdDocumento());
                documentoDao.save(d);
            }
            if(praticaDao.findById(oldId).isPresent()){
                praticaDao.deleteById(oldId);
            }
        }

        if(praticaDao.findById(pratica.getIdPratica()).isPresent()){
            praticaDao.deleteById(pratica.getIdPratica());
        }
        return praticaDao.save(pratica);
    }

    @Override
    public void deletePraticaAndRelativiDocumentiPerIdPratica(String idpratica){
        praticaDao.deleteById(idpratica);
        documentoDao.deleteDocumentiPerPratica(idpratica);
    }

    @Override
    public Pratica savePratica(String registratoDa, Pratica pratica){
        pratica.setRegistratoDa(registratoDa);
        return praticaDao.save(pratica);
    }

    @Override
    public List<Pratica> findPraticaPerFkCliente(String fkCliente){
        return praticaDao.findByFkCliente(fkCliente);
    }

    @Override
    public Pratica getPraticaById(String idPratica){
        Optional<Pratica> praticaOptional = praticaDao.findById(idPratica);
        if(praticaOptional.isPresent()){
            return praticaOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Pratica> getAll(){
        return praticaDao.findAll();
    }

}
