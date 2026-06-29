package desi.DevNada.tp.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estadoFactura;

    private LocalDateTime fechaHora;

    @ManyToOne
    private Factura factura;

    public HistorialEstadoFactura() {
    }

    public HistorialEstadoFactura(Factura factura, String estadoFactura) {
        this.factura = factura;
        this.estadoFactura = estadoFactura;
        this.fechaHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
