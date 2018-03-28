package it.impresaconsulting.Gestic.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity @Table(name="pratiche")
@AllArgsConstructor@NoArgsConstructor @lombok.Data
public class Pratica {

    @Id @Column(name="idpratica")
    private String idPratica;

    @Column(name="descrizione")
    private String descrizione;

    //@OneToMany(mappedBy="pratica") @JsonManagedReference
    //private List<Documento> documenti;

    @Column(name="note")
    private String note;

    @Column(name="data")
    private Date data;

    //@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="idcliente", nullable=false) @JsonBackReference
    //private Cliente cliente;

    @Column(name="fkcliente") @NotBlank
    private String fkCliente;

    @Column(name="registratoDa")
    private String registratoDa;

    @PrePersist
    private void setCampi(){
        data = new Date();
        idPratica = data.toInstant().toString() + fkCliente;
        //this.setId(UUID.randomUUID().toString());
    }
}
