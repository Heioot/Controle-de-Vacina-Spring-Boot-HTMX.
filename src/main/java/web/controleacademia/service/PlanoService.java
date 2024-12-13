package web.controleacademia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.controleacademia.model.Plano;
import web.controleacademia.repository.PlanoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    public Plano salvar(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Optional<Plano> buscarPorId(Long id) {
        return planoRepository.findById(id);
    }

    public void deletar(Long id) {
        planoRepository.deleteById(id);
    }
}