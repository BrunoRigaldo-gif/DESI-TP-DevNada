package desi.DevNada.tp.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// Registro histórico de los cambios de estado de un contrato. Por cada cambio
// (incluida el alta) se guarda una fila con el estado y la fecha/hora del cambio.
@Entity
public class HistorialEstadoContrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Contrato al que pertenece este registro de historial.
	@ManyToOne
	private Contrato contrato;

	// Estado que quedó registrado en este cambio.
	private String estado;

	// Momento en que se produjo el cambio de estado.
	private LocalDateTime fechaCambio;

	public HistorialEstadoContrato() {
	}

	public HistorialEstadoContrato(Contrato contrato, String estado, LocalDateTime fechaCambio) {
		this.contrato = contrato;
		this.estado = estado;
		this.fechaCambio = fechaCambio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(LocalDateTime fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
}
