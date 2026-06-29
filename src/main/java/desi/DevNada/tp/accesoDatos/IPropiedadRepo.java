package desi.DevNada.tp.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Propiedad;
import desi.DevNada.tp.entidades.Ciudad;

@Repository
public interface IPropiedadRepo extends JpaRepository<Propiedad, Long> {
	List<Propiedad> findByEliminadaFalse();

	List<Propiedad> findByDireccionIgnoreCaseAndEliminadaFalse(String direccion);

	List<Propiedad> findByCiudad_NombreAndEliminadaFalse(String nombre);

	List<Propiedad> findByTipoPropiedadIgnoreCaseAndEliminadaFalse(String tipo);

	List<Propiedad> findByEstadoDisponibilidadIgnoringCaseAndEliminadaFalse(String estado);

	boolean existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalse(String direccion, Ciudad ciudad);

	boolean existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalseAndIdNot(String direccion, Ciudad ciudad, Long id);

}
