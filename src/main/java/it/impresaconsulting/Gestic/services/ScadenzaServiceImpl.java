package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.ScadenzaDao;
import it.impresaconsulting.Gestic.entities.Scadenza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScadenzaServiceImpl implements ScadenzaService{

    @Autowired ScadenzaDao scadenzaDao;

    @Override
    public List<Scadenza> getAllScadenze(){
        return scadenzaDao.findAll();
    }

    @Override
    public List<Scadenza> getScadenze(){
        Date now = new Date();
        List<Scadenza> scadenze = scadenzaDao.findAll();
        List<Scadenza> tabellaScadenze = new ArrayList<>();
        for(Scadenza s: scadenze){
            if(Scadenza.DIECI_GIORNI.equals(s.getAvvisoDa())){
                if(getDifferenceDays(now, s.getDataScadenza()) <= 10){
                    tabellaScadenze.add(s);
                }
            }else if(Scadenza.CINQUE_GIORNI.equals(s.getAvvisoDa())){
                if(getDifferenceDays(now, s.getDataScadenza()) <= 5){
                    tabellaScadenze.add(s);
                }
            }else if(Scadenza.GIORNO_PRIMA.equals(s.getAvvisoDa())){
                if(getDifferenceDays(now, s.getDataScadenza()) <= 1){
                    tabellaScadenze.add(s);
                }
            }else if(Scadenza.GIORNO_STESSO.equals(s.getAvvisoDa())){
                if(getDifferenceDays(now, s.getDataScadenza()) < 1){
                    tabellaScadenze.add(s);
                }
            }
        }
        return tabellaScadenze;
    }

    @Override
    public void deleteOldScadenze(){
        Calendar now = Calendar.getInstance();
        List<Scadenza> scadenze = scadenzaDao.findAll();
        for(Scadenza s: scadenze){
            if(now.getTimeInMillis() - s.getDataScadenza().getTime() > 0){
                scadenzaDao.delete(s);
            }
        }
    }

    @Override
    public void deleteScandenzaById(Integer id){
        scadenzaDao.deleteById(id);
    }

    @Override
    public Scadenza salvaScadenza(String registratoDa, Scadenza scadenza){
        scadenza.setRegistratoDa(registratoDa);
        return scadenzaDao.save(scadenza);
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(d1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timenow = cal.getTimeInMillis();
        long diff = d2.getTime() - timenow;
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

}
