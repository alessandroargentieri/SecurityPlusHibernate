package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.entities.Pratica;

import java.util.List;

public interface PraticaService {

    Pratica updatePratica(String registratoDa, String oldId, Pratica pratica);
    void deletePraticaAndRelativiDocumentiPerIdPratica(String id);
    Pratica savePratica(String registratoDa, Pratica p);
    List<Pratica> findPraticaPerFkCliente(String fkCliente);
    Pratica getPraticaById(String idPratica);
    List<Pratica> getAll();
}
