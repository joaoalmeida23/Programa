package br.com.projeto.order.repository;

import br.com.projeto.order.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
