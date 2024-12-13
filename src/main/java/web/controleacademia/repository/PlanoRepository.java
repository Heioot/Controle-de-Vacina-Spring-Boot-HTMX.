package web.controleacademia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controleacademia.model.Plano;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
}