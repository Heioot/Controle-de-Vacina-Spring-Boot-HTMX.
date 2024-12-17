package web.controlevacinacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findByPlanoIsNull();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
