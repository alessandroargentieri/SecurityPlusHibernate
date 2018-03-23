package it.impresaconsulting.Gestic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity @Table(name="pratiche")
@AllArgsConstructor@NoArgsConstructor @lombok.Data
public class Pratica {

    @Id @Column(name="idpratica")
    private String idPratica;
    @Column(name="descrizione")
    private String descrizione;

    @OneToMany(mappedBy="pratica") @JsonManagedReference
    private List<Documento> documenti;

    @Column(name="note")
    private String note;

    @Column(name="data")
    private Date data;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="idcliente", nullable=false) @JsonBackReference
    private Cliente cliente;

    @Column(name="registratoDa")
    private String registratoDa;

    @PrePersist
    private void setCampi(){
        data = new Date();
        idPratica = data.toInstant().toString() + cliente.getIdCliente();
    }
}