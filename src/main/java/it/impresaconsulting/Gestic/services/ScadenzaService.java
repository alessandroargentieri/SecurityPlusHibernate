package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.entities.Scadenza;

import java.util.List;

public interface ScadenzaService {

    List<Scadenza> getScadenze();
    List<Scadenza> getAllScadenze();
    void deleteOldScadenze();
    void deleteScandenzaById(Long id);
    Scadenza salvaScadenza(String registratoDa, Scadenza scadenza);


}
