package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.entities.Documento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    private static final String DOCUMENTO_AGGIORNATO    = "Aggiornamento documentazione avvenuto con successo!";

    @Autowired
    DocumentoDao documentoDao;

    @Override
    public void deleteDocumentoById(String id) {
        documentoDao.deleteById(id);
    }

    @Override
    public Documento saveDocumento(String registratoDa, Documento documento){
        documento.setRegistratoDa(registratoDa);
        return documentoDao.save(documento);
    }

    @Override
    public String updateDocumento(String registratoDa, Documento documento){
        documento.setRegistratoDa(registratoDa);
        if(documentoDao.findById(documento.getIdDocumento()).isPresent()){
            documentoDao.deleteById(documento.getIdDocumento());
        }
        documentoDao.save(documento);
        return DOCUMENTO_AGGIORNATO;
    }

    @Override
    public Documento getDocumentoById(String idDocumento){
        Optional<Documento> documentoOptional = documentoDao.findById(idDocumento);
        if(documentoOptional.isPresent()){
            return documentoOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Documento> getDocumentiByPratica(String idPratica){
        return documentoDao.findByFkPratica(idPratica);
    }

    @Override
    public List<Documento> getAllDocumenti(){
        return documentoDao.findAll();
    }
}
