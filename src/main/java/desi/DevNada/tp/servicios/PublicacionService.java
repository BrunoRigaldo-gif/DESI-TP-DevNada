package desi.DevNada.tp.servicios;
import desi.DevNada.tp.entidades.Publicacion;
import java.util.List;
public interface PublicacionService {
    void guardarPublicacion(Publicacion publicacion) throws Exception;
    List<Publicacion> obtenerTodas();
    Publicacion obtenerPorId(Long id) throws Exception;
    void eliminarPublicacion(Long id) throws Exception;
}