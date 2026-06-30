package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import desi.DevNada.tp.entidades.Contrato;

public interface IContratoRepo extends JpaRepository<Contrato, Long> {

	// Lista los contratos que no fueron dados de baja (eliminación lógica).
	List<Contrato> findByEliminadoFalse();

	// Devuelve los contratos no eliminados de una propiedad. El filtro por estado
	// se resuelve en la capa de servicios para no depender del nombre del atributo
	// en la consulta JPQL.
	@Query("SELECT c FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.eliminado = false")
	List<Contrato> buscarPorPropiedad(@Param("propiedadId") Long propiedadId);

	// Devuelve los contratos activos no eliminados (para el formulario de Factura).
	List<Contrato> findByEliminadoFalseAndEstadoContratoIgnoreCase(String estadoContrato);
}
