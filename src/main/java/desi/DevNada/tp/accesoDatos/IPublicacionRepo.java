package desi.DevNada.tp.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Publicacion;

@Repository
public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {

    // Spring Boot lee el nombre de este método y crea la query SQL automáticamente
    boolean existsByPropiedadIdAndEstadoPublicacion(Long propiedadId, String estadoPublicacion);

}