package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import desi.DevNada.tp.entidades.HistorialEstadoPropiedad;

@Repository
public interface IHistorialEstadoPropiedadRepo extends JpaRepository<HistorialEstadoPropiedad, Long> {

	// Historial de una propiedad ordenado por fecha de cambio.
	List<HistorialEstadoPropiedad> findByPropiedad_IdOrderByFechaCambioAsc(Long id);
	boolean existsByPropiedad_IdOrderByFechaCambioAsc(Long id);
}
