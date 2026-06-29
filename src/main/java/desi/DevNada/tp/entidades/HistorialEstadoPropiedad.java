package desi.DevNada.tp.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class HistorialEstadoPropiedad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Propiedad a la que pertenece este registro de historial.
	@ManyToOne
	private Propiedad propiedad;

	// Estado que quedó registrado en este cambio.
	@NotNull
	private String estado;
	
	// Momento en que se produjo el cambio de estado.
	@NotNull
	private LocalDateTime fechaCambio;
	
	
	
	public HistorialEstadoPropiedad(Propiedad propiedad, @NotNull String estado,
			@NotNull LocalDateTime fechaCambio) {
		this.propiedad = propiedad;
		this.estado = estado;
		this.fechaCambio = fechaCambio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
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
