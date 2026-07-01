package desi.DevNada.tp.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import desi.DevNada.tp.accesoDatos.IPropiedadRepo;
import desi.DevNada.tp.accesoDatos.IPublicacionRepo;
import desi.DevNada.tp.entidades.Propiedad;
import desi.DevNada.tp.entidades.Publicacion;


@Service
public class PublicacionService {

	@Autowired
    private IPublicacionRepo publicacionRepo; 

    @Autowired
    private IPropiedadRepo propiedadRepo;
   
    public List<Publicacion> obtenerTodas() {
        return publicacionRepo.findAll();
    }

    
    
public void guardarPublicacion(Publicacion publicacion) throws Exception {
        
        
        Propiedad propiedadReal = propiedadRepo.findById(publicacion.getPropiedad().getId())
                .orElseThrow(() -> new Exception("La propiedad seleccionada no existe en la base de datos."));       // 3. 
        publicacion.setPropiedad(propiedadReal);
        publicacionRepo.save(publicacion);
    }
  
    public Publicacion obtenerPorId(Long id) throws Exception {
        return publicacionRepo.findById(id)
                .orElseThrow(() -> new Exception("La publicación solicitada no existe."));
    }

    
    public void eliminar(Long id) {
        publicacionRepo.deleteById(id);
    }
}
