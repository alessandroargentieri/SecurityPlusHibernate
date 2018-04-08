package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.entities.Documento;

import java.util.List;

public interface DocumentoService {

    void deleteDocumentoById(String id);
    String updateDocumento(String registratoDa, Documento documento);
    Documento saveDocumento(String registratoDa, Documento documento);
    List<Documento> getDocumentiByPratica(String idPratica);
    Documento getDocumentoById(String idDocumento);
    List<Documento> getAllDocumenti();

}
