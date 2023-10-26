package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpMenusMapper;

/**
 * Mapper for the entity CscpMenus and its DTO CscpMenusDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpMenusMapperImpl implements CscpMenusMapper {

    @Override
    public CscpMenus toEntity(CscpMenusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpMenus cscpMenus = new CscpMenus();

		cscpMenus.setId( dto.getId() );		
		cscpMenus.setName( dto.getName() );		
		cscpMenus.setIcon( dto.getIcon() );		
		cscpMenus.setTitle( dto.getTitle() );		
		cscpMenus.setUrl( dto.getUrl() );		
		cscpMenus.setHttpMethod( dto.getHttpMethod() );		
		cscpMenus.setComponent( dto.getComponent() );		
		cscpMenus.setParentId( dto.getParentId() );		
		cscpMenus.setType( dto.getType() );		
		cscpMenus.setPermissionCode( dto.getPermissionCode() );		
		cscpMenus.setOrderby( dto.getOrderby() );		

        return cscpMenus;
    }

    @Override
    public CscpMenusDTO toDto(CscpMenus entity) {
        if ( entity == null ) {
            return null;
        }

        CscpMenusDTO cscpMenusDTO = new CscpMenusDTO();

		cscpMenusDTO.setId( entity.getId() );	
		cscpMenusDTO.setName( entity.getName() );	
		cscpMenusDTO.setIcon( entity.getIcon() );	
		cscpMenusDTO.setTitle( entity.getTitle() );	
		cscpMenusDTO.setUrl( entity.getUrl() );	
		cscpMenusDTO.setHttpMethod( entity.getHttpMethod() );	
		cscpMenusDTO.setComponent( entity.getComponent() );	
		cscpMenusDTO.setParentId( entity.getParentId() );	
		cscpMenusDTO.setType( entity.getType() );	
		cscpMenusDTO.setPermissionCode( entity.getPermissionCode() );	
		cscpMenusDTO.setOrderby( entity.getOrderby() );	
       

        return cscpMenusDTO;
    }

    @Override
    public List<CscpMenus> toEntity(List<CscpMenusDTO> dtoList) {
        if ( dtoList == null ) {
            return Collections.emptyList(); 
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpMenusDTO> toDto(List<CscpMenus> entityList) {
        if ( entityList == null ) {
            return Collections.emptyList(); 
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
