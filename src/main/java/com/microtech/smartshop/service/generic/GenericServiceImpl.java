package com.microtech.smartshop.service.generic ;


import com.microtech.smartshop.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>>
        implements GenericService<T, ID> {

        protected final R repository ;


        public GenericServiceImpl(R repository){
            this.repository = repository ;
        }

    @Override
    public T create(T entity){
        return repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id){
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable){
            return repository.findAll(pageable);
    }

    @Override
    public T update(ID id, T entity){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    getEntityName() + " with ID " + id + " not found"
            );
        }
        return repository.save(entity);
    }

    @Override
    public void deleteById(ID id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    getEntityName() + " with ID " + id + " not found"
            );
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    protected abstract String getEntityName();

    protected T findByIdOrThrow(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getEntityName() + " with ID " + id + " not found"
                ));
    }
}