package com.ctsi.ssdc.gen.util;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.enums.FormTypeEnum;
import com.ctsi.ssdc.gen.constant.GenConstants;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.utils.HxStringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板处理工具类
 *
 * @author hx
 */
public class VelocityUtils {
  // 项目空间路径
  private static final String PROJECT_PATH = "main/java";

  // 默认mybatis 注解模板包路径
  private static final String MYBATIS_TEMPLATE_DIR = "mybatis";

  // 默认mybatis plus 模板包路径
  private static final String MYBATIS_PLUS_TEMPLATE_DIR = "mybatisplus";

  /**
   * 设置模板变量信息
   *
   * @return 模板列表
   */
  public static VelocityContext prepareContext(CscpHxFormTable cscpHxFormTable) {
    String packageName = cscpHxFormTable.getPackageName();

    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("formType", cscpHxFormTable.getFormType());
    velocityContext.put("tableName", cscpHxFormTable.getTableName());
    velocityContext.put("ClassName", cscpHxFormTable.getClassName());
    velocityContext.put("className", HxStringUtils.uncapitalize(cscpHxFormTable.getClassName()));
    velocityContext.put("packageName", packageName);
    velocityContext.put("author", cscpHxFormTable.getFunctionAuthor());
    velocityContext.put("pkColumn", cscpHxFormTable.getPkColumn());
    velocityContext.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    velocityContext.put("cappkColumn", cscpHxFormTable.getPkColumn());
    velocityContext.put("columns", cscpHxFormTable.getColumns());
    velocityContext.put("importList", VelocityUtils.getImportList(cscpHxFormTable));
    velocityContext.put("table", cscpHxFormTable);
    if (cscpHxFormTable.getFormType().equals(FormTypeEnum.TREE.getNumber())) {
      setTreeVelocityContext(velocityContext, cscpHxFormTable);
    }
    if (cscpHxFormTable.getFormType().equals(FormTypeEnum.SUB.getNumber())) {
      setSubVelocityContext(velocityContext, cscpHxFormTable);
    }
    return velocityContext;
  }

  public static void setTreeVelocityContext(
      VelocityContext context, CscpHxFormTable cscpHxFormTable) {
    String options = cscpHxFormTable.getOptions();
    JSONObject paramsObj = JSONObject.parseObject(options);

    String treeCode = getTreecode(paramsObj);
    String treeParentCode = getTreeParentCode(paramsObj);
    String treeName = getTreeName(paramsObj);

    context.put("treeParentCodeColumnName", paramsObj.getString(GenConstants.TREE_PARENT_CODE));
    context.put("treeCode", treeCode);
    context.put("treeParentCode", treeParentCode);
    context.put("treeName", treeName);
    context.put("TreeCode", HxStringUtils.convertToCamelCase(treeCode));
    context.put("TreeParentCode", HxStringUtils.convertToCamelCase(treeParentCode));
    context.put("TreeName", HxStringUtils.convertToCamelCase(treeName));
  }

  public static void setSubVelocityContext(VelocityContext context, CscpHxFormTable genTable) {
    String subTableFkName = genTable.getSubTableFkName();
    String subTableFkClassName = HxStringUtils.convertToCamelCase(subTableFkName);

    context.put("subTableFkName", subTableFkName);
    context.put("subTableFkClassName", subTableFkClassName);
    context.put("subTableFkclassName", HxStringUtils.uncapitalize(subTableFkClassName));
    context.put("subImportList", getImportList(genTable.getSubTable()));
  }

  /**
   * 设置公共模板列表
   * @param formType
   * @param templateDir
   * @return
   */
  public static List<String> prepareCommonTemplateList(Integer formType,String templateDir){

    List<String> templates = new ArrayList<String>();
    templates.add(HxStringUtils.format("vm/{}/java/Controller.java.vm",templateDir));
    templates.add(HxStringUtils.format("vm/{}/java/Domain.java.vm",templateDir));
    templates.add(HxStringUtils.format("vm/{}/java/Service.java.vm",templateDir));
    templates.add(HxStringUtils.format("vm/{}/java/ServiceImpl.java.vm",templateDir));
    if (formType.equals(FormTypeEnum.SINGLE.getNumber())) {
      templates.add(HxStringUtils.format("vm/{}/vue/SingleForm.vue.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/js/SingleForm.js.vm",templateDir));
    } else if (formType.equals(FormTypeEnum.SUB.getNumber())) {
      templates.add(HxStringUtils.format("vm/{}/vue/MainForm.vue.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/vue/SubForm.vue.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/js/MainForm.js.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/js/SubForm.js.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/java/SubController.java.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/java/SubDomain.java.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/java/SubService.java.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/java/SubServiceImpl.java.vm",templateDir));
    } else if (formType.equals(FormTypeEnum.TREE.getNumber())) {
      templates.add(HxStringUtils.format("vm/{}/vue/TreeForm.vue.vm",templateDir));
      templates.add(HxStringUtils.format("vm/{}/js/TreeForm.js.vm",templateDir));
    }
    return templates;
  }

  /**
   * 获取默认Mybatis注解模板信息
   *
   * @return 模板列表
   */
  public static List<String> getMybatisTemplateList(Integer formType) {
    List<String> templates = prepareCommonTemplateList(formType,MYBATIS_TEMPLATE_DIR);
    templates.add(HxStringUtils.format("vm/{}/java/DomainExample.java.vm",MYBATIS_TEMPLATE_DIR));
    templates.add(HxStringUtils.format("vm/{}/java/Repository.java.vm",MYBATIS_TEMPLATE_DIR));
    templates.add(HxStringUtils.format("vm/{}/java/SqlProvider.java.vm",MYBATIS_TEMPLATE_DIR));
    if (formType.equals(FormTypeEnum.SUB.getNumber())) {
      templates.add(HxStringUtils.format("vm/{}/java/SubDomainExample.java.vm",MYBATIS_TEMPLATE_DIR));
      templates.add(HxStringUtils.format("vm/{}/java/SubRepository.java.vm",MYBATIS_TEMPLATE_DIR));
      templates.add(HxStringUtils.format("vm/{}/java/SubSqlProvider.java.vm",MYBATIS_TEMPLATE_DIR));
    }
    return templates;
  }

  /**
   * 获取默认MybatisPlus模板
   * @param formType
   * @return
   */
  public static List<String> getMybatisPlusTemplateList(Integer formType) {
    List<String> templates = prepareCommonTemplateList(formType,MYBATIS_PLUS_TEMPLATE_DIR);
    templates.add(HxStringUtils.format("vm/{}/java/Mapper.java.vm",MYBATIS_PLUS_TEMPLATE_DIR));
    templates.add(HxStringUtils.format("vm/{}/java/Mapper.xml.vm",MYBATIS_PLUS_TEMPLATE_DIR));
    if (formType.equals(FormTypeEnum.SUB.getNumber())) {
      templates.add(HxStringUtils.format("vm/{}/java/SubMapper.java.vm",MYBATIS_PLUS_TEMPLATE_DIR));
      templates.add(HxStringUtils.format("vm/{}/java/SubMapper.xml.vm",MYBATIS_PLUS_TEMPLATE_DIR));
    }
    return templates;
  }

  public static void main(String[] args) {
    System.out.println("==================单表================================");
    getMybatisPlusTemplateList(1).stream().map(t->t+"\n").forEach(System.out::print);
    System.out.println("==================树表================================");
    getMybatisPlusTemplateList(2).stream().map(t->t+"\n").forEach(System.out::print);
    System.out.println("==================主子表================================");
    getMybatisPlusTemplateList(3).stream().map(t->t+"\n").forEach(System.out::print);
  }

  /**
   * 自定义模板过滤
   *
   * @param formType
   * @param templates
   * @return
   */
  public static List<String> getTemplateListByFormType(Integer formType, List<String> templates) {

    List<String> filterList = new ArrayList<>();
    if (formType.equals(FormTypeEnum.SINGLE.getNumber())) {
      filterList =
          templates.stream()
              .filter(
                  a ->
                      GenConstants.SINGLE_TABLE_TEMPLATE_LIST.stream()
                          .anyMatch(b -> a.endsWith(File.separator + b)))
              .collect(Collectors.toList());

    } else if (formType.equals(FormTypeEnum.SUB.getNumber())) {
      filterList =
          templates.stream()
              .filter(
                  a ->
                      GenConstants.MAIN_SUB_TABLE_TEMPLATE_LIST.stream()
                          .anyMatch(b -> a.endsWith(File.separator + b)))
              .collect(Collectors.toList());
    } else if (formType.equals(FormTypeEnum.TREE.getNumber())) {
      filterList =
          templates.stream()
              .filter(
                  a ->
                      GenConstants.TREE_TABLE_TEMPLATE_LIST.stream()
                          .anyMatch(b -> a.endsWith(File.separator + b)))
              .collect(Collectors.toList());
    }
    return filterList;
  }
  /** 获取文件名 */
  public static String getFileName(String template, CscpHxFormTable genTable) {
    // 文件名称
    String fileName = "";
    // 包路径
    String packageName = genTable.getPackageName();

    // 类名
    String className = genTable.getClassName();
    String subClassName = "";
    if (null != genTable.getSubTable()) {
      subClassName = genTable.getSubTable().getClassName();
    }
    String javaPath = PROJECT_PATH + "/" + HxStringUtils.replace(packageName, ".", "/");

    if (template.contains("Controller.java.vm")) {
      fileName = HxStringUtils.format("java/{}/web/{}Controller.java", javaPath, className);
    } else if (template.contains("Domain.java.vm")) {
      fileName = HxStringUtils.format("java/{}/domain/{}.java", javaPath, className);
    } else if (template.contains("DomainExample.java.vm")) {
      fileName = HxStringUtils.format("java/{}/domain/{}Example.java", javaPath, className);
    } else if (template.contains("Service.java.vm")) {
      fileName = HxStringUtils.format("java/{}/service/{}Service.java", javaPath, className);
    } else if (template.contains("ServiceImpl.java.vm")) {
      fileName =
          HxStringUtils.format("java/{}/service/impl/{}ServiceImpl.java", javaPath, className);
    } else if (template.contains("Repository.java.vm")) {
      fileName = HxStringUtils.format("java/{}/repository/{}Repository.java", javaPath, className);
    } else if (template.contains("SqlProvider.java.vm")) {
      fileName = HxStringUtils.format("java/{}/repository/{}SqlProvider.java", javaPath, className);
    } else if (template.contains("SingleForm.vue.vm")) {
      fileName =
          HxStringUtils.format(
              "vue/{}/{}Manager.vue",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.uncapitalize(className));
    } else if (template.contains("SingleForm.js.vm")) {
      fileName =
          HxStringUtils.format(
              "js/{}/{}-manager.js",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.lowerCase(className));
    } else if (template.contains("MainForm.vue.vm")) {
      fileName =
          HxStringUtils.format(
              "vue/{}/{}List.vue",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.uncapitalize(className));
    } else if (template.contains("MainForm.js.vm")) {
      fileName =
          HxStringUtils.format(
              "js/{}/{}-list.js",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.lowerCase(className));
    } else if (template.contains("SubForm.vue.vm")) {
      fileName =
          HxStringUtils.format(
              "vue/{}/{}Item.vue",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.uncapitalize(subClassName));
    } else if (template.contains("SubForm.js.vm")) {
      fileName =
          HxStringUtils.format(
              "js/{}/{}-item.js",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.lowerCase(subClassName));
    } else if (template.contains("TreeForm.vue.vm")) {
      fileName =
          HxStringUtils.format(
              "vue/{}/{}Manager.vue",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.uncapitalize(className));
    } else if (template.contains("TreeForm.js.vm")) {
      fileName =
          HxStringUtils.format(
              "js/{}/{}-manager.js",
              HxStringUtils.uncapitalize(className),
              HxStringUtils.lowerCase(className));
    }else if (template.contains("Mapper.java.vm")) {
      fileName = HxStringUtils.format("java/{}/mapper/{}Mapper.java", javaPath, className);
    } else if (template.contains("Mapper.xml.vm")) {
      fileName = HxStringUtils.format("resource/mapper/{}Mapper.xml", className);
    }
    if (template.contains("SubController.java.vm")) {
      fileName = HxStringUtils.format("java/{}/web/{}Controller.java", javaPath, subClassName);
    } else if (template.contains("SubDomain.java.vm")) {
      fileName = HxStringUtils.format("java/{}/domain/{}.java", javaPath, subClassName);
    } else if (template.contains("SubDomainExample.java.vm")) {
      fileName = HxStringUtils.format("java/{}/domain/{}Example.java", javaPath, subClassName);
    } else if (template.contains("SubService.java.vm")) {
      fileName = HxStringUtils.format("java/{}/service/{}Service.java", javaPath, subClassName);
    } else if (template.contains("SubServiceImpl.java.vm")) {
      fileName =
          HxStringUtils.format("java/{}/service/impl/{}ServiceImpl.java", javaPath, subClassName);
    } else if (template.contains("SubRepository.java.vm")) {
      fileName =
          HxStringUtils.format("java/{}/repository/{}Repository.java", javaPath, subClassName);
    } else if (template.contains("SubSqlProvider.java.vm")) {
      fileName =
          HxStringUtils.format("java/{}/repository/{}SqlProvider.java", javaPath, subClassName);
    }  else if (template.contains("SubMapper.java.vm")) {
      fileName = HxStringUtils.format("java/{}/mapper/{}Mapper.java", javaPath, subClassName);
    } else if (template.contains("SubMapper.xml.vm")) {
      fileName = HxStringUtils.format("resource/mapper/{}Mapper.xml", subClassName);
    }

    return fileName;
  }

  /**
   * 根据列类型获取导入包
   *
   * @param genTable 业务表对象
   * @return 返回需要导入的包列表
   */
  public static HashSet<String> getImportList(CscpHxFormTable genTable) {
    List<CscpHxFormColumn> columns = genTable.getColumns();
    CscpHxFormTable subGenTable = genTable.getSubTable();
    HashSet<String> importList = new HashSet<String>();
    if (HxStringUtils.isNotNull(subGenTable)) {
      importList.add("java.util.List");
    }
    for (CscpHxFormColumn column : columns) {
      if (GenConstants.TYPE_DATE.equals(column.getJavaType())) {
        importList.add("java.util.Date");
        importList.add("com.fasterxml.jackson.annotation.JsonFormat");
      } else if (GenConstants.TYPE_ZONED_DATE_TIME.equals(column.getJavaType())) {
        importList.add("java.time.ZonedDateTime");
        importList.add("com.fasterxml.jackson.annotation.JsonFormat");
      } else if (GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
        importList.add("java.math.BigDecimal");
      }
    }
    return importList;
  }

  /**
   * 获取树编码
   *
   * @param paramsObj 生成其他选项
   * @return 树编码
   */
  public static String getTreecode(JSONObject paramsObj) {
    if (paramsObj.containsKey(GenConstants.TREE_CODE)) {
      return HxStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_CODE));
    }
    return HxStringUtils.EMPTY;
  }

  /**
   * 获取树父编码
   *
   * @param paramsObj 生成其他选项
   * @return 树父编码
   */
  public static String getTreeParentCode(JSONObject paramsObj) {
    if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
      return HxStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_PARENT_CODE));
    }
    return HxStringUtils.EMPTY;
  }

  /**
   * 获取树名称
   *
   * @param paramsObj 生成其他选项
   * @return 树名称
   */
  public static String getTreeName(JSONObject paramsObj) {
    if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
      return HxStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_NAME));
    }
    return HxStringUtils.EMPTY;
  }
}
