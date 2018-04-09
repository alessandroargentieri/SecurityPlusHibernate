package it.impresaconsulting.Gestic.entities;

import it.impresaconsulting.Gestic.utilities.EncryptionUtils;
import it.impresaconsulting.Gestic.utilities.SecurityImpl;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity @Table(name="utenti")
@NoArgsConstructor @Data
public class Utente {

    @Autowired @Transient EncryptionUtils encryptionUtils;

    @Id @Column(name="codicefiscale") @NotBlank
    private String codiceFiscale;
    @Column(name="nominativo") @NotBlank
    private String nominativo;
    @Column(name="indirizzo")
    private String indirizzo;
    @Column(name="telefono")
    private String telefono;
    @Column(name="email") @Email
    private String email;
    @Column(name="natoil") @Past @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date natoIl;
    @Column(name="ruolo")
    private String ruolo;               //USER_ROLE  / ADMIN_ROLE
    @Column(name="password")
    private String password;

    public Utente(@NotBlank String codiceFiscale, @NotBlank String nominativo, String indirizzo, String telefono, String email, @Past Date natoIl, String ruolo, @NotBlank String password) {
        this.codiceFiscale = codiceFiscale;
        this.nominativo = nominativo;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.natoIl = natoIl;
        this.ruolo = ruolo;
        this.password = password;
    }

    @PrePersist
    private void setRole(){
        if(ruolo == null){
           ruolo = SecurityImpl.ROLE_USER;
        }
        if(password == null){
           encryptionUtils.encrypt("1234");
        }
    }
}
