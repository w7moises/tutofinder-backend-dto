package com.tutofinder.app.services.impl;

import com.tutofinder.app.dto.DocenteDto;
import com.tutofinder.app.dto.create.CreateDocenteDto;
import com.tutofinder.app.entity.Docente;
import com.tutofinder.app.exception.BookingException;
import com.tutofinder.app.exception.InternalServerErrorException;
import com.tutofinder.app.exception.NotFoundException;
import com.tutofinder.app.repository.DocenteRepository;
import com.tutofinder.app.services.DocenteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocenteServiceImpl implements DocenteService {

    @Autowired
    DocenteRepository docenteRepository;

    public static final ModelMapper modelMapper = new ModelMapper();

    @Override
    public DocenteDto getDocenteById(Long docenteId) throws BookingException {
        return modelMapper.map(getDocenteEntity(docenteId),DocenteDto.class);
    }

    @Override
    public List<DocenteDto> getDocentes() throws BookingException {
        final List<Docente> docenteEntity = docenteRepository.findAll();
        return docenteEntity.stream().map(service->modelMapper.map(service,DocenteDto.class)).collect(Collectors.toList());
    }

    @Override
    public DocenteDto createDocente(CreateDocenteDto createDocenteDto) throws BookingException {
        Docente docenteEntity;
        Docente docente = new Docente();
        docente.setNombre(createDocenteDto.getNombre());
        docente.setApellido(createDocenteDto.getApellido());
        docente.setDni(createDocenteDto.getDni());
        docente.setMembresia(false);
        docente.setDomicilio(createDocenteDto.getDomicilio());
        docente.setCorreo(createDocenteDto.getCorreo());
        docente.setNumeroCuenta(createDocenteDto.getNumeroCuenta());
        try {
            docenteEntity=docenteRepository.save(docente);
        } catch (final Exception e){
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR","INTERNAL_SERVER_ERROR");
        }
        return modelMapper.map(getDocenteEntity(docenteEntity.getId()),DocenteDto.class);
    }

    @Override
    public DocenteDto updateDocente(CreateDocenteDto createDocenteDto, Long docenteId) throws BookingException {
        Optional<Docente> docente = docenteRepository.findById(docenteId);
        if(!docente.isPresent()){
            throw new NotFoundException("ID_NOT_FOOUND","ID_NOT_FOUND");
        }
        Docente docenteEntity = docente.get();
        docenteEntity.setNombre(createDocenteDto.getNombre());
        docenteEntity.setApellido(createDocenteDto.getApellido());
        docenteEntity.setDni(createDocenteDto.getDni());
        docenteEntity.setDomicilio(createDocenteDto.getDomicilio());
        docenteEntity.setCorreo(createDocenteDto.getCorreo());
        docenteEntity.setNumeroCuenta(createDocenteDto.getNumeroCuenta());
        try {
            docenteRepository.save(docenteEntity);
        } catch (final Exception e){
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR","INTERNAL_SERVER_ERROR");
        }
        return modelMapper.map(getDocenteEntity(docenteEntity.getId()),DocenteDto.class);
    }

    @Override
    public String deleteDocente(Long docenteId) throws BookingException {
        docenteRepository.findById(docenteId).
                orElseThrow(()->new NotFoundException("ID_NOT_FOOUND","ID_NOT_FOUND"));
        try {
            docenteRepository.deleteById(docenteId);
        } catch (final Exception e){
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR","INTERNAL_SERVER_ERROR");
        }
        return "DOCENTE_DELETED";
    }

    private Docente getDocenteEntity(Long docenteId) throws BookingException{
        return docenteRepository.findById(docenteId).
                orElseThrow(() -> new NotFoundException("SNOT-404-1","DOCENTE_NOT_FOUND"));
    }
}