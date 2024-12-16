package web.controlevacinacao.repository.queries.usuario;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.controlevacinacao.filter.UsuarioFilter;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class UsuarioQueriesImpl implements UsuarioQueries {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioQueriesImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
        Root<Usuario> u = criteriaQuery.from(Usuario.class);
        TypedQuery<Usuario> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();
        List<Predicate> predicateListTotal = new ArrayList<>();
        Predicate[] predArray;
        Predicate[] predArrayTotal;

        // Filtros dinâmicos
        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(u.get("codigo"), filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(builder.lower(u.get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getEmail())) {
            predicateList.add(builder.like(builder.lower(u.get("email")),
                    "%" + filtro.getEmail().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNomeUsuario())) {
            predicateList.add(builder.like(builder.lower(u.get("nomeUsuario")),
                    "%" + filtro.getNomeUsuario().toLowerCase() + "%"));
        }
        if (filtro.getDataNascimentoInicial() != null) {
            predicateList.add(builder.greaterThanOrEqualTo(u.get("dataNascimento"), filtro.getDataNascimentoInicial()));
        }
        if (filtro.getDataNascimentoFinal() != null) {
            predicateList.add(builder.lessThanOrEqualTo(u.get("dataNascimento"), filtro.getDataNascimentoFinal()));
        }
        if (filtro.getAtivo() != null) {
            predicateList.add(builder.equal(u.get("ativo"), filtro.getAtivo()));
        }

        predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);
        criteriaQuery.select(u).where(predArray);
        PaginacaoUtil.prepararOrdem(u, criteriaQuery, builder, pageable);
        typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Usuario> usuarios = typedQuery.getResultList();

        logger.info("Calculando o total de registros que o filtro retornará.");
        CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
        Root<Usuario> uTotal = criteriaQueryTotal.from(Usuario.class);
        criteriaQueryTotal.select(builder.count(uTotal));

        // Total de registros
        if (filtro.getCodigo() != null) {
            predicateListTotal.add(builder.equal(uTotal.get("codigo"), filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateListTotal.add(builder.like(builder.lower(uTotal.get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getEmail())) {
            predicateListTotal.add(builder.like(builder.lower(uTotal.get("email")),
                    "%" + filtro.getEmail().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNomeUsuario())) {
            predicateListTotal.add(builder.like(builder.lower(uTotal.get("nomeUsuario")),
                    "%" + filtro.getNomeUsuario().toLowerCase() + "%"));
        }
        if (filtro.getDataNascimentoInicial() != null) {
            predicateListTotal.add(builder.greaterThanOrEqualTo(uTotal.get("dataNascimento"), filtro.getDataNascimentoInicial()));
        }
        if (filtro.getDataNascimentoFinal() != null) {
            predicateListTotal.add(builder.lessThanOrEqualTo(uTotal.get("dataNascimento"), filtro.getDataNascimentoFinal()));
        }
        if (filtro.getAtivo() != null) {
            predicateListTotal.add(builder.equal(uTotal.get("ativo"), filtro.getAtivo()));
        }

        predArrayTotal = new Predicate[predicateListTotal.size()];
        predicateListTotal.toArray(predArrayTotal);
        criteriaQueryTotal.where(predArrayTotal);
        TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
        long totalUsuarios = typedQueryTotal.getSingleResult();

        logger.info("O filtro retornará {} registros.", totalUsuarios);
        return new PageImpl<>(usuarios, pageable, totalUsuarios);
    }
}
