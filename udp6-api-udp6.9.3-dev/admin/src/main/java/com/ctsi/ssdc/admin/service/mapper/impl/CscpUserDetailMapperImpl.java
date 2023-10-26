package com.ctsi.ssdc.admin.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.CscpUserDetail;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpUserDetailMapper;

/**
 * Mapper for the entity CscpUserDetail and its DTO CscpUserDetailDTO.
 *
 * @author ctsi biyi generator
 *
 */
@Component
public class CscpUserDetailMapperImpl implements CscpUserDetailMapper {

    @Override
    public CscpUserDetail toEntity(CscpUserDetailDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CscpUserDetail cscpUserDetail = new CscpUserDetail();
		cscpUserDetail.setId( dto.getId() );		
		cscpUserDetail.setUserId( dto.getUserId() );		
		cscpUserDetail.setFamilyName( dto.getFamilyName() );		
		cscpUserDetail.setName( dto.getName() );		
		cscpUserDetail.setMobile( dto.getMobile() );		
		cscpUserDetail.setEmail( dto.getEmail() );		
		cscpUserDetail.setImgPath( dto.getImgPath() );		
		cscpUserDetail.setLastLogin( dto.getLastLogin() );		
		cscpUserDetail.setDiscTitle( dto.getDiscTitle() );		
		cscpUserDetail.setDiscDetail( dto.getDiscDetail() );		
		cscpUserDetail.setRegisterTime( dto.getRegisterTime() );
		cscpUserDetail.setAdminFlag( dto.getAdminFlag());
        cscpUserDetail.setDeptId(dto.getDeptId());
        cscpUserDetail.setTenantId(dto.getTenantId());
        return cscpUserDetail;
    }

    @Override
    public CscpUserDetailDTO toDto(CscpUserDetail entity) {
        if ( entity == null ) {
            return null;
        }

        CscpUserDetailDTO cscpUserDetailDTO = new CscpUserDetailDTO();

		cscpUserDetailDTO.setId( entity.getId() );	
		cscpUserDetailDTO.setUserId( entity.getUserId() );	
		cscpUserDetailDTO.setFamilyName( entity.getFamilyName() );	
		cscpUserDetailDTO.setName( entity.getName() );	
		cscpUserDetailDTO.setMobile( entity.getMobile() );	
		cscpUserDetailDTO.setEmail( entity.getEmail() );	
		cscpUserDetailDTO.setImgPath( entity.getImgPath() );	
		cscpUserDetailDTO.setLastLogin( entity.getLastLogin() );	
		cscpUserDetailDTO.setDiscTitle( entity.getDiscTitle() );	
		cscpUserDetailDTO.setDiscDetail( entity.getDiscDetail() );	
		cscpUserDetailDTO.setRegisterTime( entity.getRegisterTime() );
		cscpUserDetailDTO.setAdminFlag( entity.getAdminFlag());
		cscpUserDetailDTO.setUsername(entity.getUsername());
		cscpUserDetailDTO.setDeptId(entity.getDeptId());
       cscpUserDetailDTO.setTenantId(entity.getTenantId());

        return cscpUserDetailDTO;
    }

    @Override
    public List<CscpUserDetail> toEntity(List<CscpUserDetailDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<CscpUserDetailDTO> toDto(List<CscpUserDetail> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CscpUser toCscpUser(CscpUserDetailDTO dto) {
        // TODO Auto-generated method stub
        CscpUser user = new CscpUser();
        user.setUsername(dto.getUsername());
        return user;
    }
    
    
}
