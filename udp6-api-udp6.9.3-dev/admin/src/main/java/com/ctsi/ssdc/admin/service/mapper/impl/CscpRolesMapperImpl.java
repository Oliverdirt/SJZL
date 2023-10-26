package com.ctsi.ssdc.admin.service.mapper.impl;

import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpRolesMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity CscpRoles and its DTO CscpRolesDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpRolesMapperImpl implements CscpRolesMapper {

    @Override
    public CscpRoles toEntity(CscpRolesDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpRoles cscpRoles = new CscpRoles();

		cscpRoles.setId( dto.getId() );		
		cscpRoles.setName( dto.getName() );		
		cscpRoles.setRoleExtent( dto.getRoleExtent() );		
		cscpRoles.setParentId( dto.getParentId() );		
		cscpRoles.setIcon( dto.getIcon() );		
		cscpRoles.setTenantId( dto.getTenantId());
		cscpRoles.setDataScope( dto.getDataScope());

        return cscpRoles;
    }

    @Override
    public CscpRolesDTO toDto(CscpRoles entity) {
        if ( entity == null ) {
            return null;
        }

        CscpRolesDTO cscpRolesDTO = new CscpRolesDTO();

		cscpRolesDTO.setId( entity.getId() );	
		cscpRolesDTO.setName( entity.getName() );	
		cscpRolesDTO.setRoleExtent( entity.getRoleExtent() );	
		cscpRolesDTO.setParentId( entity.getParentId() );	
		cscpRolesDTO.setIcon( entity.getIcon() );	
		cscpRolesDTO.setTenantId( entity.getTenantId() );
        cscpRolesDTO.setDataScope( entity.getDataScope());

        return cscpRolesDTO;
    }

    @Override
    public List<CscpRoles> toEntity(List<CscpRolesDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpRolesDTO> toDto(List<CscpRoles> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
