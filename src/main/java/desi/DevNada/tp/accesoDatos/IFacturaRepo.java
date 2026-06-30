package desi.DevNada.tp.accesoDatos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import desi.DevNada.tp.entidades.Factura;

public interface IFacturaRepo extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadaFalse();

    @Query("""
        SELECT f FROM Factura f
        WHERE f.eliminada = false
        AND (:contratoId IS NULL OR f.contrato.id = :contratoId)
        AND (:estado IS NULL OR LOWER(f.estadoFactura) = LOWER(:estado))
        AND (:fechaDesde IS NULL OR f.fechaVencimiento >= :fechaDesde)
        AND (:fechaHasta IS NULL OR f.fechaVencimiento <= :fechaHasta)
        """)
    List<Factura> filtrarFacturas(
    		@Param("contratoId") Long contratoId,
            @Param("estado") String estado,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta
    );
}
