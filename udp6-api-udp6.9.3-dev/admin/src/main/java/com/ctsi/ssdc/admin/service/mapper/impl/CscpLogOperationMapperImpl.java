package com.ctsi.ssdc.admin.service.mapper.impl;

import com.ctsi.ssdc.admin.domain.CscpLogOperation;
import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpLogOperationMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity CscpLogOperation and its DTO CscpLogOperationDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpLogOperationMapperImpl implements CscpLogOperationMapper {

    @Override
    public CscpLogOperation toEntity(CscpLogOperationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpLogOperation cscpLogOperation = new CscpLogOperation();

		cscpLogOperation.setId( dto.getId() );		
		cscpLogOperation.setUserid( dto.getUserid() );		
		cscpLogOperation.setTenantId( dto.getTeantId() );
		cscpLogOperation.setUsername( dto.getUsername() );
		cscpLogOperation.setUri( dto.getUri() );		
		cscpLogOperation.setIp( dto.getIp() );		
		cscpLogOperation.setParams( dto.getParams() );		
		cscpLogOperation.setMethod( dto.getMethod() );		
		cscpLogOperation.setMessage( dto.getMessage() );		
		cscpLogOperation.setStatus( dto.getStatus() );		
		cscpLogOperation.setTime( dto.getTime() );		
		cscpLogOperation.setError( dto.getError() );		

        return cscpLogOperation;
    }

    @Override
    public CscpLogOperationDTO toDto(CscpLogOperation entity) {
        if ( entity == null ) {
            return null;
        }

        CscpLogOperationDTO cscpLogOperationDTO = new CscpLogOperationDTO();

		cscpLogOperationDTO.setId( entity.getId() );	
		cscpLogOperationDTO.setUserid( entity.getUserid() );	
		cscpLogOperationDTO.setTeantId( entity.getTenantId() );
		cscpLogOperationDTO.setUsername( entity.getUsername() );
		cscpLogOperationDTO.setUri( entity.getUri() );	
		cscpLogOperationDTO.setIp( entity.getIp() );	
		cscpLogOperationDTO.setParams( entity.getParams() );	
		cscpLogOperationDTO.setMethod( entity.getMethod() );	
		cscpLogOperationDTO.setMessage( entity.getMessage() );	
		cscpLogOperationDTO.setStatus( entity.getStatus() );	
		cscpLogOperationDTO.setTime( entity.getTime() );	
		cscpLogOperationDTO.setError( entity.getError() );	
       

        return cscpLogOperationDTO;
    }

    @Override
    public List<CscpLogOperation> toEntity(List<CscpLogOperationDTO> dtoList) {
        if ( dtoList == null ) {
            return Collections.emptyList(); 
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpLogOperationDTO> toDto(List<CscpLogOperation> entityList) {
        if ( entityList == null ) {
            return Collections.emptyList(); 
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
