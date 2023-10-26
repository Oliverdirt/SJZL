package com.ctsi.ssdc.sensitiveword.web;

import cn.hutool.core.text.StrFormatter;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.sensitiveword.constants.SensitiveWordConstant;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordExample;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordType;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordService;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordTypeService;
import com.ctsi.ssdc.sensitiveword.util.PathUtil;
import com.ctsi.ssdc.sensitiveword.util.SensitiveWordUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * REST controller for managing CscpSensitiveWord.
 *
 * @author ctsi-biyi-generator
 */
@Api(tags = "敏感词管理", value = "敏感词")
@RestController
@RequestMapping("/api/system")
public class CscpSensitiveWordController {

    private final Logger log = LoggerFactory.getLogger(CscpSensitiveWordController.class);
    private static final String ENTITY_NAME = "cscpSensitiveWord";
    private final CscpSensitiveWordService cscpSensitiveWordService;

    private final CscpSensitiveWordTypeService cscpSensitiveWordTypeService;

    public CscpSensitiveWordController(CscpSensitiveWordService cscpSensitiveWordService, CscpSensitiveWordTypeService cscpSensitiveWordTypeService) {
        this.cscpSensitiveWordService = cscpSensitiveWordService;
        this.cscpSensitiveWordTypeService = cscpSensitiveWordTypeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpSensitiveWords : Create a new cscpSensitiveWord.
     *
     * @param cscpSensitiveWord the cscpSensitiveWord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpSensitiveWord, or with status 400 (Bad Request) if the cscpSensitiveWord has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "敏感词-新增", notes = "新增")
    @PostMapping("/cscpSensitiveWords")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "createCscpSensitiveWord")
    public AjaxResult createCscpSensitiveWord(@RequestBody @Valid CscpSensitiveWord cscpSensitiveWord) throws URISyntaxException {
        log.debug("REST request to save CscpSensitiveWord : {}", cscpSensitiveWord);

        if (cscpSensitiveWord.getId() != null) {
            throw new BadRequestAlertException("A new cscpSensitiveWord cannot already have an id", ENTITY_NAME, "idexists");
        }
        // 判断类型是否存在
        CscpSensitiveWordType wordTypeServiceOne = cscpSensitiveWordTypeService.findOne(cscpSensitiveWord.getSenTypeId());
        if(null == wordTypeServiceOne){
            // 类型不存在
            return AjaxResult.error(StrFormatter.format("新增失败,类型不存在，请先创建类型"));
        }
        // 唯一性判断
        CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
        cscpSensitiveWordExample.createCriteria()
                .andSenContentEqualTo(cscpSensitiveWord.getSenContent());
        PageResult<CscpSensitiveWord> wordServiceBySenContent = cscpSensitiveWordService.findByExample(cscpSensitiveWordExample);

        if(wordServiceBySenContent.getData().size()>0){
            // 已存在
            return AjaxResult.error(StrFormatter.format("新增失败,敏感词{}已存在",cscpSensitiveWord.getSenContent()));
        }else{
            // 不存在
            cscpSensitiveWord.setUpdateTime(new Date());
            CscpSensitiveWord result = cscpSensitiveWordService.insert(cscpSensitiveWord);
            SensitiveWordUtil.add(cscpSensitiveWord);
            return AjaxResult.success(StrFormatter.format("敏感词{}新增成功",cscpSensitiveWord.getSenContent()));
        }
    }

    /**
     * PUT  /cscpSensitiveWords : Updates an existing cscpSensitiveWord.
     *
     * @param cscpSensitiveWord the cscpSensitiveWord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpSensitiveWord,
     * or with status 400 (Bad Request) if the cscpSensitiveWord is not valid,
     * or with status 500 (Internal Server Error) if the cscpSensitiveWord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "敏感词-修改", notes = "修改")
    @PutMapping("/cscpSensitiveWords")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "updateCscpSensitiveWord")
    public AjaxResult updateCscpSensitiveWord(@RequestBody @Valid CscpSensitiveWord cscpSensitiveWord) throws URISyntaxException {
        log.debug("REST request to update CscpSensitiveWord : {}", cscpSensitiveWord);
        if (cscpSensitiveWord.getId() == null) {
            return createCscpSensitiveWord(cscpSensitiveWord);
        }
        // 判断类型是否存在
        CscpSensitiveWordType wordTypeServiceOne = cscpSensitiveWordTypeService.findOne(cscpSensitiveWord.getSenTypeId());
        if(null == wordTypeServiceOne){
            // 类型不存在
            return AjaxResult.error(StrFormatter.format("修改失败,类型不存在，请先创建类型"));
        }
        // 唯一性判断
        CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
        cscpSensitiveWordExample.createCriteria().andSenContentEqualTo(cscpSensitiveWord.getSenContent());
        List<CscpSensitiveWord> sensitiveWordList = cscpSensitiveWordService.findByExample(cscpSensitiveWordExample).getData();
        if(sensitiveWordList.size()>0 && !sensitiveWordList.get(0).getId().equals(cscpSensitiveWord.getId())){
            // 已存在
            return AjaxResult.error(StrFormatter.format("敏感词{}修改失败，数据已存在",cscpSensitiveWord.getSenContent()));
        }else{
            // 不存在则添加
            cscpSensitiveWord.setUpdateTime(new Date());
            CscpSensitiveWord result = cscpSensitiveWordService.update(cscpSensitiveWord);
            SensitiveWordUtil.update(cscpSensitiveWord);
            return AjaxResult.success(StrFormatter.format("敏感词{}修改成功",cscpSensitiveWord.getSenContent()));
        }
    }

    /**
     * GET  /cscpSensitiveWords : get the cscpSensitiveWords with page.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpSensitiveWords in body
     */
    @ApiOperation(value = "敏感词-分页查询", notes = "分页查询")
    @GetMapping("/cscpSensitiveWordsByCriteria")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getCscpSensitiveWordsByCriteria")
    public PageResult<CscpSensitiveWord> getCscpSensitiveWordsByCriteria(CscpSensitiveWordExample cscpSensitiveWordExample, Pageable page) {
        log.debug("REST request to get CscpSensitiveWordsByCriteria");
        return cscpSensitiveWordService.findByExample(cscpSensitiveWordExample, page);
    }

    /**
     * GET  /cscpSensitiveWords/:id : get the "id" cscpSensitiveWord.
     *
     * @param id the id of the cscpSensitiveWord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpSensitiveWord, or with status 404 (Not Found)
     */
    @ApiOperation(value = "敏感词-单体查询", notes = "单体查询")
    @GetMapping("/cscpSensitiveWords/{id}")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getCscpSensitiveWord")
    public ResponseEntity<CscpSensitiveWord> getCscpSensitiveWord(@PathVariable Long id) {
        log.debug("REST request to get CscpSensitiveWord : {}", id);
        CscpSensitiveWord cscpSensitiveWord = cscpSensitiveWordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpSensitiveWord));
    }

    /**
     * DELETE  /cscpSensitiveWords/:id : delete the "id" cscpSensitiveWord.
     *
     * @param id the id of the cscpSensitiveWord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "敏感词-删除", notes = "删除")
    @DeleteMapping("/cscpSensitiveWords/{id}")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "deleteCscpSensitiveWord")
    public ResponseEntity<Void> deleteCscpSensitiveWord(@PathVariable  Long id) {
        log.debug("REST request to delete CscpSensitiveWord : {}", id);
        CscpSensitiveWord cscpSensitiveWord = cscpSensitiveWordService.findOne(id);
        cscpSensitiveWordService.delete(id);
        SensitiveWordUtil.delete(cscpSensitiveWord);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation(value = "敏感词-批量删除", notes = "批量删除")
    @DeleteMapping("/cscpSensitiveWords/delAll")
    public AjaxResult deleteCscpSensitiveWordType(Long[] ids) {
        log.debug("REST request to delete ids");

        return cscpSensitiveWordService.deleteByIds(ids);
    }


    @ApiOperation(value = "敏感词-匹配", notes = "匹配")
    @PostMapping("/matchCscpSensitiveWord/")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "matchCscpSensitiveWord")
    public ResponseEntity<Map<String, String>> matchCscpSensitiveWord(@RequestBody String content) {
        Map<String, String> map = SensitiveWordUtil.matchSensitiveWord(content);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(map));
    }

    @ApiOperation(value = "敏感词-上传模板下载", notes = "上传模板下载")
    @GetMapping("/downLoadSensitiveWordTemplate")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "downLoadTemplate")
    public void downLoadTemplate(HttpServletResponse response) {
        String fileName = "SensitiveWord_template.xlsx";
        Resource resource = new ClassPathResource("static/" + fileName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
        ) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            byte[] bs = new byte[1024 * 10];
            int l = -1;
            while ((l = inputStream.read(bs)) != -1) {
                bos.write(bs, 0, l);
            }
            bos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @ApiOperation(value = "敏感词-模板数据上传", notes = "模板数据上传")
    @PostMapping("/uploadSensitiveWordData")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "uploadSensitiveWordData")
    public ResponseEntity<String> uploadSensitiveWordData(@RequestParam("file") MultipartFile multipartFile) {
        // 文件校验,是否包含特殊字符
        String fileName = multipartFile.getOriginalFilename();
        boolean checkContainsSpecialCharacters = PathUtil.checkContainsSpecialCharacters(fileName);
        if(checkContainsSpecialCharacters){
            log.error("路径中包含特殊字符，文件上传失败");
            return ResponseEntity.badRequest().body("路径中包含特殊字符，文件上传失败");
        }
        if(!SensitiveWordConstant.EXCELFILEPATTERN.matcher(fileName).find()){
            log.error("文件格式不正确，仅支持.xlsx , .xls文件类型");
            return ResponseEntity.badRequest().body("文件格式不正确，仅支持.xlsx , .xls文件类型");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            this.cscpSensitiveWordService.saveUploadFile(inputStream);
            return ResponseEntity.ok("文件上传成功");
        } catch (IOException e) {
            log.error("录入信息异常 ");
            throw new RuntimeException(e);
        }
    }

}
