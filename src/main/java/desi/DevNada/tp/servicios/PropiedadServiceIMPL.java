package desi.DevNada.tp.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import desi.DevNada.tp.accesoDatos.IPropiedadRepo;
import desi.DevNada.tp.entidades.Propiedad;


@Service
public class PropiedadServiceIMPL implements PropiedadService{
	@Autowired
	private IPropiedadRepo repo;
	
	public Propiedad guardar(Propiedad p) {
		return repo.save(p);
	}
	
	public List<Propiedad> listarActivas(){
		return repo.findByEliminadaFalse();
	}
	public List<Propiedad> listarPorDireccion(String dir){
		return repo.findByDireccionContainingIgnoreCaseAndEliminadaFalse(dir);
	}
	public List<Propiedad> listarPorCiudad(long id){
		return repo.findByCiudad_IdAndEliminadaFalse(id);
	}

	public List<Propiedad> listarPorTipo(String tipo){
		return repo.findByTipoPropiedadContainingIgnoreCaseAndEliminadaFalse(tipo);
	}
	public List<Propiedad> listarPorEstado(String estado){
		return repo.findByEstadoDisponibilidadContainingIgnoringCaseAndEliminadaFalse(estado);
	}
	public void eliminar(Long id) {
		Propiedad p = repo.findById(id).orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: "+id));
		p.setEliminada(true);
		repo.save(p);
	}

}
