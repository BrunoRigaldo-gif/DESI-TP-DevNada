package desi.DevNada.tp.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import desi.DevNada.tp.entidades.Publicacion;
import desi.DevNada.tp.entidades.HistorialEstadoPublicacion; 
import desi.DevNada.tp.accesoDatos.IPublicacionRepo;
import desi.DevNada.tp.accesoDatos.IHistorialEstadoPublicacionRepo;

import java.util.List;
import java.time.LocalDate;

@Service
public class PublicacionServiceIMPL implements PublicacionService {

    @Autowired
    private IPublicacionRepo publicacionRepo;

    @Autowired
    private IHistorialEstadoPublicacionRepo historialRepo;

    @Override
    @Transactional
    public void guardarPublicacion(Publicacion publicacion) throws Exception {
        
        if (publicacion.getPrecioAlquiler() == null || publicacion.getPrecioAlquiler() <= 0) {
            throw new Exception("El precio mensual de alquiler debe ser un número positivo.");
        }

        if (publicacion.getEstadoPublicacion() == null || publicacion.getEstadoPublicacion().isEmpty()) {
            publicacion.setEstadoPublicacion("activa");
        }

        
        Publicacion publicacionGuardada = publicacionRepo.save(publicacion);

        
        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
        historial.setPublicacion(publicacionGuardada); 
        historial.setEstadoNuevo(publicacionGuardada.getEstadoPublicacion()); 
        historial.setFechaCambio(LocalDate.now()); 
        
        historialRepo.save(historial);
    }

    @Override
    public List<Publicacion> obtenerTodas() {
        return publicacionRepo.findAll();
    }

    @Override
    public Publicacion obtenerPorId(Long id) throws Exception {
        return publicacionRepo.findById(id)
                .orElseThrow(() -> new Exception("La publicación solicitada no existe."));
    }

    @Override
    public void eliminarPublicacion(Long id) {
        publicacionRepo.deleteById(id);
    }
}