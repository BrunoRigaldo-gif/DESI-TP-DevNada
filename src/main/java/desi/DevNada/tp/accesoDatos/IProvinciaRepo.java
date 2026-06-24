package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Provincia;

@Repository
public interface IProvinciaRepo extends JpaRepository<Provincia, Long> {

	List<Provincia> findByNombre(String nombre);

	List<Provincia> findByNombreAndIdNot(String nombre,Long idDistintoDe);
	
}
