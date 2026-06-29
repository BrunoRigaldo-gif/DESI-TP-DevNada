package desi.DevNada.tp.servicios;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import desi.DevNada.tp.accesoDatos.IHistorialEstadoPropiedadRepo;
import desi.DevNada.tp.accesoDatos.IPropiedadRepo;
import desi.DevNada.tp.entidades.Ciudad;
import desi.DevNada.tp.entidades.HistorialEstadoPropiedad;
import desi.DevNada.tp.entidades.Propiedad;
import jakarta.transaction.Transactional;

@Service
public class PropiedadServiceIMPL implements PropiedadService {
	@Autowired
	private IPropiedadRepo repo;
	@Autowired
	private IHistorialEstadoPropiedadRepo historialRepo;

	@Override
	public void guardar(Propiedad p) {
		// Toda Propiedad inicia con el estado Disponible
		p.setEstadoDisponibilidad("Disponible");

		// Comprobar que no exista otra propiedad con la misma direccion en la misma
		// ciudad
		boolean confirmacion = repo.existsByDireccionContainingIgnoreCaseAndCiudadAndEliminadaFalse(p.getDireccion(),
				p.getCiudad());
		if (confirmacion) {
			throw new RuntimeException("Ya existe una propiedad activa con esa direccion y ciudad");
		}
		repo.save(p);
		historialRepo.save(new HistorialEstadoPropiedad(p, p.getEstadoDisponibilidad(), LocalDateTime.now()));
	}

	@Override
	public List<Propiedad> listarActivas() {
		return repo.findByEliminadaFalse();
	}

	@Override
	public List<Propiedad> listarTodas() {
		return repo.findAll();
	}

	@Override
	public Propiedad listarPorID(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("No se encontró la Propiedad"));
	}

	@Override
	public List<Propiedad> listarPorDireccion(String dir) {
		return repo.findByDireccionContainingIgnoreCaseAndEliminadaFalse(dir);
	}

	@Override
	public List<Propiedad> listarPorCiudad(long id) {
		return repo.findByCiudad_IdAndEliminadaFalse(id);
	}

	@Override
	public List<Propiedad> listarPorTipo(String tipo) {
		return repo.findByTipoPropiedadContainingIgnoreCaseAndEliminadaFalse(tipo);
	}

	@Override
	public List<Propiedad> listarPorEstado(String estado) {
		return repo.findByEstadoDisponibilidadContainingIgnoringCaseAndEliminadaFalse(estado);
	}

	@Override
	public void modificar(Propiedad p) {
		;
		boolean confirmacion = repo.existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalseAndIdNot(p.getDireccion(),
				p.getCiudad(), p.getId());
		if (confirmacion) {
			throw new RuntimeException("Ya existe una propiedad activa con esa direccion y ciudad");
		}
		actualizacion(p.getId(), p.getDireccion(), p.getCiudad(), p.getTipoPropiedad(), p.getCantidadAmbientes(),
				p.getMetrosCuadrados(), p.getComodidades(), p.getEstadoDisponibilidad());

	}

	@Transactional
	public void actualizacion(Long id, String direccion, Ciudad ciudad, String tipoPropiedad, Integer cantAmbientes,
			Double metrosCuadrados, String Comodidades, String estado) {
		Propiedad propiedadAct = repo.findById(id).orElseThrow();
		String estadoAntiguo = propiedadAct.getEstadoDisponibilidad();
		propiedadAct.setCantidadAmbientes(cantAmbientes);
		propiedadAct.setCiudad(ciudad);
		propiedadAct.setEstadoDisponibilidad(estado);
		propiedadAct.setComodidades(Comodidades);
		propiedadAct.setMetrosCuadrados(metrosCuadrados);
		propiedadAct.setTipoPropiedad(tipoPropiedad);
		propiedadAct.setDireccion(direccion);
		propiedadAct.setEliminada(false);
		if (!estadoAntiguo.equals(estado)) {
			historialRepo.save(new HistorialEstadoPropiedad(propiedadAct, propiedadAct.getEstadoDisponibilidad(), LocalDateTime.now()));
		}
		repo.save(propiedadAct);
	}

	@Override
	public void eliminar(Long id) {
		Propiedad p = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
		if (p.getContrato().getEstadoContrato().equalsIgnoreCase("activo")) {
			throw new RuntimeException("¡No se puede eliminar esta propiedad debido a que tiene un contrato activo!");
		} else
		p.setEliminada(true);
		repo.save(p);
	}

}
