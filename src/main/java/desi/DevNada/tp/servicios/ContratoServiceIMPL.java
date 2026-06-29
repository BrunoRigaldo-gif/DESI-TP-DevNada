package desi.DevNada.tp.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desi.DevNada.tp.accesoDatos.IContratoRepo;
import desi.DevNada.tp.accesoDatos.IHistorialEstadoContratoRepo;
import desi.DevNada.tp.accesoDatos.IPropiedadRepo;
import desi.DevNada.tp.entidades.Contrato;
import desi.DevNada.tp.entidades.HistorialEstadoContrato;
import desi.DevNada.tp.entidades.Propiedad;

import jakarta.transaction.Transactional;

@Service
public class ContratoServiceIMPL implements ContratoService {

	@Autowired
	private IContratoRepo contratoRepo;

	@Autowired
	private IPropiedadRepo propiedadRepo;

	@Autowired
	private IHistorialEstadoContratoRepo historialRepo;

	@Override
	public List<Contrato> listarActivos() {
		return contratoRepo.findByEliminadoFalse();
	}

	@Override
	public Contrato obtenerPorId(Long id) {
		return contratoRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("No se encontró el contrato"));
	}

	@Override
	@Transactional
	public void guardar(Contrato contrato) {

		validarDatos(contrato);

		// Estado por defecto: el contrato nace como "borrador"
		if (contrato.getEstadoContrato() == null || contrato.getEstadoContrato().isBlank()) {
			contrato.setEstadoContrato("borrador");
		}
		String nuevoEstado = contrato.getEstadoContrato();

		// Se carga la propiedad gestionada para validar y, si corresponde, cambiar
		// su disponibilidad
		Propiedad propiedad = propiedadRepo.findById(contrato.getPropiedad().getId())
				.orElseThrow(() -> new RuntimeException("Debe seleccionar una propiedad"));
		contrato.setPropiedad(propiedad);

		// Estado anterior: permite detectar el cambio y las transiciones de la propiedad
		String estadoAnterior = null;
		if (contrato.getId() != null) {
			estadoAnterior = contratoRepo.findById(contrato.getId())
					.map(Contrato::getEstadoContrato).orElse(null);
		}

		// Reglas de negocio que se controlan al dejar el contrato en estado "activo"
		if ("activo".equalsIgnoreCase(nuevoEstado)) {
			validarActivacion(contrato, propiedad, estadoAnterior);
		}

		// La eliminación lógica queda apagada al guardar
		contrato.setEliminado(false);

		contratoRepo.save(contrato); // asigna el id en el alta

		// Historial: se registra cada cambio de estado (incluido el alta)
		if (estadoAnterior == null || !estadoAnterior.equalsIgnoreCase(nuevoEstado)) {
			historialRepo.save(new HistorialEstadoContrato(contrato, nuevoEstado, LocalDateTime.now()));
		}

		// Efecto sobre la propiedad según el estado resultante
		if ("activo".equalsIgnoreCase(nuevoEstado)) {
			// Al activar un contrato, la propiedad pasa a "Alquilada"
			propiedad.setEstadoDisponibilidad("Alquilada");
			propiedadRepo.save(propiedad);
		} else if ("activo".equalsIgnoreCase(estadoAnterior)) {
			// Si el contrato deja de estar activo, se libera la propiedad
			propiedad.setEstadoDisponibilidad("Disponible");
			propiedadRepo.save(propiedad);
		}
	}

	@Override
	public void eliminar(Long id) {
		Contrato contrato = obtenerPorId(id);
		// Eliminación lógica, igual que el resto de las entidades del proyecto
		contrato.setEliminado(true);
		contrato.setEstadoContrato("borrador");
		contratoRepo.save(contrato);
	}

	// Reglas de negocio que deben cumplirse para dejar un contrato en estado "activo"
	private void validarActivacion(Contrato contrato, Propiedad propiedad, String estadoAnterior) {

		// 1) Una propiedad no puede tener más de un contrato activo
		for (Contrato c : contratoRepo.buscarPorPropiedad(propiedad.getId())) {
			boolean esOtroContrato = !c.getId().equals(contrato.getId());
			if (esOtroContrato && "activo".equalsIgnoreCase(c.getEstadoContrato())) {
				throw new RuntimeException("La propiedad ya posee un contrato activo");
			}
		}

		// 2) No se puede activar si la propiedad no está disponible
		//    (se admite si ya está "Alquilada" por este mismo contrato)
		boolean yaEstabaActivo = "activo".equalsIgnoreCase(estadoAnterior);
		boolean disponible = "Disponible".equalsIgnoreCase(propiedad.getEstadoDisponibilidad());
		if (!disponible && !yaEstabaActivo) {
			throw new RuntimeException("No se puede activar el contrato: la propiedad no está disponible");
		}
	}

	// Validaciones de llenado: todos los datos del contrato son obligatorios
	private void validarDatos(Contrato contrato) {

		if (contrato.getPropiedad() == null || contrato.getPropiedad().getId() == null) {
			throw new RuntimeException("Debe seleccionar una propiedad");
		}

		if (contrato.getInquilino() == null || contrato.getInquilino().getId() == null) {
			throw new RuntimeException("Debe seleccionar un inquilino");
		}

		if (contrato.getFechaInicio() == null) {
			throw new RuntimeException("La fecha de inicio es obligatoria");
		}

		if (contrato.getDuracionMeses() == null || contrato.getDuracionMeses() <= 0) {
			throw new RuntimeException("La duración en meses debe ser un número positivo");
		}

		if (contrato.getImporteMensual() == null
				|| contrato.getImporteMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El importe mensual debe ser un número positivo");
		}

		if (contrato.getDiaVencimientoMensual() == null
				|| contrato.getDiaVencimientoMensual() < 1
				|| contrato.getDiaVencimientoMensual() > 31) {
			throw new RuntimeException("El día de vencimiento mensual debe ser un número entre 1 y 31");
		}

		if (contrato.getDescripcion() == null || contrato.getDescripcion().isBlank()) {
			throw new RuntimeException("La descripción es obligatoria");
		}
	}
}
