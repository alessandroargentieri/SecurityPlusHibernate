package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.*;
import it.impresaconsulting.Gestic.entities.*;
import it.impresaconsulting.Gestic.utilities.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteServiceImpl implements UtenteService{

    @Autowired UtenteDao        utenteDao;
    @Autowired ClienteDao       clienteDao;
    @Autowired PraticaDao       praticaDao;
    @Autowired DocumentoDao     documentoDao;
    @Autowired ScadenzaDao      scadenzaDao;
    @Autowired EncryptionUtils  encryptionUtils;

    @Override
    public Utente updateUtente(String registratoDa, String oldId, Utente utente){
        String password = "";
        String ruolo = "";
        if(oldId !=null && !"".equals(oldId) && !utente.getCodiceFiscale().equals(oldId)){
            //stiamo aggiornando l'utente cambiandogli proprio la primary Key!
            List<Cliente> clienti = clienteDao.findByRegistratoDa(oldId);
            for(Cliente c : clienti){
                c.setRegistratoDa(utente.getCodiceFiscale());  //setto il nuovo profilo
                clienteDao.deleteById(c.getIdCliente());       //elimino il vecchio dal DB
                clienteDao.save(c);                            //salvo il nuovo
            }
            List<Pratica> pratiche = praticaDao.findByRegistratoDa(oldId);
            for(Pratica p : pratiche){
                p.setRegistratoDa(utente.getCodiceFiscale());  //setto il nuovo profilo
                praticaDao.deleteById(p.getIdPratica());       //elimino il vecchio dal DB
                praticaDao.save(p);                            //salvo il nuovo
            }
            List<Documento> documenti = documentoDao.findByRegistratoDa(oldId);
            for(Documento d : documenti){
                d.setRegistratoDa(utente.getCodiceFiscale());  //setto il nuovo profilo
                documentoDao.deleteById(d.getIdDocumento());       //elimino il vecchio dal DB
                documentoDao.save(d);                            //salvo il nuovo
            }
            List<Scadenza> scadenze = scadenzaDao.findByRegistratoDa(oldId);
            for(Scadenza s : scadenze){
                s.setRegistratoDa(utente.getCodiceFiscale());  //setto il nuovo profilo
                scadenzaDao.deleteById(s.getIdScadenza());       //elimino il vecchio dal DB
                scadenzaDao.save(s);                            //salvo il nuovo
            }
            if(utenteDao.findById(oldId).isPresent()){
                password = utenteDao.findById(oldId).get().getPassword(); //è un update: è recuperata la vecchia password criptata
                ruolo = utenteDao.findById(oldId).get().getRuolo();       //è un update: è recuperato il ruolo
                utenteDao.deleteById(oldId);
            }
        }

        if(utenteDao.findById(utente.getCodiceFiscale()).isPresent()){
            password = utenteDao.findById(utente.getCodiceFiscale()).get().getPassword(); //è un update: è recuperata la vecchia password criptata
            ruolo = utenteDao.findById(oldId).get().getRuolo();       //è un update: è recuperato il ruolo
            utenteDao.deleteById(utente.getCodiceFiscale());
        }
        if("".equals(password)){ //non è un update ma un inserimento viene criptata la password di default '1234'
            password = encryptionUtils.encrypt(utente.getPassword());
        }
        utente.setPassword(password);
        if(!"".equals(ruolo)){//è un update e lasciamo il ruolo preesistente invece che lasciarlo settare a 'ROLE_USER'
            utente.setRuolo(ruolo);
        }
        return utenteDao.save(utente);
    }

    @Override
    public void deleteUtente(String codiceFiscale){
        utenteDao.deleteById(codiceFiscale);
    }

    @Override
    public Utente saveUtente(Utente utente){
        utente.setPassword(encryptionUtils.encrypt(utente.getPassword()));
        return utenteDao.save(utente);
    }

    @Override
    public boolean cambiaPassword(String codiceFiscale, String vecchiaPassword, String nuovaPassword){
        Optional<Utente> utenteOptional = utenteDao.findById(codiceFiscale);
        if(utenteOptional.isPresent()){
            Utente utente = utenteOptional.get();
            if(vecchiaPassword.equals(encryptionUtils.decrypt(utente.getPassword()))){
                utenteDao.deleteById(codiceFiscale);
                utente.setPassword(encryptionUtils.encrypt(nuovaPassword));
                utenteDao.save(utente);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public Utente getUtenteByCodiceFiscale(String codiceFiscale){
        Optional<Utente> utenteOptional = utenteDao.findById(codiceFiscale);
        if(utenteOptional.isPresent()){
            return utenteOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Utente> getAll(){
        return utenteDao.findAll();
    }

    @Override
    public String getMioNome(String codiceFiscale){
        Optional<Utente> optionalUtente = utenteDao.findById(codiceFiscale);
        if(optionalUtente.isPresent()){
            return optionalUtente.get().getNominativo();
        }else{
            return "";
        }
    }
}
