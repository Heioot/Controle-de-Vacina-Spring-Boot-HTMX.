package web.controlevacinacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.repository.queries.usuario.UsuarioQueries;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioQueries {
	
	Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario);

	List<Usuario> findByPlanoIsNull();
	
}
