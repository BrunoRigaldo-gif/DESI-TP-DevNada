package desi.DevNada.tp.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desi.DevNada.tp.accesoDatos.IFacturaRepo;
import desi.DevNada.tp.accesoDatos.IHistorialEstadoFacturaRepo;
import desi.DevNada.tp.entidades.Factura;
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
        String estadoNuevo = datosNuevos.getEstadoFactura();

        validarCambioEstado(estadoAnterior, estadoNuevo);

        factura.setConceptoFacturado(datosNuevos.getConceptoFacturado());
        factura.setFechaEmision(datosNuevos.getFechaEmision());
        factura.setFechaVencimiento(datosNuevos.getFechaVencimiento());
        factura.setImporte(datosNuevos.getImporte());
        factura.setEstadoFactura(estadoNuevo);

        factura.setFechaPago(datosNuevos.getFechaPago());
        factura.setMedioPago(datosNuevos.getMedioPago());
        factura.setImportePagado(datosNuevos.getImportePagado());
        factura.setInteres(datosNuevos.getInteres());

        validarFactura(factura);

        Factura modificada = facturaRepo.save(factura);

        if (!estadoAnterior.equalsIgnoreCase(estadoNuevo)) {
            historialRepo.save(new HistorialEstadoFactura(modificada, estadoNuevo));
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

    @Override
    public List<Factura> filtrarFacturas(Long contratoId, String estado, LocalDate fechaDesde, LocalDate fechaHasta) {

        if (estado != null && estado.isBlank()) {
            estado = null;
        }

        return facturaRepo.filtrarFacturas(contratoId, estado, fechaDesde, fechaHasta);
    }

    private void validarFactura(Factura factura) {

        if (factura.getContrato() == null) {
            throw new RuntimeException("Debe seleccionar un contrato");
        }

        if (factura.getContrato().getEliminado() != null && factura.getContrato().getEliminado()) {
            throw new RuntimeException("No se puede facturar un contrato eliminado");
        }

        String estadoContrato = factura.getContrato().getEstadoContrato();

        if (estadoContrato == null || !estadoContrato.trim().equalsIgnoreCase("activo")) {
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
            throw new RuntimeException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }

        if (factura.getImporte() == null || factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El importe debe ser positivo");
        }

        if (factura.getEstadoFactura() == null || factura.getEstadoFactura().isBlank()) {
            factura.setEstadoFactura("pendiente");
        }

        if (factura.getImportePagado() != null &&
                factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El importe pagado debe ser positivo");
        }

        if ("anulada".equalsIgnoreCase(factura.getEstadoFactura())) {
            if (factura.getFechaPago() != null ||
                    factura.getMedioPago() != null && !factura.getMedioPago().isBlank() ||
                    factura.getImportePagado() != null ||
                    factura.getInteres() != null) {

                throw new RuntimeException("Una factura anulada no puede tener datos de pago");
            }
        }

        if ("pagada".equalsIgnoreCase(factura.getEstadoFactura())) {
            if (factura.getFechaPago() == null ||
                    factura.getMedioPago() == null || factura.getMedioPago().isBlank() ||
                    factura.getImportePagado() == null) {

                throw new RuntimeException("Una factura pagada debe tener fecha de pago, medio de pago e importe pagado");
            }
        }

        if (!"pagada".equalsIgnoreCase(factura.getEstadoFactura())) {
            if (factura.getFechaPago() != null ||
                    factura.getMedioPago() != null && !factura.getMedioPago().isBlank() ||
                    factura.getImportePagado() != null ||
                    factura.getInteres() != null) {

                throw new RuntimeException("Solo una factura pagada puede tener datos de pago");
            }
        }
    }

    private void validarCambioEstado(String estadoAnterior, String estadoNuevo) {

        if (estadoNuevo == null || estadoNuevo.isBlank()) {
            throw new RuntimeException("Debe seleccionar un estado");
        }

        if (estadoAnterior.equalsIgnoreCase(estadoNuevo)) {
            return;
        }

        if (estadoAnterior.equalsIgnoreCase("pendiente") && estadoNuevo.equalsIgnoreCase("pagada")) {
            return;
        }

        if (estadoAnterior.equalsIgnoreCase("pendiente") && estadoNuevo.equalsIgnoreCase("vencida")) {
            return;
        }

        if (estadoAnterior.equalsIgnoreCase("vencida") && estadoNuevo.equalsIgnoreCase("pagada")) {
            return;
        }

        if (estadoAnterior.equalsIgnoreCase("pendiente") && estadoNuevo.equalsIgnoreCase("anulada")) {
            return;
        }

        throw new RuntimeException("Cambio de estado no permitido");
    }
}
