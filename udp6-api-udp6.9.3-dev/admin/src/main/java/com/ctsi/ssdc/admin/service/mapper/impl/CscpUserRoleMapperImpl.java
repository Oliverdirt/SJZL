package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpUserRole;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpUserRoleMapper;

/**
 * Mapper for the entity CscpUserRole and its DTO CscpUserRoleDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpUserRoleMapperImpl implements CscpUserRoleMapper {

    @Override
    public CscpUserRole toEntity(CscpUserRoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpUserRole cscpUserRole = new CscpUserRole();

		cscpUserRole.setId( dto.getId() );		
		cscpUserRole.setUserId( dto.getUserId() );		
		cscpUserRole.setRoleId( dto.getRoleId() );		

        return cscpUserRole;
    }

    @Override
    public CscpUserRoleDTO toDto(CscpUserRole entity) {
        if ( entity == null ) {
            return null;
        }

        CscpUserRoleDTO cscpUserRoleDTO = new CscpUserRoleDTO();

		cscpUserRoleDTO.setId( entity.getId() );	
		cscpUserRoleDTO.setUserId( entity.getUserId() );	
		cscpUserRoleDTO.setRoleId( entity.getRoleId() );	
       

        return cscpUserRoleDTO;
    }

    @Override
    public List<CscpUserRole> toEntity(List<CscpUserRoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpUserRoleDTO> toDto(List<CscpUserRole> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
