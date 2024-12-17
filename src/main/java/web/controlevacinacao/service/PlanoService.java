package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.model.Plano;
import web.controlevacinacao.repository.PlanoRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    // Salvar um plano
    public void salvar(Plano plano) {
        planoRepository.save(plano);
    }

    // Atualizar um plano existente
    public void alterar(Plano plano) {
        planoRepository.save(plano);
    }

    // Remover um plano
    public void remover(Long id) {
        planoRepository.deleteById(id);
    }

    // Buscar um plano por ID
    public Optional<Plano> buscarPorId(Long id) {
        return planoRepository.findById(id);
    }

    // Listar todos os planos
    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }
}
