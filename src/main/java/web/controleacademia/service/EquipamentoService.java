package web.controleacademia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.controleacademia.model.Equipamento;
import web.controleacademia.repository.EquipamentoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public Equipamento salvar(Equipamento equipamento) {
        return equipamentoRepository.save(equipamento);
    }

    public List<Equipamento> listarTodos() {
        return equipamentoRepository.findAll();
    }

    public Optional<Equipamento> buscarPorId(Long id) {
        return equipamentoRepository.findById(id);
    }

    public void deletar(Long id) {
        equipamentoRepository.deleteById(id);
    }
}