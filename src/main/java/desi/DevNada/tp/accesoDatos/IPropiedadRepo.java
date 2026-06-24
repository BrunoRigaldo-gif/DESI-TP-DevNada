package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Propiedad;


@Repository
public interface IPropiedadRepo extends JpaRepository<Propiedad, Long>{
	List<Propiedad> findByEliminadaFalse();
	List<Propiedad> findByDireccionContainingIgnoreCaseAndEliminadaFalse(String direccion);
	
	List<Propiedad> findByCiudad_IdAndEliminadaFalse(Long ciudadId);
	
	List<Propiedad> findByTipoPropiedadContainingIgnoreCaseAndEliminadaFalse(String tipo);
	
	List<Propiedad> findByEstadoDisponibilidadContainingIgnoringCaseAndEliminadaFalse(String estado);
}
