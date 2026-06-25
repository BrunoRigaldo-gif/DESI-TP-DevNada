package desi.DevNada.tp.servicios;

import java.util.List;
import desi.DevNada.tp.entidades.Factura;

public interface FacturaService {

    List<Factura> obtenerTodas();

    Factura obtenerPorId(Long id);

    void guardarFactura(Factura factura);

    void modificarFactura(Factura factura);

    void eliminar(Long id);
}
