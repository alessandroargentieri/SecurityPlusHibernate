package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.entities.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente updateCliente(String registratoDa, String oldId, Cliente cliente);
    void deleteClienteAndRelativePraticheAndDocumentiByIdCliente(String id);
    Cliente saveCliente(String registratoDa, Cliente cliente);
    List<Cliente> findClienteByInteressatoA(String interessatoA);
    List<Cliente> findClienteBySegnalatore(String segnalatore);
    List<Cliente> findClienteByAttivita(String attivita);
    List<Cliente> findClienteByNominativo(String nominativo);
    List<Cliente> findClienteByRagioneSociale(String ragioneSociale);
    List<Cliente> findClienteByIdWithAPseudoList(String idCliente);
    Cliente findClienteById(String idCliente);
    List<Cliente> getAll();
}
