package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.entities.Utente;

import java.util.List;

public interface UtenteService {
    Utente updateUtente(String registratoDa, String oldId, Utente utente);
    void deleteUtente(String codiceFiscale);
    Utente saveUtente(Utente utente);
    boolean cambiaPassword(String codiceFiscale, String vecchiaPassword, String nuovaPassword);
    Utente getUtenteByCodiceFiscale(String codiceFiscale);
    List<Utente> getAll();
    String getMioNome(String codiceFiscale);
}
