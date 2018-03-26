package it.impresaconsulting.Gestic.utilities;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {


    //attribute from jasypt library
    private BasicTextEncryptor textEncryptor;


    /* constructor */
    public EncryptionUtils(){
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("GesticEncryptionSystem");
    }

    public String encrypt(String data){
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String encriptedData){
        return textEncryptor.decrypt(encriptedData);
    }

}