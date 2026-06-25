package desi.DevNada.tp.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


	@Entity//avisa que esto sera una tabla
	public class Factura {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    
	    @ManyToOne
	    private Contrato contrato; 

	    private String conceptoFacturado;

	    private LocalDate fechaEmision;

	    private LocalDate fechaVencimiento;

	    private BigDecimal importe;

	    // Estados: pendiente, pagada, vencida, anulada
	    private String estadoFactura;

	    // Eliminación lógica
	    private Boolean eliminada;

	    // Datos de pago
	    private String medioPago;

	    private LocalDate fechaPago;

	    private BigDecimal importePagado;

	    private BigDecimal interes;

	    public Long getId() {
	        return id;
	    }

	    public Contrato getContrato() {
	        return contrato;
	    }

	    public void setContrato(Contrato contrato) {
	        this.contrato = contrato;
	       
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getConceptoFacturado() {
	        return conceptoFacturado;
	    }

	    public void setConceptoFacturado(String conceptoFacturado) {
	        this.conceptoFacturado = conceptoFacturado;
	    }

	    public LocalDate getFechaEmision() {
	        return fechaEmision;
	    }

	    public void setFechaEmision(LocalDate fechaEmision) {
	        this.fechaEmision = fechaEmision;
	    }

	    public LocalDate getFechaVencimiento() {
	        return fechaVencimiento;
	    }

	    public void setFechaVencimiento(LocalDate fechaVencimiento) {
	        this.fechaVencimiento = fechaVencimiento;
	    }

	    public BigDecimal getImporte() {
	        return importe;
	    }

	    public void setImporte(BigDecimal importe) {
	        this.importe = importe;
	    }

	    public String getEstadoFactura() {
	        return estadoFactura;
	    }

	    public void setEstadoFactura(String estadoFactura) {
	        this.estadoFactura = estadoFactura;
	    }

	    public Boolean getEliminada() {
	        return eliminada;
	    }

	    public void setEliminada(Boolean eliminada) {
	        this.eliminada = eliminada;
	    }

	    public String getMedioPago() {
	        return medioPago;
	    }

	    public void setMedioPago(String medioPago) {
	        this.medioPago = medioPago;
	    }

	    public LocalDate getFechaPago() {
	        return fechaPago;
	    }

	    public void setFechaPago(LocalDate fechaPago) {
	        this.fechaPago = fechaPago;
	    }

	    public BigDecimal getImportePagado() {
	        return importePagado;
	    }

	    public void setImportePagado(BigDecimal importePagado) {
	        this.importePagado = importePagado;
	    }

	    public BigDecimal getInteres() {
	        return interes;
	    }

	    public void setInteres(BigDecimal interes) {
	        this.interes = interes;
	    }
	}

