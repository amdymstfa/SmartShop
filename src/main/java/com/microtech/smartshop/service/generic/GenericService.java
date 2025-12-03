package com.microtech.smartshop.service.generic ;





import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Generic interface defining the common CRUD contract
 * Avoid code duplication across all services
 * @param <T>
 * @param <ID>
 */
public interface GenericService<T, ID> {

    /**
     * Create a new entity
     * @param entity of type T
     * @return T
     */
    T create(T entity);

    /**
     * Find an entity with id
     * @param id of entity
     * @return null of Optional if not found
     */
    Optional<T> findById(ID id);

    /**
     * Retrieve all entities with pagination
     * @param pageable
     * @return Page of entities
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Update an entity
     * @param id of entity to update
     * @param entity corresponding entity
     * @return T after update
     */
    T update(ID id, T entity);

    /**
     * Delete an entity
     * @param id of entity to delete
     */
    void deleteById(ID id) ;

    /**
     * Check if an entity exists
     * @param id of entity to check
     * @return  true if found or false otherwise
     */
    boolean existsById(ID id);
}