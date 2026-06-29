package desi.DevNada.tp.servicios;

import java.util.List;
import desi.DevNada.tp.entidades.Propiedad;


public interface PropiedadService {
	
	public void guardar(Propiedad p);
	
	public List<Propiedad> listarActivas();
	
	public List<Propiedad> listarTodas();
	
	public Propiedad listarPorID(Long id);
	
	public List<Propiedad> listarPorDireccion(String dir);
	
	public List<Propiedad> listarPorCiudad(String nombre);
	
	public List<Propiedad> listarPorTipo(String tipo);
	
	public List<Propiedad> listarPorEstado(String estado);
	
	public void modificar(Propiedad propiedad);
	
	public void eliminar(Long id) ;

	
}
