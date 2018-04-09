package it.impresaconsulting.Gestic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity @Table(name="scadenze")
@AllArgsConstructor @NoArgsConstructor @Data
public class Scadenza {

    public static final String GIORNO_STESSO  = "giorno_stesso";
    public static final String GIORNO_PRIMA   = "giorno_prima";
    public static final String CINQUE_GIORNI  = "cinque_giorni_prima";
    public static final String DIECI_GIORNI   = "dieci_giorni_prima";

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idscadenza", columnDefinition = "INT(11) UNSIGNED")
    private Long idScadenza;
    @Column(name="descrizione") @NotBlank
    private String descrizione;
    @Column(name="datascadenza") @DateTimeFormat(pattern = "dd/MM/yyyy") @Future @NotNull
    private Date dataScadenza;
    @Column(name="fkcliente") @NotBlank
    private String fkCliente;
    @Column(name="avvisoda") @NotBlank
    private String avvisoDa;
    @Column(name="registratoda")
    private String registratoDa;
    @Column(name="data")
    private Date data;

    @PrePersist
    private void setData(){
        this.data = new Date();
    }


}
