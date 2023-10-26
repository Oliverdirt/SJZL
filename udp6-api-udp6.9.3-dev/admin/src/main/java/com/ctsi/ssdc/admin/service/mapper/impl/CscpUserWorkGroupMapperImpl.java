package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpUserWorkGroup;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpUserWorkGroupMapper;

/**
 * Mapper for the entity CscpUserWorkGroup and its DTO CscpUserWorkGroupDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpUserWorkGroupMapperImpl implements CscpUserWorkGroupMapper {

    @Override
    public CscpUserWorkGroup toEntity(CscpUserWorkGroupDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpUserWorkGroup cscpUserWorkGroup = new CscpUserWorkGroup();

		cscpUserWorkGroup.setId( dto.getId() );		
		cscpUserWorkGroup.setUserId( dto.getUserId() );		
		cscpUserWorkGroup.setGroupId( dto.getGroupId() );		

        return cscpUserWorkGroup;
    }

    @Override
    public CscpUserWorkGroupDTO toDto(CscpUserWorkGroup entity) {
        if ( entity == null ) {
            return null;
        }

        CscpUserWorkGroupDTO cscpUserWorkGroupDTO = new CscpUserWorkGroupDTO();

		cscpUserWorkGroupDTO.setId( entity.getId() );	
		cscpUserWorkGroupDTO.setUserId( entity.getUserId() );	
		cscpUserWorkGroupDTO.setGroupId( entity.getGroupId() );	
       

        return cscpUserWorkGroupDTO;
    }

    @Override
    public List<CscpUserWorkGroup> toEntity(List<CscpUserWorkGroupDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpUserWorkGroupDTO> toDto(List<CscpUserWorkGroup> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
