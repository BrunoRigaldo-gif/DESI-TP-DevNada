package desi.DevNada.tp.servicios;

import java.util.List;
import desi.DevNada.tp.entidades.Propiedad;


public interface PropiedadService {
	
	public Propiedad guardar(Propiedad p);
	
	public List<Propiedad> listarActivas();
	
	public List<Propiedad> listarTodas();
	
	public List<Propiedad> listarPorDireccion(String dir);
	
	public List<Propiedad> listarPorCiudad(long id);
	
	public List<Propiedad> listarPorTipo(String tipo);
	
	public List<Propiedad> listarPorEstado(String estado);
	
	public void eliminar(Long id) ;

	
}
