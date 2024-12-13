package web.controleacademia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controleacademia.model.Equipamento;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
}
