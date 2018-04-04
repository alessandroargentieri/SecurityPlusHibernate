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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private static final String PRATICA_AGGIORNATA      = "Aggiornamento pratiche avvenuto con successo!";
    private static final String CLIENTE_AGGIORNATO      = "Aggiornamento contatti avvenuto con successo!";
    private static final String DOCUMENTO_AGGIORNATO    = "Aggiornamento documentazione avvenuto con successo!";
    private static final String SELEZIONA_UN_FILE       = "Seleziona il file da allegare";
    private static final String CARICAMENTO_AVVENUTO    = "Documento caricato: ";

    private static String UPLOADED_FOLDER = "/Users/alessandroargentieri/Desktop/logback/";//"./documentazione";

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
    public String cambiaPassword(UsernamePasswordAuthenticationToken token, @RequestParam(name = "vecchiapassword") String vecchiaPassword, @RequestParam(name = "nuovapassword") String nuovaPassword) {
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/utente/{codicefiscale}")
    public String deleteUtente(UsernamePasswordAuthenticationToken token, @PathVariable(name="codicefiscale") String codicefiscale){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            utenteDao.deleteById(codicefiscale);
            return UTENTE_ELIMINATO;
        } else {
            return NON_AUTORIZZATO;
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/update/utente")
    public String updateUtente(UsernamePasswordAuthenticationToken token, @RequestParam(name = "oldId") String oldId, @Valid Utente utente) throws RuntimeException{
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){

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
                if(utenteDao.findById(oldId).isPresent()){
                    utenteDao.deleteById(oldId);
                }
            }

            if(utenteDao.findById(utente.getCodiceFiscale()).isPresent()){
                utenteDao.deleteById(utente.getCodiceFiscale());
            }

            utente.setPassword(encryptionUtils.encrypt(utente.getPassword()));
            utenteDao.save(utente);
            return UTENTE_AGGIORNATO;
        } else {
            throw new RuntimeException(NON_AUTORIZZATO);
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
    public String updateCliente(UsernamePasswordAuthenticationToken token,  @RequestParam(name = "oldId") String oldId, @Valid Cliente cliente){
        cliente.setRegistratoDa(token.getName());

        if(oldId !=null && !"".equals(oldId) && !cliente.getIdCliente().equals(oldId)) {
            //stiamo aggiornando il cliente cambiandogli proprio la primary Key!
            List<Pratica> pratiche = praticaDao.findByFkCliente(oldId);
            for(Pratica p : pratiche){
                p.setFkCliente(cliente.getIdCliente());
                praticaDao.deleteById(p.getIdPratica());
                praticaDao.save(p);
            }
            if(clienteDao.findById(oldId).isPresent()){
                clienteDao.deleteById(oldId);
            }
        }

        if(clienteDao.findById(cliente.getIdCliente()).isPresent()){
            clienteDao.deleteById(cliente.getIdCliente());
        }

        clienteDao.save(cliente);
        return CLIENTE_AGGIORNATO;
    }

    @RequestMapping("/save/cliente")
    public Cliente saveCliente(UsernamePasswordAuthenticationToken token, @Valid Cliente cliente){
       cliente.setRegistratoDa(token.getName());
       return clienteDao.save(cliente);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("delete/cliente/{idcliente}")
    public String deleteCliente(UsernamePasswordAuthenticationToken token, @PathVariable(name = "idcliente") String idcliente){
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
    public String updatePratica(UsernamePasswordAuthenticationToken token,  @RequestParam(name = "oldId") String oldId, @Valid Pratica pratica){
        pratica.setRegistratoDa(token.getName());

        if(oldId !=null && !"".equals(oldId) && !pratica.getIdPratica().equals(oldId)) {
            //stiamo aggiornando il cliente cambiandogli proprio la primary Key!
            List<Documento> documenti = documentoDao.findByFkPratica(oldId);
            for(Documento d : documenti){
                d.setFkPratica(pratica.getIdPratica());
                documentoDao.deleteById(d.getIdDocumento());
                documentoDao.save(d);
            }
            if(praticaDao.findById(oldId).isPresent()){
                praticaDao.deleteById(oldId);
            }
        }

        if(praticaDao.findById(pratica.getIdPratica()).isPresent()){
            praticaDao.deleteById(pratica.getIdPratica());
        }
        praticaDao.save(pratica);
        return PRATICA_AGGIORNATA;
    }

    @RequestMapping("/save/pratica")
    public Pratica savePratica(UsernamePasswordAuthenticationToken token, @Valid Pratica pratica){
       pratica.setRegistratoDa(token.getName());
       return praticaDao.save(pratica);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/pratica/{idpratica}")
    public String deletePratica(UsernamePasswordAuthenticationToken token, @PathVariable(name="idpratica") String idpratica){
        if(token.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN))){
            praticaDao.deleteById(idpratica);
            documentoDao.deleteDocumentiPerPratica(idpratica);
            return PRATICHE_ELIMINATE;
        } else {
             throw new RuntimeException(NON_AUTORIZZATO);
        }

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
    public String updateDocumento(UsernamePasswordAuthenticationToken token, @Valid Documento documento){
        documento.setRegistratoDa(token.getName());
        if(documentoDao.findById(documento.getIdDocumento()).isPresent()){
            documentoDao.deleteById(documento.getIdDocumento());
        }
        documentoDao.save(documento);
        return DOCUMENTO_AGGIORNATO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete/documento/{iddocumento}")
    public void deleteDocumento(@PathVariable(name="iddocumento") String id){
        documentoDao.deleteById(id);
    }


    //**************** UPLOAD FILE ********************************************


    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFileMulti(@RequestParam("cliente") String cliente, @RequestParam("pratica") String pratica, @RequestParam("step") String step, @RequestParam("documento") String documento, @RequestParam("files") MultipartFile[] uploadfiles) {
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity(SELEZIONA_UN_FILE, HttpStatus.OK);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfiles), cliente, pratica, step, documento);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(CARICAMENTO_AVVENUTO + uploadedFileName, HttpStatus.OK);
    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files, String cliente, String pratica, String step, String documento) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; //next pls
            }
            byte[] bytes = file.getBytes();
            String dinamicFolder = UPLOADED_FOLDER + cliente + "/";
            File directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }
            dinamicFolder = dinamicFolder + pratica + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }
            dinamicFolder = dinamicFolder + step + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }
            dinamicFolder = dinamicFolder + documento + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }

            Path path = Paths.get(dinamicFolder + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }







}
