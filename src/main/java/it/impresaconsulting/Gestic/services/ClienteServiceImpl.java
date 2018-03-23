package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.ClienteDao;
import it.impresaconsulting.Gestic.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired ClienteDao clienteDao;

    @Override
    public Cliente updateCliente(Cliente cliente){
        clienteDao.deleteById(cliente.getIdCliente());
        return clienteDao.save(cliente);
    }

}
