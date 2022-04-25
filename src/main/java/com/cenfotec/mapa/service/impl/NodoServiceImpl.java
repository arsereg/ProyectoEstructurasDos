package com.cenfotec.mapa.service.impl;

import com.cenfotec.mapa.domain.Nodo;
import com.cenfotec.mapa.repository.NodoRepository;
import com.cenfotec.mapa.service.NodoService;
import com.cenfotec.mapa.service.dto.NodoDTO;
import com.cenfotec.mapa.service.mapper.NodoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Nodo}.
 */
@Service
@Transactional
public class NodoServiceImpl implements NodoService {

    private final Logger log = LoggerFactory.getLogger(NodoServiceImpl.class);

    private final NodoRepository nodoRepository;

    private final NodoMapper nodoMapper;

    public NodoServiceImpl(NodoRepository nodoRepository, NodoMapper nodoMapper) {
        this.nodoRepository = nodoRepository;
        this.nodoMapper = nodoMapper;
    }

    @Override
    public NodoDTO save(NodoDTO nodoDTO) {
        log.debug("Request to save Nodo : {}", nodoDTO);
        Nodo nodo = nodoMapper.toEntity(nodoDTO);
        nodo = nodoRepository.save(nodo);
        return nodoMapper.toDto(nodo);
    }

    @Override
    public Optional<NodoDTO> partialUpdate(NodoDTO nodoDTO) {
        log.debug("Request to partially update Nodo : {}", nodoDTO);

        return nodoRepository
            .findById(nodoDTO.getId())
            .map(existingNodo -> {
                nodoMapper.partialUpdate(existingNodo, nodoDTO);

                return existingNodo;
            })
            .map(nodoRepository::save)
            .map(nodoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodoDTO> findAll() {
        log.debug("Request to get all Nodos");
        return nodoRepository.findAll().stream().map(nodoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NodoDTO> findOne(Long id) {
        log.debug("Request to get Nodo : {}", id);
        return nodoRepository.findById(id).map(nodoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Nodo : {}", id);
        nodoRepository.deleteById(id);
    }
}
