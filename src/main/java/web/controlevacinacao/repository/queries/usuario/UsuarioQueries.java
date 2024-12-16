package web.controlevacinacao.repository.queries.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.UsuarioFilter;
import web.controlevacinacao.model.Usuario;

public interface UsuarioQueries {
    Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable);
    
}
