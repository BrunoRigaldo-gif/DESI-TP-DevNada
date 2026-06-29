package desi.DevNada.tp.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import desi.DevNada.tp.entidades.HistorialEstadoFactura;

public interface IHistorialEstadoFacturaRepo extends JpaRepository<HistorialEstadoFactura, Long> {

}
