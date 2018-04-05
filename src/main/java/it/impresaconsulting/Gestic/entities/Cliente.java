package it.impresaconsulting.Gestic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * tabella dei clienti dell'azienda
 */

@Entity @Table(name="clienti")
@AllArgsConstructor @NoArgsConstructor @Data
public class Cliente {

    @Id @Column(name="idcliente") @NotBlank
    private String idCliente;               //P.IVA o CODICE FISCALE

    @Column(name="ragionesociale")
    private String ragioneSociale;          //VUOTO SE PERSONA FISICA

    @Column(name="nominativo") @NotBlank
    private String nominativo;              //NOME E COGNOME

    @Column(name="indirizzo")
    private String indirizzo;

    @Column(name="telefono")
    private String telefono;

    @Column(name="email") @Email
    private String email;

    @Column(name="sito")
    private String sito;

    @Column(name="segnalatore")
    private String segnalatore;

    @Column(name="attivita")
    private String attivita;

    @Column(name="interessatoa")
    private String interessatoA;

    //@OneToMany(mappedBy="cliente") @JsonManagedReference
    //private List<Pratica> pratiche;

    @Column(name="registratoda")
    private String registratoDa;    //utente che ha registrato il cliente

    @Column(name="data")
    private Date data;

    @Column(name="note")
    private String note;

    @PrePersist
    private void salvaData(){
        data = new Date();
    }
}
