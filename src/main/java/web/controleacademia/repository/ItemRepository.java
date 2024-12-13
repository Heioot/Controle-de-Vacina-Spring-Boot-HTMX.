package web.controleacademia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controleacademia.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}