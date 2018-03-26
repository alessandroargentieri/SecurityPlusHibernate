package it.impresaconsulting.Gestic.controllers;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import it.impresaconsulting.Gestic.entities.Utente;
import it.impresaconsulting.Gestic.services.ClienteService;
import it.impresaconsulting.Gestic.services.PraticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class GesticController {

    private static final String PASSWORD_CAMBIATA = "Password modificata correttamente!";
    private static final String PASSWORD_NON_CAMBIATA = "Errore nella modifica della password!";

    @Autowired UtenteDao    utenteDao;
    @Autowired ClienteDao   clienteDao;
    @Autowired PraticaDao   praticaDao;
    @Autowired DocumentoDao documentoDao;

    @Autowired ClienteService clienteService;
    @Autowired PraticaService praticaService;

    //************************************************* TEST

    @RequestMapping("/test")
    public String testSistema(){
        return "<h1>Benvenuti in Gestic</h1><p>Il gestionale di Impresa Consulting s.r.l.</p>";
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/hello")
    public String getResponse(UsernamePasswordAuthenticationToken token){
        return "Benvenuto/a! Ti sei loggato/a correttamente come " + token.getName() + "! <br/>";
    }

    //************************************************* UTENTE

    @RequestMapping("get/utente")
    public List<Utente> getUtenti(){
        return utenteDao.findAll();   //TODO: oscurare le password
    }

    @RequestMapping("get/utente/{codicefiscale}")
    public Utente getUtenteByCodFisc(@PathVariable(name = "codicefiscale") String codiceFiscale){
        Optional<Utente> utenteOptional = utenteDao.findById(codiceFiscale);
        if(utenteOptional.isPresent()){
            return utenteOptional.get();  //TODO: oscurare le password
        } else {
            return null;
        }
    }

    @RequestMapping("/password")
    public String cambiaPassword(UsernamePasswordAuthenticationToken token, @RequestAttribute(name = "nuovapassword") String nuovaPassword){
        Optional<Utente> utenteOptional = utenteDao.updatePassword(token.getName(), nuovaPassword);
        if(utenteOptional.isPresent()){
            return PASSWORD_CAMBIATA;
        } else {
            return PASSWORD_NON_CAMBIATA;
        }
    }

    //************************************************* CLIENTE

    @RequestMapping("/get/cliente")
    public List<Cliente> getClienti(){
        return clienteDao.findAll();
    }

    @RequestMapping("get/cliente/{id}")
    public Cliente getClienteById(@PathVariable(name = "id") String id){
        Optional<Cliente> clienteOptional = clienteDao.findById(id);
        if(clienteOptional.isPresent()){
            return clienteOptional.get();
        } else {
            return null;
        }
    }

    @RequestMapping("/update/cliente")
    public Cliente modificaCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
        cliente.setRegistratoDa(token.getName());
        return clienteService.updateCliente(cliente);
    }

    @RequestMapping("/save/cliente")
    public Cliente saveCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
       cliente.setRegistratoDa(token.getName());
       return clienteDao.save(cliente);
    }

    @RequestMapping("delete/cliente")
    public String deleteCliente(UsernamePasswordAuthenticationToken token, @PathVariable(name = "id") String id){
      /*  try {
            Cliente cliente = clienteDao.getOne(id);
            List<Pratica> pratiche = cliente.getPratiche();
            if (pratiche != null) {
                for (Pratica pratica : pratiche) {
                    List<Documento> documenti = pratica.getDocumenti();
                    if (documenti != null) {
                        for (Documento documento : documenti) {
                            documentoDao.delete(documento);
                        }
                    }
                    praticaDao.delete(pratica);
                }
            }
            clienteDao.delete(cliente);
        } catch(Exception e){
            return "C'è stato un errore. Contattare l'assistenza: " + e.toString();
        }*/
        return "Cliente, pratiche annesse e documenti sono stati eliminati con successo";
    }


    //************************************************* PRATICA


    @RequestMapping("get/pratica")
    public List<Pratica> getPratiche(){
        return praticaDao.findAll();
    }

    @RequestMapping("get/pratica/{id}")
    public Pratica getPraticaById(@PathVariable(name = "id") String id){
        Optional<Pratica> praticaOptional = praticaDao.findById(id);
        if(praticaOptional.isPresent()){
            return praticaOptional.get();
        } else {
            return null;
        }
    }

    @RequestMapping("/update/pratica")
    public Pratica updatePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica){
        pratica.setRegistratoDa(token.getName());
        return praticaService.updatePratica(pratica);
    }

    @RequestMapping("/save/pratica")
    public Pratica savePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica, String clienteId){
       // pratica.setCliente(clienteDao.getOne(clienteId));
        pratica.setRegistratoDa(token.getName());
        return praticaDao.save(pratica);
    }

    //************************************************* DOCUMENTO


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("get/documento")
    public List<Documento> getDocumenti(){
        return documentoDao.findAll();
    }

    @RequestMapping("get/documento/{id}")
    public Documento getDocumentoById(@PathVariable(name = "id") String id){
        Optional<Documento> documentoOptional = documentoDao.findById(id);
        if(documentoOptional.isPresent()){
            return documentoOptional.get();
        } else {
            return null;
        }
    }

    @RequestMapping("/save/documento")
    public Documento saveDocumento(){
        return null;
    }




}
