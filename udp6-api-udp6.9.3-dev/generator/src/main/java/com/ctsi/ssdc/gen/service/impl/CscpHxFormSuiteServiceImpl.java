package com.ctsi.ssdc.gen.service.impl;


import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuite;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuiteExample;
import com.ctsi.ssdc.gen.exception.BusinessException;
import com.ctsi.ssdc.gen.repository.CscpHxFormSuiteRepository;
import com.ctsi.ssdc.gen.service.CscpHxFormSuiteService;
import com.ctsi.ssdc.gen.util.FileUtil;
import com.ctsi.ssdc.poi.excel.util.PathUtil;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Status;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service Implementation for managing CodeTemplateSuite.
 *
 * @author ctsi-biyi-generator
 */
@Service
public class CscpHxFormSuiteServiceImpl
        extends StrengthenBaseServiceImpl<CscpHxFormSuiteRepository, CscpHxFormSuite, Long, CscpHxFormSuiteExample>
        implements CscpHxFormSuiteService {
    private final Logger log = LoggerFactory.getLogger(CscpHxFormSuiteServiceImpl.class);

    private static final String TEMPLATEDIR = "/template/";

    @Value("${ctsi.upload.uploadPath}")
    private String uploadDir;

    @Override
    public CscpHxFormSuite insert(CscpHxFormSuite codeTemplateSuite, MultipartFile multipartFile) {
        //查询对应的模板名称及版本是否已存在
        CscpHxFormSuiteExample codeTemplateSuiteExample = new CscpHxFormSuiteExample();
        codeTemplateSuiteExample.setSuiteCode((StringCriteria) new StringCriteria().setEquals(codeTemplateSuite.getSuiteCode()));
        codeTemplateSuiteExample.setSuiteVersion((StringCriteria) new StringCriteria().setEquals(codeTemplateSuite.getSuiteVersion()));
        codeTemplateSuiteExample.buildCriteria();
        List<CscpHxFormSuite> query = super.r.selectByExample(codeTemplateSuiteExample);
        if (0 != query.size()) {
            log.error("模板套件版本已存在");
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, "模板版本已存在", "模板版本已存在", null);
        }
        saveSuiteFile(codeTemplateSuite, multipartFile);
        return super.insert(codeTemplateSuite);
    }

    @Override
    public CscpHxFormSuite update(CscpHxFormSuite codeTemplateSuite, MultipartFile multipartFile) {
        if (multipartFile != null) {
            //删除模板文件
            FileUtil.deleteDir(codeTemplateSuite.getSuitePath());
            //保存新文件
            saveSuiteFile(codeTemplateSuite, multipartFile);
        }
        return super.update(codeTemplateSuite);
    }

    @Override
    public void delete(Long p) {
        //删除模板文件
        CscpHxFormSuite codeTemplateSuite = super.findOne(p);
        FileUtil.deleteDir(codeTemplateSuite.getSuitePath());
        super.delete(p);
    }

    /**
     * 保存模板文件
     *
     * @param codeTemplateSuite
     * @param multipartFile
     */
    private void saveSuiteFile(CscpHxFormSuite codeTemplateSuite, MultipartFile multipartFile) {
        //文件类型判断
        String originalFileName = multipartFile.getOriginalFilename();
        if (!originalFileName.matches(".*.zip")) {
            log.error("模板文件类型错误!");
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, "模板文件类型错误！", "模板文件类型错误，请上传zip压缩文件", null);
        }
        String fileName = codeTemplateSuite.getSuiteCode() + "-" + codeTemplateSuite.getSuiteVersion();
        File projectPath = null;
        try {
            if (null != uploadDir) {
                projectPath = new File(PathUtil.filePathFilter(uploadDir));
            } else {
                projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
            }
        } catch (FileNotFoundException e) {
            log.error("获取根目录异常" + e);
            throw new BusinessException("获取根目录异常！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "获取根目录异常！", null);
        }
        if (!projectPath.exists()) {
            boolean mkdirs = projectPath.mkdirs();
            if (!mkdirs) {
                throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
            }
        }
        String suitePath = projectPath + TEMPLATEDIR + fileName;
        File suiteDir = new File(PathUtil.filePathFilter(suitePath));
        if (!suiteDir.exists()) {
            boolean mkdirs = suiteDir.mkdirs();
            if (!mkdirs) {
                throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
            }
        }
        //保存zip包
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(PathUtil.filePathFilter(suitePath), PathUtil.filePathFilter(fileName + ".zip")));
        } catch (IOException e) {
            log.error("保存zip模板文件异常" + e);
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "保存zip模板文件异常！", null);
        }

        try (FileInputStream fin = new FileInputStream(PathUtil.filePathFilter(suitePath + File.separator + fileName + ".zip"));
             BufferedInputStream bis = new BufferedInputStream(fin);
             ZipInputStream zis = new ZipInputStream(bis);
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                write(zis, entry, suitePath);
            }
        } catch (IOException e) {
            log.error("模板文件保存出错", e);
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "模板文件保存出错", null);
        }
        codeTemplateSuite.setSuitePath(suitePath);
    }

    /**
     * zip包文件写入指定目录
     *
     * @param zis
     * @param entry
     * @param suitePath
     */
    private void write(ZipInputStream zis, ZipEntry entry, String suitePath) {
        String entryName = entry.getName();
        try (FileOutputStream fout = new FileOutputStream(PathUtil.filePathFilter(suitePath + File.separator + entryName));
             BufferedOutputStream bos = new BufferedOutputStream(fout)) {
            int b = 0;
            while ((b = zis.read()) != -1) {
                bos.write(b);
            }
            bos.flush();
        } catch (IOException e) {
            log.error("模板文件保存出错", e);
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "模板文件保存出错", null);
        }
    }
}
