package br.com.projeto.order.service;



import br.com.projeto.order.exception.ProductNotFoundException;
import br.com.projeto.order.model.Product;
import br.com.projeto.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;


    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product){
        if (product.getPrice() <= 0){
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product){
        Product existingProduct = productRepository.findById(id).orElse(null);
         if (existingProduct != null){
             existingProduct.setName(product.getName());
             existingProduct.setPrice(product.getPrice());
             existingProduct.setQuantity(product.getQuantity());

             return productRepository.save(existingProduct);
         } else {
             throw new ProductNotFoundException("Produto com ID " + id + " não encontrado.");
         }
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
