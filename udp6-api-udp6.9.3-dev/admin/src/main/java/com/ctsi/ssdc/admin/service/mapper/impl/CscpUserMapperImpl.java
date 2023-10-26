package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpUserMapper;

/**
 * Mapper for the entity CscpUser and its DTO CscpUserDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpUserMapperImpl implements CscpUserMapper {

    @Override
    public CscpUser toEntity(CscpUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpUser cscpUser = new CscpUser();

		cscpUser.setId( dto.getId() );		
		cscpUser.setUsername( dto.getUsername() );		
		cscpUser.setPassword( dto.getPassword() );	

        return cscpUser;
    }

    @Override
    public CscpUserDTO toDto(CscpUser entity) {
        if ( entity == null ) {
            return null;
        }

        CscpUserDTO cscpUserDTO = new CscpUserDTO();

		cscpUserDTO.setId( entity.getId() );	
		cscpUserDTO.setUsername( entity.getUsername() );	
		cscpUserDTO.setPassword( entity.getPassword() );

        return cscpUserDTO;
    }

    @Override
    public List<CscpUser> toEntity(List<CscpUserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpUserDTO> toDto(List<CscpUser> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
