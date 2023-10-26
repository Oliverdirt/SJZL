package com.ctsi.flow.server.rule;

import com.ctsi.flow.param.model.CscpTableOperation;

import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/26 10:27  by xx
 */
public interface CscpTableOperationService {
    /*新增表单字段权限*/
    void addTableFieldPerm(CscpTableOperation cscpTableOperation);
    void updateTableFieldPerm(CscpTableOperation cscpTableOperation);
    List<CscpTableOperation> qryTableFieldPerm(CscpTableOperation cscpTableOperation);
//    List<CscpTableOperation> qryTableOperByFormIdAndFieldName(CscpTableOperation cscpTableOperation);
    void deleteTableFieldPerm(CscpTableOperation cscpTableOperation);
}
