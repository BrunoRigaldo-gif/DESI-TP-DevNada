package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import desi.DevNada.tp.entidades.HistorialEstadoContrato;

public interface IHistorialEstadoContratoRepo extends JpaRepository<HistorialEstadoContrato, Long> {

	// Historial de un contrato ordenado por fecha de cambio.
	List<HistorialEstadoContrato> findByContrato_IdOrderByFechaCambioAsc(Long contratoId);
}
