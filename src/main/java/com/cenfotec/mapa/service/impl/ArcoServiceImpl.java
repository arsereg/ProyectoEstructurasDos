package com.cenfotec.mapa.service.impl;

import com.cenfotec.mapa.domain.Arco;
import com.cenfotec.mapa.domain.Nodo;
import com.cenfotec.mapa.repository.ArcoRepository;
import com.cenfotec.mapa.repository.NodoRepository;
import com.cenfotec.mapa.service.ArcoService;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.mapper.ArcoMapper;

import java.util.*;
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

    private final NodoRepository nodoRepository;

    public ArcoServiceImpl(ArcoRepository arcoRepository, ArcoMapper arcoMapper, NodoRepository nodoRepository) {
        this.arcoRepository = arcoRepository;
        this.arcoMapper = arcoMapper;
        this.nodoRepository = nodoRepository;
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


    public void llenarDataFake(){

        List<Arco> result = new ArrayList<>();
        String[] arcos = {
            "kenabres-fortPortolmaeus",
            "kenabres-boudor",
            "fortPortolmaeus-starKeep",
            "starKeep-nerosyan",
            "nerosyan-starfall",
            "kenabres-egede",
            "egede-hernesOak",
            "egede-chessed",
            "portIce-sunderHorn",
            "sunderHorn-chitterhome",
            "scrapwall-hollowGarden",
            "chitterhome-falheart",
            "falheart-castleOfKnives",
            "kuratown-marstol",
            "marstol-iadenveigh",
            "iadenveigh-deadbridge",
            "iadenveigh-chokingTower",
            "chokingTower-blackpipe",
            "blackpipe-hajoth",
            "blackpipe-blackHorse",
            "blackHorse-mormouth",
            "chessed-scrapwall",
            "scrapwall-chitterhome",
            "fortPortolmaeus-scrapwall",
            "deadbridge-hajoth",
            "hajoth-castleOfKnives",
            "hollowGarden-chokingTower",
            "kuratown-deadbridge",
        };

        double[] pesos = {
                50.0,
                50.0,
                50.0,
                40.0,
                40.0,
                40.0,
                40.0,
                40.0,
                20.0,
                20.0,
                20.0,
                40.0,
                20.0,
                20.0,
                5.0,
                20.0,
                80.0,
                60.0,
                60.0,
                90.0,
                80.0,
                80.0,
                70.0,
                70.0,
                70.0,
                30.0,
                30.0,
                30.0,
                40.0,
                10.0
        };

        for (int i = 0; i < arcos.length; i++) {
            Nodo nodo1 = nodoRepository.findByName(arcos[i].split("-")[0]);
            Nodo nodo2 = nodoRepository.findByName(arcos[i].split("-")[1]);
            if(nodo1 == null || nodo2 == null){
                int x = 0;
            }
            HashSet<Nodo> froms = new HashSet<>();
            froms.add(nodo1);
            HashSet<Nodo> tos = new HashSet<>();
            tos.add(nodo2);
            Double peso = pesos[i];
            Arco arco = new Arco();
            arco.setFroms(froms);
            arco.setTos(tos);
            arco.setWeight(peso);
            result.add(arco);
        }
        arcoRepository.saveAll(result);

    }

}
