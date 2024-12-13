package web.controleacademia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controleacademia.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario);
	
}
