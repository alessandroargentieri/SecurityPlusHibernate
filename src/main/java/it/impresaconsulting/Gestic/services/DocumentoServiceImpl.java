package it.impresaconsulting.Gestic.services;

import it.impresaconsulting.Gestic.daos.DocumentoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired DocumentoDao documentoDao;
}
