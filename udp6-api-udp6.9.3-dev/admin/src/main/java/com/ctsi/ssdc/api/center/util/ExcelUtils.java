package com.ctsi.ssdc.api.center.util;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/7 14:26
 */

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExcelUtils {
    /**
     * 创建excel文档，
     */
    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String columnNames[]) {
        // 创建excel工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }
        // 创建第一行
        Row row = sheet.createRow((short) 0);
        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 12);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        // 设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            // 在row行上创建一个方格
            for (int j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }

    /**
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     * @Description: 生成Excel导入模板
     */
    public static HSSFWorkbook createExcelTemplate(String[] handers,
                                                   List<String[]> downData, String[] downRows) {
        //创建工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //表头样式
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        // 创建一个居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 12);
        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(fontStyle);
        //新建sheet
        HSSFSheet sheet1 = wb.createSheet("Sheet1");
        HSSFSheet sheet2 = wb.createSheet("Sheet2");
        //生成sheet1内容
        //第一个sheet的第一行为标题
        HSSFRow rowFirst = sheet1.createRow(0);
        //写标题
        for (int i = 0; i < handers.length; i++) {
            //获取第一行的每个单元格
            HSSFCell cell = rowFirst.createCell(i);
            //设置每列的列宽
            sheet1.setColumnWidth(i, 4000);
            sheet1.setDefaultColumnStyle(i, style);
            //加样式
            cell.setCellStyle(style);
            //往单元格里写数据
            cell.setCellValue(handers[i]);
        }
        //设置下拉框数据
        String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int index = 0;
        HSSFRow row = null;
        if (downRows == null || downData == null) {
            return wb;
        }
        for (int r = 0; r < downRows.length; r++) {
            //获取下拉对象
            String[] dlData = downData.get(r);
            int rownum = Integer.parseInt(downRows[r]);
            //255以内的下拉
            if (dlData.length < 5) {
                //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                //超过255个报错
                sheet1.addValidationData(setDataValidation(sheet1, dlData, 1, 50000, rownum, rownum));
            } else { //255以上的下拉，即下拉列表元素很多的情况
                //1、设置有效性
                //String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                //Sheet2第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet2!$" + arr[index] + "$1:$" + arr[index] + "$5000";
                //设置每列的列宽
                sheet2.setColumnWidth(r, 4000);
                //设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                //下拉列表元素很多的情况
                sheet1.addValidationData(setDataValidation(strFormula, 1, 50000, rownum, rownum));
                //2、生成sheet2内容
                for (int j = 0; j < dlData.length; j++) {
                    //第1个下拉选项，直接创建行、列
                    if (index == 0) {
                        //创建数据行
                        row = sheet2.createRow(j);
                        //设置每列的列宽
                        sheet2.setColumnWidth(j, 4000);
                        //设置对应单元格的值
                        row.createCell(0).setCellValue(dlData[j]);

                    } else { //非第1个下拉选项

                        int rowCount = sheet2.getLastRowNum();
                        //前面创建过的行，直接获取行，创建列
                        if (j <= rowCount) {
                            //获取行，创建列
                            //设置对应单元格的值
                            sheet2.getRow(j).createCell(index).setCellValue(dlData[j]);

                        } else {
                            //未创建过的行，直接创建行、创建列
                            //设置每列的列宽
                            sheet2.setColumnWidth(j, 4000);
                            //创建行、创建列
                            //设置对应单元格的值
                            sheet2.createRow(j).createCell(index).setCellValue(dlData[j]);
                        }
                    }
                }
                index++;
            }
        }
        return wb;
    }

    /**
     * @param @param  strFormula
     * @param @param  firstRow   起始行
     * @param @param  endRow     终止行
     * @param @param  firstCol   起始列
     * @param @param  endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     * @Title: SetDataValidation
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     */
    private static HSSFDataValidation setDataValidation(String strFormula,
                                                        int firstRow, int endRow, int firstCol, int endCol) {
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);
        return dataValidation;
    }

    /**
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        constraint.setExplicitListValues(textList);
        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        return data_validation;
    }
}

