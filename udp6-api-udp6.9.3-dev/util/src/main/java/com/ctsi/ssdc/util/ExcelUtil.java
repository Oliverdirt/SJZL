package com.ctsi.ssdc.util;

import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * excel导出类
 * @author ctsi
 *
 */
public class ExcelUtil {
	/**
	 * 根据LIST导出EXCEL
	 * @param l 数据集合
	 * @param c 类型
	 * @param coldefined 列配置
	 * @return excel流
	 */
	

	public static ByteArrayInputStream exportExcelByList(List l, Class c, List<ExcelColDefine> coldefined) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			// head
			int rowcount = 0;
			HSSFRow rowhead = sheet.createRow(rowcount++);
			int col = 0;
			for (int i = 0; i < coldefined.size(); i++) {
				if (  coldefined.get(i).getIsHide()!=null && coldefined.get(i).getIsHide() == ExcelColDefine.COL_HIDDEN ){
					continue;
				}
				HSSFCell cell = rowhead.createCell(col++);
				cell.setCellValue(coldefined.get(i).getHead());
			}

			// body
			// data
			for (int i = 0; i < l.size(); i++) {
				HSSFRow row = sheet.createRow(rowcount++);
				col = 0;
				//Object[] r = (Object[]) l.get(i);
				for (int j = 0; j < coldefined.size(); j++) {
					if ( coldefined.get(j).getIsHide()!=null && coldefined.get(j).getIsHide() == ExcelColDefine.COL_HIDDEN){
						continue;
					}
					
					HSSFCell cell = row.createCell(col++);
					Object r = getPropertyValue(l.get(i), c, coldefined.get(j).getProperty());
					cell.setCellValue(r == null ? "" : r.toString());
				}
			}
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			workbook.write(o);
			o.flush();
			workbook.close();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(o.toByteArray());
			return inputStream;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * 导出EXCEL
	 * @param l 数据集合
	 * @param cols 列名
	 * @return excel流
	 */
	
	public static ByteArrayInputStream exportExcelByList(List l, List cols) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			// head
			int rowcount = 0;
			HSSFRow rowhead = sheet.createRow(rowcount++);
			int col = 0;
			for (int i = 0; i < cols.size(); i++) {
				HSSFCell cell = rowhead.createCell(col++);
				cell.setCellValue((String) cols.get(i));
			}

			// body
			// data
			for (int i = 0; i < l.size(); i++) {
				HSSFRow row = sheet.createRow(rowcount++);
				col = 0;
				JSONObject r = (JSONObject) l.get(i);
				for (int j = 0; j < cols.size(); j++) {
					HSSFCell cell = row.createCell(col++);
					Object rx = r.get(cols.get(j));
					cell.setCellValue(rx == null ? "" : rx.toString());
				}
			}
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			workbook.write(o);
			o.flush();
			
			workbook.close();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(o.toByteArray());
			return inputStream;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * 导出EXCEL
	 * @param l 数据集合
	 * @param cols 列名
	 * @param hiddenCols 忽略列名
	 * @return excel流
	 */
	
	public static ByteArrayInputStream exportExcelByList(List l, List cols, List hiddenCols) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            // head
            int rowcount = 0;
            HSSFRow rowhead = sheet.createRow(rowcount++);
            int col = 0;
            for (int i = 0; i < cols.size(); i++) {
                if(hiddenCols.contains(cols.get(i))){
                    continue;
                }
                
                HSSFCell cell = rowhead.createCell(col++);
                cell.setCellValue((String) cols.get(i));
            }

            // body
            // data
            for (int i = 0; i < l.size(); i++) {
                HSSFRow row = sheet.createRow(rowcount++);
                col = 0;
                JSONObject r = (JSONObject) l.get(i);
                for (int j = 0; j < cols.size(); j++) {
                    if(hiddenCols.contains(cols.get(j))){
                        continue;
                    }
                    HSSFCell cell = row.createCell(col++);
                    Object rx = r.get(cols.get(j));
                    cell.setCellValue(rx == null ? "" : rx.toString());
                }
            }
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            workbook.write(o);
            o.flush();

            workbook.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(o.toByteArray());
            return inputStream;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 获取属性
	 * @param o 对象
	 * @param c 类型
	 * @param p 属性名
	 * @return 属性值
	 */
	private static Object getPropertyValue(Object o, Class c, String p){
		try{
			Field fds = c.getDeclaredField(p);
			fds.setAccessible(true);
			Object v = fds.get(o);
			
			if(v != null && v instanceof Date){
				Date d = (Date) v;
				return DateUtil.formatDateObject3(d);
			}
			return v;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
