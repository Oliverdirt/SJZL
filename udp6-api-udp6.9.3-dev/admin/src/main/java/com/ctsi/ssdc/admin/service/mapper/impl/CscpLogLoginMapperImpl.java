package com.ctsi.ssdc.admin.service.mapper.impl;

import com.ctsi.ssdc.admin.domain.CscpLogLogin;
import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpLogLoginMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity CscpLogLogin and its DTO CscpLogLoginDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpLogLoginMapperImpl implements CscpLogLoginMapper {

    @Override
    public CscpLogLogin toEntity(CscpLogLoginDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpLogLogin cscpLogLogin = new CscpLogLogin();

		cscpLogLogin.setId( dto.getId() );		
		cscpLogLogin.setTenantId( dto.getTeantId() );
		cscpLogLogin.setUserName( dto.getUserName() );
		cscpLogLogin.setIp( dto.getIp() );		
		cscpLogLogin.setMessage( dto.getMessage() );		
		cscpLogLogin.setTime( dto.getTime() );		
		cscpLogLogin.setStatus( dto.getStatus() );		

        return cscpLogLogin;
    }

    @Override
    public CscpLogLoginDTO toDto(CscpLogLogin entity) {
        if ( entity == null ) {
            return null;
        }

        CscpLogLoginDTO cscpLogLoginDTO = new CscpLogLoginDTO();

		cscpLogLoginDTO.setId( entity.getId() );	
		cscpLogLoginDTO.setTeantId( entity.getTenantId() );
		cscpLogLoginDTO.setUserName( entity.getUserName() );
		cscpLogLoginDTO.setIp( entity.getIp() );	
		cscpLogLoginDTO.setMessage( entity.getMessage() );	
		cscpLogLoginDTO.setTime( entity.getTime() );	
		cscpLogLoginDTO.setStatus( entity.getStatus() );	
       

        return cscpLogLoginDTO;
    }

    @Override
    public List<CscpLogLogin> toEntity(List<CscpLogLoginDTO> dtoList) {
        if ( dtoList == null ) {
            return Collections.emptyList(); 
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpLogLoginDTO> toDto(List<CscpLogLogin> entityList) {
        if ( entityList == null ) {
            return Collections.emptyList(); 
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
