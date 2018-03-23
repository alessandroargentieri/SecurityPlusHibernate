package it.impresaconsulting.Gestic.daos;

import it.impresaconsulting.Gestic.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteDao extends JpaRepository<Cliente, String> {
}

