package desi.DevNada.tp.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import desi.DevNada.tp.entidades.Contrato;




public interface IContratoRepo extends JpaRepository<Contrato, Long> {

}