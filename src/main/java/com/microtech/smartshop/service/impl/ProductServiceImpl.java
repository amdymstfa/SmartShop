package com.microtech.smartshop.service.impl ;

import com.microtech.smartshop.entity.Product;
import com.microtech.smartshop.repository.ProductRepository;
import com.microtech.smartshop.service.ProductService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class ProductServiceImpl
            extends GenericServiceImpl<Product, Long, ProductRepository>
            implements ProductService {

    public ProductServiceImpl(ProductRepository repository){
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }


    @Override
    @Transactional(readOnly = true)
    public boolean checkStock(Long id, Integer quantity) {
        Product product = findByIdOrThrow(id);
        return product.hasStock(quantity);
    }

    @Override
    public void softDelete(Long id) {
        Product product = findByIdOrThrow(id);
        product.softDelete();
        repository.save(product);
        log.info("Product {} soft deleted", id);
    }

    @Override
    public void restore(Long id) {
        Product product = findByIdOrThrow(id);
        if (!product.getIsDeleted()){
            throw new IllegalArgumentException("This product is not deleted");
        }
        product.setIsDeleted(false);
        repository.save(product);
        log.info("Product {} restored", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchByName(String name, Pageable pageable) {
        return repository.findByName(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllAvailable(Pageable pageable) {
        return repository.findByIsDeletedFalse(pageable);
    }

}