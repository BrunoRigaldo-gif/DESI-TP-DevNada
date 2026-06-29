package desi.DevNada.tp.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "La fecha de inicio es obligatoria")
	private LocalDate fechaInicio;
	
	// Duración del contrato expresada en meses (debe ser un número positivo).
	@NotNull(message = "La duración en meses es obligatoria")
	@Positive(message = "La duración en meses debe ser un número positivo")
	private Integer duracionMeses;
	
	// Importe mensual del alquiler (debe ser un número positivo).
	@NotNull(message = "El importe mensual es obligatorio")
	@Positive(message = "El importe mensual debe ser un número positivo")
	private BigDecimal importeMensual;
	
	// Día del mes en que vence la cuota (entre 1 y 31).
	@NotNull(message = "El día de vencimiento es obligatorio")
	@Min(value = 1, message = "El día de vencimiento debe estar entre 1 y 31")
	@Max(value = 31, message = "El día de vencimiento debe estar entre 1 y 31")
	private Integer diaVencimientoMensual;
	
	@NotBlank(message = "La descripción es obligatoria")
	private String descripcion;
	
	private String EstadoContrato; //No estoy seguro del tipo
	
	private Boolean eliminado;

	
	
	// Propiedad alquilada en este contrato.
	@ManyToOne
	@NotNull(message = "Debe seleccionar una propiedad asociada")
	private Propiedad propiedad;

	// Inquilino (persona) que alquila la propiedad.
	@ManyToOne
	@NotNull(message = "Debe seleccionar un inquilino")
	private Persona inquilino;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstadoContrato() {
		return EstadoContrato;
	}

	public void setEstadoContrato(String estadoContrato) {
		EstadoContrato = estadoContrato;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}
	
	
}
