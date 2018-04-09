package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.daos.DocumentoDao;
import it.impresaconsulting.Gestic.daos.PraticaDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import it.impresaconsulting.Gestic.entities.Pratica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired ClienteDao   clienteDao;
    @Autowired PraticaDao   praticaDao;
    @Autowired DocumentoDao documentoDao;

    @Override
    public Cliente updateCliente(String registratoDa, String oldId, Cliente cliente){
        cliente.setRegistratoDa(registratoDa);

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

        return clienteDao.save(cliente);
    }

    @Override
    public void deleteClienteAndRelativePraticheAndDocumentiByIdCliente(String idcliente){
        clienteDao.deleteById(idcliente);
        List<Pratica> pratiche = praticaDao.findByFkCliente(idcliente);
        for(Pratica pratica : pratiche){
            documentoDao.deleteDocumentiPerPratica(pratica.getIdPratica());
        }
        praticaDao.deletePraticaPerCliente(idcliente);
    }

    @Override
    public Cliente saveCliente(String registratoDa, Cliente cliente){
        cliente.setRegistratoDa(registratoDa);
        return clienteDao.save(cliente);
    }

    @Override
    public List<Cliente> findClienteByInteressatoA(String interessatoA){
        return clienteDao.findByInteressatoA(interessatoA);
    }

    @Override
    public List<Cliente> findClienteBySegnalatore(String segnalatore){
        if(segnalatore.contains("%20")){
            segnalatore = segnalatore.replaceAll("%20"," ");
        }
        String[] splited = segnalatore.split("\\s+");
        List<Cliente> result;
        if(splited.length == 2){
            result = clienteDao.findBySegnalatore(splited[0] + " " + splited[1]);
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[1] + " " + splited[0]);
            }
        }else if(splited.length == 3){
            result = clienteDao.findBySegnalatore(splited[0] + " " + splited[1] + " " + splited[2]);
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[0] + " " + splited[2] + " " + splited[1]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[1] + " " + splited[0] + " " + splited[2]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[1] + " " + splited[2] + " " + splited[0]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[2] + " " + splited[0] + " " + splited[1]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findBySegnalatore(splited[2] + " " + splited[1] + " " + splited[0]);
            }
        }else{
            result = clienteDao.findBySegnalatore(segnalatore);
        }
        return result;




    }

    @Override
    public List<Cliente> findClienteByAttivita(String attivita){
        return clienteDao.findByAttivita(attivita);
    }

    @Override
    public List<Cliente> findClienteByNominativo(String nominativo){
        if(nominativo.contains("%20")){
            nominativo = nominativo.replaceAll("%20"," ");
        }
        String[] splited = nominativo.split("\\s+");
        List<Cliente> result;
        if(splited.length == 2){
            result = clienteDao.findByNominativo(splited[0] + " " + splited[1]);
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[1] + " " + splited[0]);
            }
        }else if(splited.length == 3){
            result = clienteDao.findByNominativo(splited[0] + " " + splited[1] + " " + splited[2]);
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[0] + " " + splited[2] + " " + splited[1]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[1] + " " + splited[0] + " " + splited[2]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[1] + " " + splited[2] + " " + splited[0]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[2] + " " + splited[0] + " " + splited[1]);
            }
            if(result == null || result.size() == 0){
                result = clienteDao.findByNominativo(splited[2] + " " + splited[1] + " " + splited[0]);
            }
        }else{
            result = clienteDao.findByNominativo(nominativo);
        }
        return result;
    }

    @Override
    public List<Cliente> findClienteByRagioneSociale(String ragioneSociale){
        return clienteDao.findByRagioneSociale(ragioneSociale);
    }

    @Override
    public List<Cliente> findClienteByIdWithAPseudoList(String idCliente){
        List<Cliente> pseudoList = new ArrayList<>();
        Optional<Cliente> clienteOptional = clienteDao.findById(idCliente);
        if(clienteOptional.isPresent()){
            pseudoList.add(clienteOptional.get());
        }
        return pseudoList;
    }

    @Override
    public Cliente findClienteById(String idCliente){
        Optional<Cliente> clienteOptional = clienteDao.findById(idCliente);
        if(clienteOptional.isPresent()){
            return clienteOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Cliente> getAll(){
        return clienteDao.findAll();
    }

}
