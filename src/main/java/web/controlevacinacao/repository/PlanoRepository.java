package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Plano;

public interface PlanoRepository extends JpaRepository<Plano, Long>{
    
}
