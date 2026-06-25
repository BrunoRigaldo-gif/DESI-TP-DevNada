package desi.DevNada.tp.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import desi.DevNada.tp.entidades.Factura;


@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {

    // Lista las facturas que no fueron eliminadas
    List<Factura> findByEliminadaFalse();

    // Busca facturas por estado
    List<Factura> findByEstadoFacturaAndEliminadaFalse(String estadoFactura);

    // Busca facturas por contrato
    List<Factura> findByContratoIdAndEliminadaFalse(Long idContrato);
}