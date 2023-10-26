package com.ctsi.ssdc.gen.service.impl;

import com.ctsi.ssdc.enums.FieldValidateEnum;
import com.ctsi.ssdc.enums.MysqlTypeEnum;
import com.ctsi.ssdc.enums.QueryTypeEnum;
import com.ctsi.ssdc.enums.ShowTypeEnum;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuite;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxFormTableExample;
import com.ctsi.ssdc.gen.repository.CodeTemplateSuiteRepository;
import com.ctsi.ssdc.gen.repository.CscpHxFormColumnRepository;
import com.ctsi.ssdc.gen.repository.CscpHxFormTableRepository;
import com.ctsi.ssdc.gen.service.CscpHxCodeGenService;
import com.ctsi.ssdc.gen.util.FileUtil;
import com.ctsi.ssdc.gen.util.VelocityInitializer;
import com.ctsi.ssdc.gen.util.VelocityUtils;
import com.ctsi.ssdc.poi.excel.util.PathUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-02-23 15:35
 */
@Service
@Slf4j
public class CscpHxCodeGenServiceImpl implements CscpHxCodeGenService {

  @Resource private CscpHxFormTableRepository cscpHxFormTableRepository;
  @Resource CscpHxFormColumnRepository cscpHxFormColumnRepository;

  @Resource CodeTemplateSuiteRepository codeTemplateSuiteRepository;

  @Value("${gen.author:hx}")
  private String author;

  @Override
  public byte[] downloadCode(Long tableId) {

    // 查询表信息
    CscpHxFormTable cscpHxFormTable = initInfo(tableId);

    if (cscpHxFormTable.getGenType().equals(1)) {
      // 生成文件到指定路径
      generatorFile(cscpHxFormTable);
      return null;
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zip = new ZipOutputStream(outputStream);
    generatorCode(cscpHxFormTable, zip);
    IOUtils.closeQuietly(zip);
    return outputStream.toByteArray();
  }

  @Override
  public Map<String, String> preview(Long tableId) {
    Map<String, String> dataMap = new LinkedHashMap<>();
    // 查询表信息
    CscpHxFormTable cscpHxFormTable = initInfo(tableId);
    // 初始化类加载器
    VelocityInitializer.initCustomerVelocity();
    // 获取模板列表
    List<String> templates = getTemplates(cscpHxFormTable);

    // 初始化数据内容
    VelocityContext context = VelocityUtils.prepareContext(cscpHxFormTable);

    // 渲染模板
    for (String template : templates) {

      StringWriter sw = new StringWriter();
      Template tpl = Velocity.getTemplate(template, "UTF-8");
      tpl.merge(context, sw);
      dataMap.put(template, sw.toString());
    }
    return dataMap;
  }

  /**
   * 获取模板列表
   *
   * @param cscpHxFormTable
   * @return
   */
  private List<String> getTemplates(CscpHxFormTable cscpHxFormTable) {
    Long formSuite = cscpHxFormTable.getFormSuite();
    List<String> templates = new ArrayList<>();
    if (null != formSuite && formSuite.equals(1L)) {
      // 获取内置mybatis模板列表
      templates = VelocityUtils.getMybatisTemplateList(cscpHxFormTable.getFormType());
    } else if (null != formSuite && formSuite.equals(2L)) {
      // 获取内置mybatis plus模板列表
      templates = VelocityUtils.getMybatisPlusTemplateList(cscpHxFormTable.getFormType());

    } else if (null != formSuite) {
      // 获取自定义模板路径
      CodeTemplateSuite codeTemplateSuite =
          codeTemplateSuiteRepository.selectByPrimaryKey(formSuite);
      try {
        // 获取自定模板列表
        templates = FileUtil.readfile(codeTemplateSuite.getSuitePath(), templates);
        // 文件过滤
        templates =
            VelocityUtils.getTemplateListByFormType(cscpHxFormTable.getFormType(), templates);

      } catch (IOException e) {
        log.error("模板读取失败");
      }
    }
    return templates;
  }

  /**
   * 数据表信息初始化
   *
   * @param tableId
   * @return
   */
  private CscpHxFormTable initInfo(Long tableId) {
    // 查询表信息
    CscpHxFormTable table = cscpHxFormTableRepository.selectByPrimaryKey(tableId);

    if (null != table) {
      // 设置校验初始值
      table.setIsFeildValid(0);
      // 查询列信息
      List<CscpHxFormColumn> cscpHxFormColumns =
          cscpHxFormColumnRepository.selectByTableId(table.getTableId());

      if (null == cscpHxFormColumns) {
        return null;
      }
      initColumnInfo(table, cscpHxFormColumns);
      // 填充className
      table.setClassName(HxStringUtils.convertToCamelCase(table.getTableName()));

      // 作者
      table.setFunctionAuthor(author);

      // 设置主子表信息
      if (HxStringUtils.isNotEmpty(table.getSubTableName())
          && HxStringUtils.isNotEmpty(table.getSubTableFkName())) {
        initSubTable(table, tableId);
      }
      return table;
    }
    return null;
  }

  /**
   * 列数据内容初始化
   *
   * @param table
   * @param cscpHxFormColumns
   */
  private void initColumnInfo(CscpHxFormTable table, List<CscpHxFormColumn> cscpHxFormColumns) {
    // 遍历列信息填充字段
    cscpHxFormColumns.parallelStream()
        .forEach(
            (v) -> {
              // 设置主键
              if (v.getIsPk() == 1) {
                table.setPkColumn(v);
              }

              if (null != v.getValidateRule() && !"".equals(v.getValidateRule().trim())) {
                // 修改校验值
                table.setIsFeildValid(1);
              } else {
                v.setValidateRule("");
              }
              if (StringUtils.isNoneBlank(v.getValidateRule())) {
                // 设置rulevalue
                v.setRulevalue(FieldValidateEnum.getByRuleCode(v.getValidateRule()).getRulevalue());
                // 设置ruleinfo
                v.setRuleinfo(FieldValidateEnum.getByRuleCode(v.getValidateRule()).getRuleinfo());
              }
              // 设置javafield
              v.setJavaField(HxStringUtils.toCamelCase(v.getColumnName()));
              // 设置javaType
              v.setJavaType(MysqlTypeEnum.getJavaTypeByNum(v.getColumnType()));
              // 设置jdbcType
              v.setJdbcType(MysqlTypeEnum.getJdbcTypeByNum(v.getColumnType()));
              // 初始化列备注，如果没有填写，默认字段小驼峰
              if (null == v.getColumnComment() || "".equals(v.getColumnComment().trim())) {
                v.setColumnComment(HxStringUtils.convertToCamelCase(v.getColumnName()));
              }
              if (null != v.getShowType()) {
                v.setHtmlType(ShowTypeEnum.getNameByNum(v.getShowType()));
              }
              if (null != v.getQueryType()) {
                v.setQueryType(QueryTypeEnum.getByNum(Integer.valueOf(v.getQueryType())).getType());
              }
            });

    // 表信息填充
    // 填充列
    table.setColumns(cscpHxFormColumns);
  }

  /**
   * 初始化子表内容
   *
   * @param table
   * @param tableId
   */
  private void initSubTable(CscpHxFormTable table, Long tableId) {

    // 查询子表信息
    String subTableName = table.getSubTableName();
    CscpHxFormTableExample cscpHxFormTableExample = new CscpHxFormTableExample();
    cscpHxFormTableExample.createCriteria().andTableNameEqualTo(subTableName);
    CscpHxFormTable subCscpHxFormTables =
        cscpHxFormTableRepository.selectByExample(cscpHxFormTableExample).get(0);

    // 子表列封装
    List<CscpHxFormColumn> subCscpHxFormColumnsList =
        cscpHxFormColumnRepository.selectByTableId(subCscpHxFormTables.getTableId());
    if (subCscpHxFormColumnsList.size() > 0) {
      initColumnInfo(subCscpHxFormTables, subCscpHxFormColumnsList);
    }
    // 填充className
    subCscpHxFormTables.setClassName(
        HxStringUtils.convertToCamelCase(subCscpHxFormTables.getTableName()));
    table.setSubTable(subCscpHxFormTables);
  }

  /**
   * 生成代码到zip
   *
   * @param cscpHxFormTable
   * @param zip
   */
  private void generatorCode(CscpHxFormTable cscpHxFormTable, ZipOutputStream zip) {

    //        VelocityInitializer.initVelocity();
    VelocityInitializer.initCustomerVelocity();

    VelocityContext context = VelocityUtils.prepareContext(cscpHxFormTable);

    // 获取模板列表
    // 获取模板列表
    List<String> templates = getTemplates(cscpHxFormTable);
    //        List<String> templates = VelocityUtils.getTemplateList(cscpHxFormTable.getFormType());
    for (String template : templates) {
      // 渲染模板
      StringWriter sw = new StringWriter();
      Template tpl = Velocity.getTemplate(template, "UTF-8");
      tpl.merge(context, sw);
      try {
        // 添加到zip
        zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, cscpHxFormTable)));
        IOUtils.write(sw.toString(), zip, "UTF-8");
        IOUtils.closeQuietly(sw);
        zip.flush();
        zip.closeEntry();
      } catch (IOException e) {
        log.error("渲染模板失败");
      }
    }
  }

  /**
   * 生成代码到本地目录
   *
   * @param cscpHxFormTable
   */
  private void generatorFile(CscpHxFormTable cscpHxFormTable) {

    // 获取模板列表
    List<String> templates = getTemplates(cscpHxFormTable);
    //        List<String> templates = VelocityUtils.getTemplateList(cscpHxFormTable.getFormType());
    //        VelocityInitializer.initVelocity();
    VelocityInitializer.initCustomerVelocity();
    VelocityContext context = VelocityUtils.prepareContext(cscpHxFormTable);
    for (String template : templates) {
      // 渲染模板
      StringWriter sw = new StringWriter();
      Template tpl = Velocity.getTemplate(template, "UTF-8");
      tpl.merge(context, sw);
      try {
        String path = getGenPath(cscpHxFormTable, template);
        FileUtils.writeStringToFile(
            new File(PathUtil.filePathFilter(path)), sw.toString(), "UTF-8");
      } catch (IOException e) {
        log.error("渲染模板失败");
      }
    }
  }

  public static String getGenPath(CscpHxFormTable cscpHxFormTable, String template) {
    return cscpHxFormTable.getGenPath()
        + File.separator
        + VelocityUtils.getFileName(template, cscpHxFormTable);
  }
}
