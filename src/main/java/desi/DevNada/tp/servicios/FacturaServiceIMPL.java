package desi.DevNada.tp.servicios;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import desi.DevNada.tp.accesoDatos.IFacturaRepo;
import desi.DevNada.tp.entidades.Factura;

@Service
public class FacturaServiceIMPL implements FacturaService {

    @Autowired
    private IFacturaRepo facturaRepo;

    @Override
    public List<Factura> obtenerTodas() {
        return facturaRepo.findByEliminadaFalse();
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la factura"));
    }

    @Override
    public void guardarFactura(Factura factura) {

        validarDatosPrincipales(factura);

        // Toda factura nueva arranca pendiente
        factura.setEstadoFactura("pendiente");

        // Eliminación lógica apagada
        factura.setEliminada(false);

        // Como arranca pendiente, no debe tener datos de pago
        limpiarDatosPago(factura);

        facturaRepo.save(factura);
    }

    @Override
    public void modificarFactura(Factura factura) {

        Factura facturaBD = obtenerPorId(factura.getId());

        if ("pagada".equalsIgnoreCase(facturaBD.getEstadoFactura())) {
            throw new RuntimeException("No se puede modificar una factura pagada");
        }

        if ("anulada".equalsIgnoreCase(facturaBD.getEstadoFactura())) {
            throw new RuntimeException("No se puede modificar una factura anulada");
        }

        validarDatosPrincipales(factura);

        if (!cambioEstadoPermitido(facturaBD.getEstadoFactura(), factura.getEstadoFactura())) {
            throw new RuntimeException("El cambio de estado no está permitido");
        }

        // No se cambia el contrato porque el TP dice que no debe modificarse
        facturaBD.setConceptoFacturado(factura.getConceptoFacturado());
        facturaBD.setFechaEmision(factura.getFechaEmision());
        facturaBD.setFechaVencimiento(factura.getFechaVencimiento());
        facturaBD.setImporte(factura.getImporte());
        facturaBD.setEstadoFactura(factura.getEstadoFactura());

        facturaBD.setFechaPago(factura.getFechaPago());
        facturaBD.setMedioPago(factura.getMedioPago());
        facturaBD.setImportePagado(factura.getImportePagado());
        facturaBD.setInteres(factura.getInteres());

        validarPago(facturaBD);

        if (!"pagada".equalsIgnoreCase(facturaBD.getEstadoFactura())) {
            limpiarDatosPago(facturaBD);
        }

        facturaRepo.save(facturaBD);
    }

    @Override
    public void eliminar(Long id) {

        Factura factura = obtenerPorId(id);

        if ("pagada".equalsIgnoreCase(factura.getEstadoFactura())) {
            throw new RuntimeException("No se puede eliminar una factura pagada");
        }

        // Eliminación lógica
        factura.setEliminada(true);

        facturaRepo.save(factura);
    }

    private void validarDatosPrincipales(Factura factura) {

        if (factura.getContrato() == null || factura.getContrato().getId() == null) {
            throw new RuntimeException("Debe seleccionar un contrato");
        }

        if (factura.getConceptoFacturado() == null || factura.getConceptoFacturado().isBlank()) {
            throw new RuntimeException("El concepto facturado es obligatorio");
        }

        if (factura.getFechaEmision() == null) {
            throw new RuntimeException("La fecha de emisión es obligatoria");
        }

        if (factura.getFechaVencimiento() == null) {
            throw new RuntimeException("La fecha de vencimiento es obligatoria");
        }

        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new RuntimeException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión");
        }

        if (factura.getImporte() == null || factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El importe debe ser positivo");
        }

        /*Epic3 if (!"activo".equalsIgnoreCase(factura.getContrato().getEstadoContrato())) {
            throw new RuntimeException("Solo se puede facturar un contrato activo");
        }*/

       /*Epic3  if (factura.getContrato().getEliminado() != null && factura.getContrato().getEliminado()) {
            throw new RuntimeException("No se puede facturar un contrato eliminado");
        }*/
    }

    private void validarPago(Factura factura) {

        if ("pagada".equalsIgnoreCase(factura.getEstadoFactura())) {

            if (factura.getFechaPago() == null) {
                throw new RuntimeException("Debe cargar la fecha de pago");
            }

            if (factura.getMedioPago() == null || factura.getMedioPago().isBlank()) {
                throw new RuntimeException("Debe seleccionar un medio de pago");
            }

            if (factura.getImportePagado() == null ||
                    factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El importe pagado debe ser positivo");
            }
        }

        if ("anulada".equalsIgnoreCase(factura.getEstadoFactura())) {

            if (factura.getFechaPago() != null || factura.getMedioPago() != null) {
                throw new RuntimeException("Una factura anulada no puede tener datos de pago");
            }
        }
    }

    private boolean cambioEstadoPermitido(String estadoActual, String estadoNuevo) {

        if (estadoActual == null || estadoNuevo == null) {
            return false;
        }

        estadoActual = estadoActual.toLowerCase();
        estadoNuevo = estadoNuevo.toLowerCase();

        if (estadoActual.equals(estadoNuevo)) {
            return true;
        }

        if (estadoActual.equals("pendiente") && estadoNuevo.equals("pagada")) {
            return true;
        }

        if (estadoActual.equals("pendiente") && estadoNuevo.equals("vencida")) {
            return true;
        }

        if (estadoActual.equals("pendiente") && estadoNuevo.equals("anulada")) {
            return true;
        }

        if (estadoActual.equals("vencida") && estadoNuevo.equals("pagada")) {
            return true;
        }

        return false;
    }

    private void limpiarDatosPago(Factura factura) {
        factura.setFechaPago(null);
        factura.setMedioPago(null);
        factura.setImportePagado(null);
        factura.setInteres(null);
    }
}
