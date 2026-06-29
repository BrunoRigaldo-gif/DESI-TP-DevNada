package desi.DevNada.tp.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import desi.DevNada.tp.entidades.Factura;

public interface IFacturaRepo extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadaFalse();

}