package com.svincent7.sentraiam.common.controller;

import com.svincent7.sentraiam.common.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class BaseController<E, Req, Resp, ID> {

    protected abstract BaseService<E, Req, Resp, ID> getService();

    @GetMapping()
    public ResponseEntity<List<Resp>> getAll() {
        verifyGetAll();
        List<Resp> entity = getService().getAll();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resp> getById(final @PathVariable ID id) {
        verifyGetById(id);
        Resp entity = getService().getById(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Resp> create(@Valid final @RequestBody Req entity) {
        verifyCreate(entity);
        Resp createdEntity = getService().create(entity);
        return ResponseEntity.ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resp> update(@Valid final @PathVariable ID id, @Valid final @RequestBody Req entity) {
        verifyUpdate(id, entity);
        Resp updatedEntity = getService().update(id, entity);
        return ResponseEntity.ok(updatedEntity);
    }

    protected void verifyGetAll() {

    }

    protected void verifyGetById(final ID id) {

    }

    protected void verifyCreate(final Req entity) {

    }

    protected void verifyUpdate(final ID id, final Req entity) {

    }
}
