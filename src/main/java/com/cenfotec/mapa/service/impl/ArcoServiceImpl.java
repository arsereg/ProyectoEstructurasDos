package com.cenfotec.mapa.service.impl;

import com.cenfotec.mapa.domain.Arco;
import com.cenfotec.mapa.repository.ArcoRepository;
import com.cenfotec.mapa.service.ArcoService;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.mapper.ArcoMapper;

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
 * Service Implementation for managing {@link Arco}.
 */
@Service
@Transactional
public class ArcoServiceImpl implements ArcoService {

    private final Logger log = LoggerFactory.getLogger(ArcoServiceImpl.class);

    private final ArcoRepository arcoRepository;

    private final ArcoMapper arcoMapper;

    public ArcoServiceImpl(ArcoRepository arcoRepository, ArcoMapper arcoMapper) {
        this.arcoRepository = arcoRepository;
        this.arcoMapper = arcoMapper;
    }

    @Override
    public ArcoDTO save(ArcoDTO arcoDTO) {
        log.debug("Request to save Arco : {}", arcoDTO);
        Arco arco = arcoMapper.toEntity(arcoDTO);
        arco = arcoRepository.save(arco);
        return arcoMapper.toDto(arco);
    }

    @Override
    public Optional<ArcoDTO> partialUpdate(ArcoDTO arcoDTO) {
        log.debug("Request to partially update Arco : {}", arcoDTO);

        return arcoRepository
            .findById(arcoDTO.getId())
            .map(existingArco -> {
                arcoMapper.partialUpdate(existingArco, arcoDTO);

                return existingArco;
            })
            .map(arcoRepository::save)
            .map(arcoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArcoDTO> findAll() {
        log.debug("Request to get all Arcos");
        return arcoRepository.findAll().stream().map(arcoMapper::toDto).collect(Collectors.toList());
    }

    public Page<ArcoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return arcoRepository.findAllWithEagerRelationships(pageable).map(arcoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArcoDTO> findOne(Long id) {
        log.debug("Request to get Arco : {}", id);
        return arcoRepository.findOneWithEagerRelationships(id).map(arcoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Arco : {}", id);
        arcoRepository.deleteById(id);
    }
}
