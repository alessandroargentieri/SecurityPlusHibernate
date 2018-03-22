package it.impresaconsulting.Gestic.controllers;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import it.impresaconsulting.Gestic.entities.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GesticController {

    @Autowired UtenteDao utenteDao;
    @Autowired ClienteDao clienteDao;
    @Autowired PraticaDao praticaDao;
    @Autowired DocumentoDao documentoDao;

    @RequestMapping("/test")
    public String testSistema(){
        return "<h1>Benvenuti in Gestic</h1><p>Il gestionale di Impresa Consulting s.r.l.</p>";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/hello")
    public String getResponse(UsernamePasswordAuthenticationToken token){
        return "Welcome! You are correctly logged! <br/>" + token.getName() + "!!!";
    }

    @RequestMapping("/utente")
    public List<Utente> getUtenti(){
        return utenteDao.findAll();
    }

    @RequestMapping("/cliente")
    public List<Cliente> getClienti(){
        return clienteDao.findAll();
    }

    @RequestMapping("/pratica")
    public List<Pratica> getPratiche(){
        return praticaDao.findAll();
    }

    @RequestMapping("/documento")
    public List<Documento> getDocumenti(){
        return documentoDao.findAll();
    }

}
