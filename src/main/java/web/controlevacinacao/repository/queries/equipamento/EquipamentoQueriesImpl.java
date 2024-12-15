package web.controlevacinacao.repository.queries.equipamento;

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
import web.controlevacinacao.filter.EquipamentoFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Equipamento;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class EquipamentoQueriesImpl implements EquipamentoQueries {

    private static final Logger logger = LoggerFactory.getLogger(EquipamentoQueriesImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Equipamento> pesquisar(EquipamentoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Equipamento> criteriaQuery = builder.createQuery(Equipamento.class);
        Root<Equipamento> e = criteriaQuery.from(Equipamento.class);
        TypedQuery<Equipamento> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();
        List<Predicate> predicateListTotal = new ArrayList<>();
        Predicate[] predArray;
        Predicate[] predArrayTotal;

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(e.<Long>get("id"), filtro.getId()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(builder.lower(e.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getDescricao())) {
            predicateList.add(builder.like(builder.lower(e.<String>get("descricao")),
                    "%" + filtro.getDescricao().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getStatus())) {
            predicateList.add(builder.equal(e.<Status>get("status"), Status.valueOf(filtro.getStatus())));
        }

        predicateList.add(builder.equal(e.<Status>get("status"), Status.ATIVO));

        predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);
        criteriaQuery.select(e).where(predArray);
        PaginacaoUtil.prepararOrdem(e, criteriaQuery, builder, pageable);
        typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        typedQuery.setHint("hibernate.query.passDistinctThrough", false);
        List<Equipamento> equipamentos = typedQuery.getResultList();

        logger.info("Calculando o total de registros que o filtro retornará.");
        CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
        Root<Equipamento> eTotal = criteriaQueryTotal.from(Equipamento.class);
        criteriaQueryTotal.select(builder.count(eTotal));

        if (filtro.getId() != null) {
            predicateListTotal.add(builder.equal(eTotal.<Long>get("id"), filtro.getId()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateListTotal.add(builder.like(builder.lower(eTotal.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getDescricao())) {
            predicateListTotal.add(builder.like(builder.lower(eTotal.<String>get("descricao")),
                    "%" + filtro.getDescricao().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getStatus())) {
            predicateListTotal.add(builder.equal(eTotal.<Status>get("status"), Status.valueOf(filtro.getStatus())));
        }

        predicateListTotal.add(builder.equal(eTotal.<Status>get("status"), Status.ATIVO));

        predArrayTotal = new Predicate[predicateListTotal.size()];
        predicateListTotal.toArray(predArrayTotal);
        criteriaQueryTotal.where(predArrayTotal);
        TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
        long totalEquipamentos = typedQueryTotal.getSingleResult();

        logger.info("O filtro retornará {} registros.", totalEquipamentos);
        Page<Equipamento> page = new PageImpl<>(equipamentos, pageable, totalEquipamentos);
        return page;
    }
}
