package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrg;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpWorkGroupOrgMapper;

/**
 * Mapper for the entity CscpWorkGroupOrg and its DTO CscpWorkGroupOrgDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpWorkGroupOrgMapperImpl implements CscpWorkGroupOrgMapper {

    @Override
    public CscpWorkGroupOrg toEntity(CscpWorkGroupOrgDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpWorkGroupOrg cscpWorkGroupOrg = new CscpWorkGroupOrg();

		cscpWorkGroupOrg.setId( dto.getId() );		
		cscpWorkGroupOrg.setGroupId( dto.getGroupId() );		
		cscpWorkGroupOrg.setOrgId( dto.getOrgId() );		

        return cscpWorkGroupOrg;
    }

    @Override
    public CscpWorkGroupOrgDTO toDto(CscpWorkGroupOrg entity) {
        if ( entity == null ) {
            return null;
        }

        CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO = new CscpWorkGroupOrgDTO();

		cscpWorkGroupOrgDTO.setId( entity.getId() );	
		cscpWorkGroupOrgDTO.setGroupId( entity.getGroupId() );	
		cscpWorkGroupOrgDTO.setOrgId( entity.getOrgId() );	
       

        return cscpWorkGroupOrgDTO;
    }

    @Override
    public List<CscpWorkGroupOrg> toEntity(List<CscpWorkGroupOrgDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpWorkGroupOrgDTO> toDto(List<CscpWorkGroupOrg> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
