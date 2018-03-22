package it.impresaconsulting.Gestic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity @Table(name="utenti")
@AllArgsConstructor @NoArgsConstructor @Data
public class Utente {

    @Id @Column(name="codicefiscale") @NotBlank
    private String codiceFiscale;
    @Column(name="nominativo") @NotBlank
    private String nominativo;
    @Column(name="indirizzo")
    private String indirizzo;
    @Column(name="natoil")
    private Date natoIl;
    @Column(name="ruolo")
    private String ruolo; //USER_ROLE  / ADMIN_ROLE
    @Column(name="password")
    private String password;

    @PrePersist
    private void setRole(){
        if(ruolo == null){
            ruolo="USER_ROLE";
        }
    }
}
