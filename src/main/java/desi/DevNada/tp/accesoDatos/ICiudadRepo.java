package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Ciudad;


@Repository
public interface ICiudadRepo extends JpaRepository<Ciudad, Long> {
	

}
