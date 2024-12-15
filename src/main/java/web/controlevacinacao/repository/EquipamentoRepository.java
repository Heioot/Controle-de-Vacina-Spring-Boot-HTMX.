package web.controlevacinacao.repository;

import web.controlevacinacao.model.Equipamento;
import web.controlevacinacao.repository.queries.equipamento.EquipamentoQueries;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long>, EquipamentoQueries{
    
}
