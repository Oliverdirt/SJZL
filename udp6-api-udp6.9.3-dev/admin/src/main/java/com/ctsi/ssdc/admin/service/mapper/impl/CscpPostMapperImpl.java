package com.ctsi.ssdc.admin.service.mapper.impl;

import com.ctsi.ssdc.admin.domain.CscpPost;
import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.domain.dto.CscpPostDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpPostMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CscpPostMapperImpl implements CscpPostMapper {
    @Override
    public CscpPost toEntity(CscpPostDTO dto) {
        if (dto == null) {
            return null;
        }
        CscpPost cscpPost = new CscpPost();
        cscpPost.setPostId(dto.getPostId());
        cscpPost.setPostCode(dto.getPostCode());
        cscpPost.setPostName(dto.getPostName());
        cscpPost.setPostSort(dto.getPostSort());
        cscpPost.setStatus(dto.getStatus());
        cscpPost.setCreateBy(dto.getCreateBy());
        cscpPost.setCreateTime(dto.getCreateTime());
        cscpPost.setUpdateBy(dto.getUpdateBy());
        cscpPost.setRemark(dto.getRemark());
        cscpPost.setTenantId(dto.getTenantId());
        return cscpPost;
    }

    @Override
    public CscpPostDTO toDto(CscpPost entity){
        if ( entity == null ) {
            return null;
        }
        CscpPostDTO cscpPostDTO = new CscpPostDTO();
        cscpPostDTO.setPostId(entity.getPostId());
        cscpPostDTO.setPostCode(entity.getPostCode());
        cscpPostDTO.setPostName(entity.getPostName());
        cscpPostDTO.setPostSort(entity.getPostSort());
        cscpPostDTO.setStatus(entity.getStatus());
        cscpPostDTO.setCreateBy(entity.getCreateBy());
        cscpPostDTO.setCreateTime(entity.getCreateTime());
        cscpPostDTO.setUpdateBy(entity.getUpdateBy());
        cscpPostDTO.setUpdateTime(entity.getUpdateTime());
        cscpPostDTO.setRemark(entity.getRemark());
        cscpPostDTO.setTenantId(entity.getTenantId());
        return cscpPostDTO;
    }

    @Override
    public List<CscpPost> toEntity(List<CscpPostDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public List<CscpPostDTO> toDto(List<CscpPost> entityList) {
        if ( entityList == null ) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
