package com.ctsi.ssdc.base.system.service.impl;

import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.api.center.util.ExcelUtils;
import com.ctsi.ssdc.base.system.service.ExportInOrOutService;
import com.ctsi.ssdc.model.R;
import com.ctsi.ssdc.security.SecurityHxUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description
 * @Author Len
 * @Date 2023/7/14 9:26
 */
@Service
public class ExportInOrOutServiceImpl implements ExportInOrOutService {

    @Autowired
    private CscpRolesService cscpRolesService;

    @Autowired
    private CscpUserDetailService cscpUserDetailService;

    @Override
    public void userTemplateDownload(HttpServletResponse response) {
        //设置列名
        String[] columnNames = {"姓名", "登录账号", "手机号", "证件号码"};
        //定义Workbook
        Workbook workbook = null;
        try {
            //调用ExcelUtil创建excel表格并填入数据
            workbook = ExcelUtils.createExcelTemplate(columnNames, null, null);
            // 设置response参数，可以打开下载页面
            //准备将Excel的输出流通过response输出到页面下载
            //八进制输出流
            response.setContentType("application/octet-stream");
            //设置导出Excel的名称
            response.setHeader("Content-disposition", "attachment;filename=hospital.xls");
            //刷新缓冲
            ServletOutputStream outputStream = response.getOutputStream();
            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void roleTemplateDownload(HttpServletResponse response) {
        //设置列名
        String[] columnNames = {"角色名称", "角色编码"};
        //定义Workbook
        Workbook workbook = null;
        try {
            //调用ExcelUtil创建excel表格并填入数据
            workbook = ExcelUtils.createExcelTemplate(columnNames, null, null);
            // 设置response参数，可以打开下载页面
            //准备将Excel的输出流通过response输出到页面下载
            //八进制输出流
            response.setContentType("application/octet-stream");
            //设置导出Excel的名称
            response.setHeader("Content-disposition", "attachment;filename=hospital.xls");
            //刷新缓冲
            ServletOutputStream outputStream = response.getOutputStream();
            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public R<String> uploadUserInfo(MultipartFile file) {
        CreateWorkbook createWorkbook = new CreateWorkbook(file).invoke();
        if (createWorkbook.is()) {
            return R.failed("导入失败");
        }
        Workbook workbook = createWorkbook.getWorkbook();
        //遍历表格中的数据
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            //获取单行数据
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            // 循环行中列Row
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                //获取单行数据
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    //列1值
                    Cell c1 = row.getCell(0);
                    //列2值
                    Cell c2 = row.getCell(1);
                    //列1值
                    Cell c3 = row.getCell(2);
                    //列2值
                    Cell c4 = row.getCell(3);

                    CscpUserDetailDTO dto = new CscpUserDetailDTO();
                    if (c1 != null) {
                        dto.setName(c1.toString());
                    }
                    if (c2 != null) {
                        dto.setUsername(c2.toString());
                    }
                    if (c3 != null) {
                        dto.setMobile(c3.toString());
                    }
                    if (c4 != null) {
                        dto.setIdCard(c4.toString());
                    }
                    dto.setCardType("99");
                    cscpUserDetailService.insert(dto);
                }
            }
        }
        return R.ok("导入成功");
    }

    @Override
    public R<String> uploadRole(MultipartFile multipartFile) {
        Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
        CreateWorkbook createWorkbook = new CreateWorkbook(multipartFile).invoke();
        if (createWorkbook.is()) {
            return R.failed("导入失败");
        }
        Workbook workbook = createWorkbook.getWorkbook();
        //遍历表格中的数据
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            //获取单行数据
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            // 循环行中列Row
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                //获取单行数据
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    //列1值
                    Cell c1 = row.getCell(0);
                    //列2值
                    Cell c2 = row.getCell(1);
                    CscpRolesDTO roles = new CscpRolesDTO();
                    roles.setName(c1.toString());
                    if (c2 != null) {
                        roles.setRoleCode(c2.toString());
                    }
                    roles.setTenantId(currentTenantId);
                    cscpRolesService.insert(roles);
                }
            }
        }
        return R.ok("导入成功");
    }

    private class CreateWorkbook {
        private boolean myResult;
        private MultipartFile multipartFile;
        private Workbook workbook;

        public CreateWorkbook(MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
        }

        boolean is() {
            return myResult;
        }

        public Workbook getWorkbook() {
            return workbook;
        }

        public CreateWorkbook invoke() {
            if (multipartFile == null) {
                myResult = true;
                return this;
            }
            //获取上传文件的文件名
            String fileName = multipartFile.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                myResult = true;
                return this;
            }
            workbook = null;
            XSSFWorkbook xssfWorkbook = null;
            HSSFWorkbook hssfWorkbook = null;
            try {
                //获取导入的文件
                InputStream inputStream = multipartFile.getInputStream();
                if (fileName.endsWith("xlsx")) {
                    //创建Excel表格--Excel 2007
                    xssfWorkbook = new XSSFWorkbook(inputStream);
                    workbook = xssfWorkbook;
                } else if (fileName.endsWith("xls")) {
                    //创建Excel表格--Excel 2003
                    hssfWorkbook = new HSSFWorkbook(inputStream);
                    workbook = hssfWorkbook;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (workbook == null) {
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }
}
