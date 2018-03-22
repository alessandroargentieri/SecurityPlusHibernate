package it.impresaconsulting.Gestic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity @Table(name="documenti")
@AllArgsConstructor @NoArgsConstructor @Data
public class Documento {

    @Id @Column(name="iddocumento")
    private String idDocumento;

    @Column(name="descrizione")
    private String descrizione;

    @Column(name="percorsofile") @NotBlank
    private String percorsoFile;

    @Column(name="note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="idpratica", nullable=false) @JsonBackReference
    private Pratica pratica;

    @Column(name="registratoda")
    private String registratoDa;

    @Column(name="data")
    private Date data;

    @PrePersist
    private void setCampi(){
        data = new Date();
        idDocumento = data.toInstant().toString() + pratica.getIdPratica();
    }

}
