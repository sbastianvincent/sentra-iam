package com.svincent7.sentraiam.common.service;

import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseService<E, Req, Resp, ID> {

    protected abstract JpaRepository<E, ID> getRepository();
    protected abstract BaseMapper<E, Req, Resp> getMapper();

    public Resp create(final Req request) {
        verifyCreate(request);
        E entity = getMapper().toEntity(request);
        E savedEntity = getRepository().save(entity);
        return getMapper().toResponseDTO(savedEntity);
    }

    public Resp update(final ID id, final Req request) {
        verifyUpdate(id, request);
        E entity = getResourceById(id);
        getMapper().updateEntityFromDTO(request, entity);
        E updatedEntity = getRepository().save(entity);
        return getMapper().toResponseDTO(updatedEntity);
    }

    public Resp getById(final ID id) {
        verifyGetById(id);
        E entity = getResourceById(id);
        return getMapper().toResponseDTO(entity);
    }

    public List<Resp> getAll() {
        verifyGetAll();
        List<E> entities = getRepository().findAll();
        return entities.stream()
                .map(getMapper()::toResponseDTO)
                .collect(Collectors.toList());
    }

    public E getResourceById(final ID id) {
        verifyGetResourceById(id);
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found: " + id));
    }

    protected void verifyCreate(final Req request) {

    }

    protected void verifyUpdate(final ID id, final Req request) {

    }

    protected void verifyGetById(final ID id) {

    }

    protected void verifyGetResourceById(final ID id) {

    }

    protected void verifyGetAll() {

    }
}
