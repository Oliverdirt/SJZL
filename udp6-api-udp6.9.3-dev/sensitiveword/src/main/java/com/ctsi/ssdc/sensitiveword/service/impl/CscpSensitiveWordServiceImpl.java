package com.ctsi.ssdc.sensitiveword.service.impl;



import cn.hutool.core.util.StrUtil;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordExample;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordType;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordTypeExample;
import com.ctsi.ssdc.sensitiveword.repository.CscpSensitiveWordRepository;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordService;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordTypeService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Service Implementation for managing CscpSensitiveWord.
 *
 * @author ctsi-biyi-generator
 */
@Service
public class CscpSensitiveWordServiceImpl
        extends StrengthenBaseServiceImpl<CscpSensitiveWordRepository, CscpSensitiveWord, Long, CscpSensitiveWordExample>
        implements CscpSensitiveWordService {

    @Autowired
    private CscpSensitiveWordTypeService cscpSensitiveWordTypeService;

    @Resource
    private CscpSensitiveWordRepository cscpSensitiveWordRepository;

    /**
     * 通过typeid删除敏感词
     */
    public void deleteByTypeId(Long senTypeId) {
        CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
        cscpSensitiveWordExample.createCriteria().andSenTypeIdEqualTo(senTypeId);
        r.deleteByExample(cscpSensitiveWordExample);
    }

    @Override
    @Transactional
    public void saveUploadFile(InputStream inputStream) {
        try {
            Workbook source = new XSSFWorkbook(inputStream);
            Sheet sheet = source.getSheetAt(0);
            int rowCount = sheet.getLastRowNum() + 1;
            int rowL = -1;
            Map<String, List<CscpSensitiveWord>> datas = new HashMap<>();
            for (int i = 0; i < rowCount; ) {
                int mergedRegion = isMergedRegion(sheet, i, 0);
                if (mergedRegion != -1) {
                    String sensitiveType = getMergedRegionValue(sheet, i, 0);
                    if(null != sensitiveType && StrUtil.isNotEmpty(sensitiveType)){
                        List<CscpSensitiveWord> ls = new ArrayList<>();
                        for (int ik = i; ik <= mergedRegion; ik++) {
                            Row row = sheet.getRow(ik);
                            Cell sk = row.getCell(1);
                            Cell sv = row.getCell(2);
                            String sensitiveKey = this.getCellValue(sk);
                            String sensitiveValue = this.getCellValue(sv);
                            CscpSensitiveWord word = new CscpSensitiveWord();
                            word.setSenContent(sensitiveKey);
                            word.setSenReplace(sensitiveValue);
                            ls.add(word);
                        }
                        if(ls.size()>0){
                            datas.put(sensitiveType, ls);
                        }

                    }
                    i = mergedRegion;
                    i++;
                } else {
                    Row row = sheet.getRow(i);
                    Cell k = row.getCell(0);
                    Cell sk = row.getCell(1);
                    Cell sv = row.getCell(2);
                    String sensitiveType = this.getCellValue(k);
                    String sensitiveKey = this.getCellValue(sk);
                    String sensitiveValue = this.getCellValue(sv);
                    if(null != sensitiveType && StrUtil.isNotEmpty(sensitiveType) && StrUtil.isNotEmpty(sensitiveKey)){
                        CscpSensitiveWord word = new CscpSensitiveWord();
                        word.setSenContent(sensitiveKey);
                        word.setSenReplace(sensitiveValue);
                        List<CscpSensitiveWord> ls = new ArrayList<>();
                        ls.add(word);
                        datas.put(sensitiveType, ls);
                    }
                    i++;
                }

            }
            if (!datas.isEmpty()) {
                for (Map.Entry<String, List<CscpSensitiveWord>> entry : datas.entrySet()) {
                    String k = entry.getKey();
                    CscpSensitiveWordTypeExample example = new CscpSensitiveWordTypeExample();
                    StringCriteria equalsName = new StringCriteria();
                    equalsName.setEquals(k);
                    example.setSenTypeName(equalsName);
                    PageResult<CscpSensitiveWordType> byExample = cscpSensitiveWordTypeService.findByExample(example);
                    List<CscpSensitiveWordType> data = byExample.getData();
                    CscpSensitiveWordType type = null;
                    if (data == null || data.size() < 1) {
                        type = new CscpSensitiveWordType();
                        type.setSenTypeName(k);
                        type.setUpdateTime(new Date());
                        type.setSenTypeOrder(1);
                        if(StrUtil.isNotEmpty(k)){
                            this.cscpSensitiveWordTypeService.insert(type);
                        }
                    } else {
                        type = data.get(0);
                    }
                    // 数据去重
                    Set<CscpSensitiveWord> cscpSensitiveWordSet = new HashSet<>(entry.getValue());
                    // 根据type获取该类型所有的值
                    for (CscpSensitiveWord word : cscpSensitiveWordSet) {
                        // 重复性判断
                        CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
                        cscpSensitiveWordExample.createCriteria().andSenContentEqualTo(word.getSenContent());
                        long countSensitiveWord = cscpSensitiveWordRepository.countByExample(cscpSensitiveWordExample);
                        if(countSensitiveWord==0){
                            word.setSenTypeId(type.getId());
                            word.setSenTypeName(k);
                            word.setUpdateTime(new Date());
                            this.insert(word);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("导入数据异常");
        }
    }

    @Override
    public AjaxResult deleteByIds(Long[] ids) {
        // 校验
        cscpSensitiveWordRepository.deleteByIds(ids);
        return AjaxResult.success("批量删除类型成功");
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }


    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    private int isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return lastRow;
                }
            }
        }
        return -1;
    }


}
