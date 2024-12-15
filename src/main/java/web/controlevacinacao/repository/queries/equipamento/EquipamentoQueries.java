package web.controlevacinacao.repository.queries.equipamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.EquipamentoFilter;
import web.controlevacinacao.model.Equipamento;


public interface EquipamentoQueries {
    Page<Equipamento> pesquisar(EquipamentoFilter filtro, Pageable pageable);
}
