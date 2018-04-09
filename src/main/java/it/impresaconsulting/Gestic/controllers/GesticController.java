package it.impresaconsulting.Gestic.controllers;

import it.impresaconsulting.Gestic.entities.*;
import it.impresaconsulting.Gestic.services.*;
import it.impresaconsulting.Gestic.utilities.FileUtils;
import it.impresaconsulting.Gestic.utilities.SecurityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static it.impresaconsulting.Gestic.utilities.SecurityImpl.ROLE_ADMIN;

@RestController
public class GesticController {

    private static final String PASSWORD_CAMBIATA       = "Password modificata correttamente!";
    private static final String PASSWORD_NON_CAMBIATA   = "Errore nella modifica della password!";
    private static final String NUOVO_UTENTE            = "Nuovo utente memorizzato con successo";
    private static final String NON_AUTORIZZATO         = "Non sei autorizzato/a ad effettuare questa operazione";
    private static final String PRATICHE_ELIMINATE      = "Pratiche e documenti annessi sono stati eliminati con successo";
    private static final String UTENTE_AGGIORNATO       = "Informazioni utente aggiornate";
    private static final String UTENTE_ELIMINATO        = "Utente eliminato";
    private static final String CLIENTE_ELIMINATO       = "Cliente, pratiche annesse e documenti sono stati eliminati con successo";
    private static final String LOGGATO                 = "Benvenuto/a! Ti sei loggato/a correttamente come %s! <br/>";
    private static final String TEST                    = "<h1>Benvenuti in Gestic</h1><p>Il gestionale di Impresa Consulting s.r.l.</p>";
    private static final String PRATICA_AGGIORNATA      = "Aggiornamento pratiche avvenuto con successo!";
    private static final String CLIENTE_AGGIORNATO      = "Aggiornamento contatti avvenuto con successo!";

    @Autowired ScadenzaService  scadenzaService;
    @Autowired FileUtils        fileUtils;
    @Autowired DocumentoService documentoService;
    @Autowired PraticaService   praticaService;
    @Autowired ClienteService   clienteService;
    @Autowired UtenteService    utenteService;


    //************************************************* TEST

    @RequestMapping("/test")
    public String testSistema(){
        return TEST;
    }

    @RequestMapping("/hellox")
    public String getResponse(UsernamePasswordAuthenticationToken token){
        return String.format(LOGGATO, token.getName());
    }

    @RequestMapping("/user/or/admin")
    public String isAdminOrIsUser(UsernamePasswordAuthenticationToken token){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            return SecurityImpl.ROLE_ADMIN;
        }else{
            return SecurityImpl.ROLE_USER;
        }
    }

    //************************************************* UTENTE

    @RequestMapping("/get/mio/nome")
    public String getMioNome(UsernamePasswordAuthenticationToken token){
        return utenteService.getMioNome(token.getName());
    }

    @RequestMapping("get/utente")
    public List<Utente> getUtenti(){
        return utenteService.getAll();
    }

    @RequestMapping("get/utente/{codicefiscale}")
    public Utente getUtenteByCodFisc(@PathVariable(name = "codicefiscale") String codiceFiscale){
        return utenteService.getUtenteByCodiceFiscale(codiceFiscale);
    }

    @RequestMapping("/password")
    public String cambiaPassword(UsernamePasswordAuthenticationToken token, @RequestParam(name = "vecchiapassword") String vecchiaPassword, @RequestParam(name = "nuovapassword") String nuovaPassword) {
        boolean success = utenteService.cambiaPassword(token.getName(), vecchiaPassword, nuovaPassword);
        if(success){
            return PASSWORD_CAMBIATA;
        }else{
            throw new RuntimeException(PASSWORD_NON_CAMBIATA);
        }
    }

    @RequestMapping("/save/utente")
    public String saveUtente(UsernamePasswordAuthenticationToken token, @Valid Utente utente){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utenteService.saveUtente(utente);
        } else {
            return NON_AUTORIZZATO;
        }
        return NUOVO_UTENTE;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/utente/{codicefiscale}")
    public String deleteUtente(UsernamePasswordAuthenticationToken token, @PathVariable(name="codicefiscale") String codicefiscale){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utenteService.deleteUtente(codicefiscale);
        } else {
            return NON_AUTORIZZATO;
        }
        return UTENTE_ELIMINATO;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/update/utente")
    public String updateUtente(UsernamePasswordAuthenticationToken token, @RequestParam(name = "oldId") String oldId, @Valid Utente utente) throws RuntimeException{
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utenteService.updateUtente(token.getName(), oldId, utente);
        } else {
            throw new RuntimeException(NON_AUTORIZZATO);
        }
        return UTENTE_AGGIORNATO;
    }

    //************************************************* CLIENTE

    @RequestMapping("/get/cliente")
    public List<Cliente> getClienti(){
        return clienteService.getAll();
    }

    @RequestMapping("get/cliente/{id}")
    public Cliente getClienteById(@PathVariable(name = "id") String id){
        return clienteService.findClienteById(id);
    }

    @RequestMapping("get/cliente/id/{id}")
    public List<Cliente> getPseudoListClienteById(@PathVariable(name = "id") String id){
        return clienteService.findClienteByIdWithAPseudoList(id);
    }

    @RequestMapping("get/cliente/ragionesociale/{ragionesociale}")
    public List<Cliente> getClienteByRagioneSociale(@PathVariable(name = "ragionesociale") String ragioneSociale){
        return clienteService.findClienteByRagioneSociale(ragioneSociale);
    }

    @RequestMapping("get/cliente/nominativo/{nominativo}")
    public List<Cliente> getClienteByNominativo(@PathVariable(name = "nominativo") String nominativo){
        return clienteService.findClienteByNominativo(nominativo);
    }

    @RequestMapping("get/cliente/attivita/{attivita}")
    public List<Cliente> getClienteByAttivita(@PathVariable(name = "attivita") String attivita){
        return clienteService.findClienteByAttivita(attivita);
    }

    @RequestMapping("get/cliente/segnalatore/{segnalatore}")
    public List<Cliente> getClienteBySegnalatore(@PathVariable(name = "segnalatore") String segnalatore){
        return clienteService.findClienteBySegnalatore(segnalatore);
    }

    @RequestMapping("get/cliente/interessatoa/{interessatoa}")
    public List<Cliente> getClienteByInteressatoA(@PathVariable(name = "interessatoa") String interessatoA){
        return clienteService.findClienteByInteressatoA(interessatoA);
    }


    @RequestMapping("/update/cliente")
    public String updateCliente(UsernamePasswordAuthenticationToken token,  @RequestParam(name = "oldId") String oldId, @Valid Cliente cliente){
        clienteService.updateCliente(token.getName(), oldId, cliente);
        return CLIENTE_AGGIORNATO;
    }

    @RequestMapping("/save/cliente")
    public Cliente saveCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
       return clienteService.saveCliente(token.getName(), cliente);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("delete/cliente/{idcliente}")
    public String deleteCliente(UsernamePasswordAuthenticationToken token, @PathVariable(name = "idcliente") String idcliente){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            clienteService.deleteClienteAndRelativePraticheAndDocumentiByIdCliente(idcliente);
        } else {
            return NON_AUTORIZZATO;
        }
        return CLIENTE_ELIMINATO;
    }

    //************************************************* PRATICA

    @RequestMapping("get/pratica")
    public List<Pratica> getPratiche(){
        return praticaService.getAll();
    }

    @RequestMapping("get/pratica/{id}")
    public Pratica getPraticaPerId(@PathVariable(name = "id") String idPratica){
        return praticaService.getPraticaById(idPratica);
    }

    @RequestMapping("get/pratica/cliente/{idcliente}")
    public List<Pratica> getPratichePerCliente(@PathVariable(name="idcliente") String idcliente){
        return praticaService.findPraticaPerFkCliente(idcliente);
    }

    @RequestMapping("/update/pratica")
    public String updatePratica(UsernamePasswordAuthenticationToken token, @RequestParam(name = "oldId") String oldId, @Valid Pratica pratica){
        praticaService.updatePratica(token.getName(), oldId, pratica);
        return PRATICA_AGGIORNATA;
    }

    @RequestMapping("/save/pratica")
    public Pratica savePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica){
       return praticaService.savePratica(token.getName(), pratica);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/pratica/{idpratica}")
    public String deletePratica(UsernamePasswordAuthenticationToken token, @PathVariable(name="idpratica") String idpratica){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            praticaService.deletePraticaAndRelativiDocumentiPerIdPratica(idpratica);
        } else {
             throw new RuntimeException(NON_AUTORIZZATO);
        }
        return PRATICHE_ELIMINATE;
    }

    //************************************************* DOCUMENTO

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("get/documento")
    public List<Documento> getDocumenti(){
        return documentoService.getAllDocumenti();
    }

    @RequestMapping("get/documento/{id}")
    public Documento getDocumentoById(@PathVariable(name = "id") String id){
        return documentoService.getDocumentoById(id);
    }

    @RequestMapping("get/documento/pratica/{idpratica}")
    public List<Documento> getDocumentiPerPratica(@PathVariable(name="idpratica") String idpratica){
        return documentoService.getDocumentiByPratica(idpratica);
    }

    @RequestMapping("/save/documento")
    public Documento salvaDocumento(UsernamePasswordAuthenticationToken token, @Valid Documento documento){
        return documentoService.saveDocumento(token.getName(), documento);
    }

    @RequestMapping("/update/documento")
    public String aggiornaDocumento(UsernamePasswordAuthenticationToken token, @Valid Documento documento){
        return documentoService.updateDocumento(token.getName(), documento);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/documento/{iddocumento}")
    public void deleteDocumento(@PathVariable(name="iddocumento") String id){
        documentoService.deleteDocumentoById(id);
    }

    //**************** UPLOAD FILE ********************************************

    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFileMulti(@RequestParam("cliente") String cliente, @RequestParam("pratica") String pratica, @RequestParam("step") String step, @RequestParam("files") MultipartFile[] uploadfiles) {
        return fileUtils.uploadFileOnServer(cliente, pratica, step, uploadfiles);
    }

    //*************************** DOWNLOAD FILE *******************************

    @RequestMapping(value="/getfile", method=RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@RequestParam("filename") String filename) throws IOException{
        return fileUtils.getFile(filename);
    }

    //*************************** SCADENZE *******************************

    @RequestMapping("save/scadenza")
    public Scadenza creaNuovaScadenza(UsernamePasswordAuthenticationToken token, @Valid Scadenza scadenza){
        return scadenzaService.salvaScadenza(token.getName(), scadenza);
    }

    @RequestMapping("delete/scadenza/{idscadenza}")
    public void deleteScadenza(@PathVariable("idscadenza") Long id){
        scadenzaService.deleteScandenzaById(id);
    }

    @RequestMapping("/delete/old/scadenza")
    public void deleteScadenzeVecchie(){
        scadenzaService.deleteOldScadenze();
    }

    @RequestMapping("/get/all/scadenza")
    public List<Scadenza> allScadenze(){
        return scadenzaService.getAllScadenze();
    }

    @RequestMapping("/get/scadenza")
    public List<Scadenza> ciSonoScadenze(){
        return scadenzaService.getScadenze();
    }


}
