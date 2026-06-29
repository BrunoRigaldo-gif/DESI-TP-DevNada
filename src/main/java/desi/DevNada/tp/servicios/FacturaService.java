package desi.DevNada.tp.servicios;

import java.util.List;

import desi.DevNada.tp.entidades.Factura;

public interface FacturaService {

    List<Factura> listar();

    Factura buscarPorId(Long id);

    Factura guardar(Factura factura);

    Factura modificar(Long id, Factura factura);

    void eliminar(Long id);
}