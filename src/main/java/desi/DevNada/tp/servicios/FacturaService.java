package desi.DevNada.tp.servicios;

import java.time.LocalDate;
import java.util.List;

import desi.DevNada.tp.entidades.Factura;

public interface FacturaService {

    List<Factura> listar();

    Factura buscarPorId(Long id);

    Factura guardar(Factura factura);

    Factura modificar(Long id, Factura factura);

    void eliminar(Long id);

    List<Factura> filtrarFacturas(Long contratoId, String estado, LocalDate fechaDesde, LocalDate fechaHasta);
}
