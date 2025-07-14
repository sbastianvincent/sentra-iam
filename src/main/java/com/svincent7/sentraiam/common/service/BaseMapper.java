package com.svincent7.sentraiam.common.service;

public interface BaseMapper<E, Req, Resp> {
    E toEntity(Req request);
    Resp toResponseDTO(E entity);
    void updateEntityFromDTO(Req request, E entity);
}
