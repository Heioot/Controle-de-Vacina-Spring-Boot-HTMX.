package web.controleacademia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controleacademia.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}