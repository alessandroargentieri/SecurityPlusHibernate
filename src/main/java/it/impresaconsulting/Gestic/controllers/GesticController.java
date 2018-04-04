package it.impresaconsulting.Gestic.controllers;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Documento;
import it.impresaconsulting.Gestic.entities.Pratica;
import it.impresaconsulting.Gestic.entities.Utente;
import it.impresaconsulting.Gestic.utilities.EncryptionUtils;
import it.impresaconsulting.Gestic.utilities.SecurityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static it.impresaconsulting.Gestic.utilities.SecurityImpl.ROLE_ADMIN;

@CrossOrigin
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

    @Autowired UtenteDao       utenteDao;
    @Autowired ClienteDao      clienteDao;
    @Autowired PraticaDao      praticaDao;
    @Autowired DocumentoDao    documentoDao;
    @Autowired EncryptionUtils encryptionUtils;

    //************************************************* TEST

    @RequestMapping("/test")
    public String testSistema(){
        return TEST;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/hello")
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

    @RequestMapping("get/utente")
    public List<Utente> getUtenti(){
        return utenteDao.findAll();
    }

    @RequestMapping("get/utente/{codicefiscale}")
    public Utente getUtenteByCodFisc(@PathVariable(name = "codicefiscale") String codiceFiscale){
        Optional<Utente> utenteOptional = utenteDao.findById(codiceFiscale);
        if(utenteOptional.isPresent()){
            return utenteOptional.get();
        } else {
            return null;
        }
    }

    @RequestMapping("/password")
    public String cambiaPassword(UsernamePasswordAuthenticationToken token, @RequestParam(name = "vecchiapassword") String vecchiaPassword, @RequestParam(name = "nuovapassword") String nuovaPassword) throws RuntimeException{
        Optional<Utente> utenteOptional = utenteDao.findById(token.getName());
        if(utenteOptional.isPresent()){
            Utente utente = utenteOptional.get();
            if(vecchiaPassword.equals(encryptionUtils.decrypt(utente.getPassword()))){
                utenteDao.deleteById(token.getName());
                utente.setPassword(encryptionUtils.encrypt(nuovaPassword));
                utenteDao.save(utente);
                return PASSWORD_CAMBIATA;
            }
        }
        throw new RuntimeException(PASSWORD_NON_CAMBIATA);
    }

    @RequestMapping("/save/utente")
    public String saveUtente(UsernamePasswordAuthenticationToken token, @Valid Utente utente){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utente.setPassword(encryptionUtils.encrypt(utente.getPassword()));
            utenteDao.save(utente);
            return NUOVO_UTENTE;
        } else {
            return NON_AUTORIZZATO;
        }
    }

    @RequestMapping("/delete/utente")
    public String deleteUtente(UsernamePasswordAuthenticationToken token, @RequestParam(name="codicefiscale") String codicefiscale){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utenteDao.deleteById(codicefiscale);
            return UTENTE_ELIMINATO;
        } else {
            return NON_AUTORIZZATO;
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/update/utente")
    public String updateUtente(UsernamePasswordAuthenticationToken token, @Valid Utente utente){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            if(utenteDao.findById(utente.getCodiceFiscale()).isPresent()){
                utenteDao.deleteById(utente.getCodiceFiscale());
            }
            utente.setPassword(encryptionUtils.encrypt(utente.getPassword()));
            utenteDao.save(utente);
            return UTENTE_AGGIORNATO;
        } else {
            return NON_AUTORIZZATO;
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

    @RequestMapping("get/cliente/ragionesociale/{ragionesociale}")
    public List<Cliente> getClienteByRagioneSociale(@PathVariable(name = "ragionesociale") String ragioneSociale){
        return clienteDao.findByRagioneSociale(ragioneSociale);
    }

    @RequestMapping("get/cliente/nominativo/{nominativo}")
    public List<Cliente> getClienteByNominativo(@PathVariable(name = "nominativo") String nominativo){
        return clienteDao.findByNominativo(nominativo);
    }

    @RequestMapping("get/cliente/attivita/{attivita}")
    public List<Cliente> getClienteByAttivita(@PathVariable(name = "attivita") String attivita){
        return clienteDao.findByAttivita(attivita);

    }

    @RequestMapping("get/cliente/segnalatore/{segnalatore}")
    public List<Cliente> getClienteBySegnalatore(@PathVariable(name = "segnalatore") String segnalatore){
        return clienteDao.findBySegnalatore(segnalatore);
    }

    @RequestMapping("get/cliente/interessatoa/{interessatoa}")
    public List<Cliente> getClienteByInteressatoA(@PathVariable(name = "interessatoa") String interessatoA){
        return clienteDao.findByInteressatoA(interessatoA);
    }


    @RequestMapping("/update/cliente")
    public Cliente modificaCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
        cliente.setRegistratoDa(token.getName());
        clienteDao.deleteById(cliente.getIdCliente());
        return clienteDao.save(cliente);
    }

    @RequestMapping("/save/cliente")
    public Cliente saveCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
       cliente.setRegistratoDa(token.getName());
       return clienteDao.save(cliente);
    }

    @RequestMapping("delete/cliente")
    public String deleteCliente(UsernamePasswordAuthenticationToken token, @PathVariable(name = "id") String idcliente){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            clienteDao.deleteById(idcliente);
            List<Pratica> pratiche = praticaDao.findByFkCliente(idcliente);
            for(Pratica pratica : pratiche){
                documentoDao.deleteDocumentiPerPratica(pratica.getIdPratica());
            }
            praticaDao.deletePraticaPerCliente(idcliente);
        } else {
            return NON_AUTORIZZATO;
        }
        return CLIENTE_ELIMINATO;
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

    @RequestMapping("get/pratica/cliente/{idcliente}")
    public List<Pratica> getPratichePerCliente(@PathVariable(name="idcliente") String idcliente){
        return praticaDao.findByFkCliente(idcliente);
    }

    @RequestMapping("/update/pratica")
    public Pratica updatePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica){
        pratica.setRegistratoDa(token.getName());
        praticaDao.deleteById(pratica.getIdPratica());
        return praticaDao.save(pratica);
    }

    @RequestMapping("/save/pratica")
    public Pratica savePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica){
       pratica.setRegistratoDa(token.getName());
       return praticaDao.save(pratica);
    }

    @RequestMapping("/delete/pratica")
    public String deletePratica(UsernamePasswordAuthenticationToken token, @RequestParam(name="idpratica") String idpratica){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            praticaDao.deleteById(idpratica);
            documentoDao.deleteDocumentiPerPratica(idpratica);
        } else {
            return NON_AUTORIZZATO;
        }
        return PRATICHE_ELIMINATE;
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

    @RequestMapping("get/documento/pratica/{idpratica}")
    public List<Documento> getDocumentiPerPratica(@PathVariable(name="idpratica") String idpratica){
        return documentoDao.findByFkPratica(idpratica);
    }

    @RequestMapping("/save/documento")
    public Documento saveDocumento(UsernamePasswordAuthenticationToken token, @Valid Documento documento){
        documento.setRegistratoDa(token.getName());
        return documentoDao.save(documento);
    }

    @RequestMapping("/update/documento")
    public Documento updateDocumento(UsernamePasswordAuthenticationToken token, @Valid Documento documento){
        documentoDao.deleteById(documento.getIdDocumento());
        documento.setRegistratoDa(token.getName());
        return documentoDao.save(documento);
    }

    @RequestMapping("/delete/documento")
    public void deleteDocumento(@RequestParam(name="iddocumento") String id){
        documentoDao.deleteById(id);
    }

}
