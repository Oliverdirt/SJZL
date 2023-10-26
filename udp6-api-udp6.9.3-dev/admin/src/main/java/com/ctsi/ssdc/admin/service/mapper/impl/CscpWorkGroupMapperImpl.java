package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpWorkGroup;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpWorkGroupMapper;

/**
 * Mapper for the entity CscpWorkGroup and its DTO CscpWorkGroupDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpWorkGroupMapperImpl implements CscpWorkGroupMapper {

    @Override
    public CscpWorkGroup toEntity(CscpWorkGroupDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpWorkGroup cscpWorkGroup = new CscpWorkGroup();

		cscpWorkGroup.setId( dto.getId() );		
		cscpWorkGroup.setGroupName( dto.getGroupName() );		
		cscpWorkGroup.setDescription( dto.getDescription() );		
		cscpWorkGroup.setOrgId( dto.getOrgId() );		

        return cscpWorkGroup;
    }

    @Override
    public CscpWorkGroupDTO toDto(CscpWorkGroup entity) {
        if ( entity == null ) {
            return null;
        }

        CscpWorkGroupDTO cscpWorkGroupDTO = new CscpWorkGroupDTO();

		cscpWorkGroupDTO.setId( entity.getId() );	
		cscpWorkGroupDTO.setGroupName( entity.getGroupName() );	
		cscpWorkGroupDTO.setDescription( entity.getDescription() );	
		cscpWorkGroupDTO.setOrgId( entity.getOrgId() );	
       

        return cscpWorkGroupDTO;
    }

    @Override
    public List<CscpWorkGroup> toEntity(List<CscpWorkGroupDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpWorkGroupDTO> toDto(List<CscpWorkGroup> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
