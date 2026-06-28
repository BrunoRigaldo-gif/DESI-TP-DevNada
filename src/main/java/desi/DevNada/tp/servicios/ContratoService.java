package desi.DevNada.tp.servicios;

import java.util.List;
import desi.DevNada.tp.entidades.Contrato;

public interface ContratoService {

	List<Contrato> listarActivos();

	Contrato obtenerPorId(Long id);

	void guardar(Contrato contrato);

	void eliminar(Long id);
}
