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
        return clienteDao.findBySegnalatore(segnalatore);
    }

    @Override
    public List<Cliente> findClienteByAttivita(String attivita){
        return clienteDao.findByAttivita(attivita);
    }

    @Override
    public List<Cliente> findClienteByNominativo(String nominativo){
        return clienteDao.findByNominativo(nominativo);
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
