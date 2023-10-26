package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpOrg;
import com.ctsi.ssdc.admin.domain.dto.CscpOrgDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpOrgMapper;

/**
 * Mapper for the entity CscpOrg and its DTO CscpOrgDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpOrgMapperImpl implements CscpOrgMapper {

    @Override
    public CscpOrg toEntity(CscpOrgDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpOrg cscpOrg = new CscpOrg();

		cscpOrg.setId( dto.getId() );		
		cscpOrg.setOrgName( dto.getOrgName() );		
		cscpOrg.setDescription( dto.getDescription() );		
		cscpOrg.setParentId( dto.getParentId() );
		cscpOrg.setOrderby( dto.getOrderby());
        return cscpOrg;
    }

    @Override
    public CscpOrgDTO toDto(CscpOrg entity) {
        if ( entity == null ) {
            return null;
        }

        CscpOrgDTO cscpOrgDTO = new CscpOrgDTO();

		cscpOrgDTO.setId( entity.getId() );	
		cscpOrgDTO.setOrgName( entity.getOrgName() );	
		cscpOrgDTO.setDescription( entity.getDescription() );	
		cscpOrgDTO.setParentId( entity.getParentId() );
        cscpOrgDTO.setOrderby( entity.getOrderby());
       

        return cscpOrgDTO;
    }

    @Override
    public List<CscpOrg> toEntity(List<CscpOrgDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpOrgDTO> toDto(List<CscpOrg> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
