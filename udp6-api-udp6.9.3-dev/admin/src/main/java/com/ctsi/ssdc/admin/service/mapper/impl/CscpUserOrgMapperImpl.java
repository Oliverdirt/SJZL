package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpUserOrg;
import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpUserOrgMapper;

/**
 * Mapper for the entity CscpUserOrg and its DTO CscpUserOrgDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpUserOrgMapperImpl implements CscpUserOrgMapper {

    @Override
    public CscpUserOrg toEntity(CscpUserOrgDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpUserOrg cscpUserOrg = new CscpUserOrg();

		cscpUserOrg.setId( dto.getId() );		
		cscpUserOrg.setUserId( dto.getUserId() );		
		cscpUserOrg.setOrgId( dto.getOrgId() );		

        return cscpUserOrg;
    }

    @Override
    public CscpUserOrgDTO toDto(CscpUserOrg entity) {
        if ( entity == null ) {
            return null;
        }

        CscpUserOrgDTO cscpUserOrgDTO = new CscpUserOrgDTO();

		cscpUserOrgDTO.setId( entity.getId() );	
		cscpUserOrgDTO.setUserId( entity.getUserId() );	
		cscpUserOrgDTO.setOrgId( entity.getOrgId() );	
       

        return cscpUserOrgDTO;
    }

    @Override
    public List<CscpUserOrg> toEntity(List<CscpUserOrgDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpUserOrgDTO> toDto(List<CscpUserOrg> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
