package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Equipamento;
import web.controlevacinacao.repository.EquipamentoRepository;

@Service
@Transactional
public class EquipamentoService {

    private EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public void salvar(Equipamento equipamento) {
        equipamentoRepository.save(equipamento);
    }

    public void alterar(Equipamento equipamento) {
        equipamentoRepository.save(equipamento);
    }

    public void remover(Equipamento equipamento) {
        equipamentoRepository.delete(equipamento);
    }
}
