package desi.DevNada.tp.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import desi.DevNada.tp.accesoDatos.ICiudadRepo;
import desi.DevNada.tp.entidades.Ciudad;

@Service
public class CiudadServiceIMPL implements CiudadService{
	
	@Autowired
	private ICiudadRepo repo;
	@Override
	public List<Ciudad> listarTodas() {
		return repo.findAll();
	}

}
