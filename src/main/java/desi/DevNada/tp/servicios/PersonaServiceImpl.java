package desi.DevNada.tp.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desi.DevNada.tp.accesoDatos.IPersonaRepo;
import desi.DevNada.tp.entidades.Persona;


@Service
public class PersonaServiceImpl implements PersonaService {

	@Autowired
	IPersonaRepo repo;
	
	@Override
	public List<Persona> getAll() {
		return repo.findAll();
	}

}
