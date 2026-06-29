package desi.DevNada.tp.servicios;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desi.DevNada.tp.accesoDatos.IFacturaRepo;
import desi.DevNada.tp.entidades.Factura;

import desi.DevNada.tp.accesoDatos.IHistorialEstadoFacturaRepo;
import desi.DevNada.tp.entidades.HistorialEstadoFactura;

@Service
public class FacturaServiceIMPL implements FacturaService {

    @Autowired
    private IFacturaRepo facturaRepo;
    
    @Autowired
    private IHistorialEstadoFacturaRepo historialRepo;

    @Override
    public List<Factura> listar() {
        return facturaRepo.findByEliminadaFalse();
    }

    @Override
    public Factura buscarPorId(Long id) {
        return facturaRepo.findById(id).orElse(null);
    }

 
    @Override
    public Factura guardar(Factura factura) {
        validarFactura(factura);

        factura.setEstadoFactura("pendiente");
        factura.setEliminada(false);

        Factura guardada = facturaRepo.save(factura);

        historialRepo.save(new HistorialEstadoFactura(guardada, guardada.getEstadoFactura()));

        return guardada;
    }

    @Override
    public Factura modificar(Long id, Factura datosNuevos) {
        Factura factura = buscarPorId(id);

        if (factura == null) {
            throw new RuntimeException("La factura no existe");
        }

        if ("pagada".equalsIgnoreCase(factura.getEstadoFactura())) {
            throw new RuntimeException("No se puede modificar una factura pagada");
        }

        if ("anulada".equalsIgnoreCase(factura.getEstadoFactura())) {
            throw new RuntimeException("No se puede modificar una factura anulada");
        }

        String estadoAnterior = factura.getEstadoFactura();

        factura.setConceptoFacturado(datosNuevos.getConceptoFacturado());
        factura.setFechaEmision(datosNuevos.getFechaEmision());
        factura.setFechaVencimiento(datosNuevos.getFechaVencimiento());
        factura.setImporte(datosNuevos.getImporte());
        factura.setEstadoFactura(datosNuevos.getEstadoFactura());

        validarFactura(factura);

        Factura modificada = facturaRepo.save(factura);

        if (!estadoAnterior.equalsIgnoreCase(modificada.getEstadoFactura())) {
            historialRepo.save(new HistorialEstadoFactura(modificada, modificada.getEstadoFactura()));
        }

        return modificada;
    }

    @Override
    public void eliminar(Long id) {
        Factura factura = buscarPorId(id);

        if (factura == null) {
            throw new RuntimeException("La factura no existe");
        }

        if ("pagada".equalsIgnoreCase(factura.getEstadoFactura())) {
            throw new RuntimeException("No se puede eliminar una factura pagada");
        }

        factura.setEliminada(true);
        facturaRepo.save(factura);
    }

    private void validarFactura(Factura factura) {
        if (factura.getContrato() == null) {
            throw new RuntimeException("Debe seleccionar un contrato");
        }

        if (factura.getContrato().getEliminado() != null && factura.getContrato().getEliminado()) {
            throw new RuntimeException("No se puede facturar un contrato eliminado");
        }

        if (!"activo".equalsIgnoreCase(factura.getContrato().getEstadoContrato())) {
            throw new RuntimeException("Solo se puede facturar un contrato activo");
        }

        if (factura.getConceptoFacturado() == null || factura.getConceptoFacturado().isBlank()) {
            throw new RuntimeException("Debe ingresar el concepto facturado");
        }

        if (factura.getFechaEmision() == null) {
            throw new RuntimeException("Debe ingresar la fecha de emisión");
        }

        if (factura.getFechaVencimiento() == null) {
            throw new RuntimeException("Debe ingresar la fecha de vencimiento");
        }

        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new RuntimeException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión");
        }

        if (factura.getImporte() == null || factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El importe debe ser positivo");
        }

        if (factura.getEstadoFactura() == null || factura.getEstadoFactura().isBlank()) {
            factura.setEstadoFactura("pendiente");
        }
    }
}
