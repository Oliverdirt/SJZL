package com.ctsi.ssdc.admin.service.mapper;

import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.CscpUserDetail;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;

/**
 * Mapper for the entity CscpUserDetail and its DTO CscpUserDetailDTO.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserDetailMapper extends EntityMapper<CscpUserDetailDTO, CscpUserDetail> {


    public CscpUser toCscpUser(CscpUserDetailDTO dto);

//    default CscpUserDetail fromId(Integer id) {
//        CscpUserDetail cscpUserDetail = new CscpUserDetail();
//        cscpUserDetail.setId(id);
//        return cscpUserDetail;
//    }
}
