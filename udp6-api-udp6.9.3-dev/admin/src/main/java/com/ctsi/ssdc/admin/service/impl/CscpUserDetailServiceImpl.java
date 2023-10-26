package com.ctsi.ssdc.admin.service.impl;

import com.ctsi.ssdc.admin.constants.AdminFlagConstant;
import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.CscpUserDetailExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.*;
import com.ctsi.ssdc.admin.repository.*;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.admin.service.CscpUserPostService;
import com.ctsi.ssdc.admin.service.CscpUserRoleService;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserDetailMapper;
import com.ctsi.ssdc.cache.LoginCache;
import com.ctsi.ssdc.model.*;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.RSAUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpUserDetail.
 *
 * @author ctsi biyi generator
 */
@Service
public class CscpUserDetailServiceImpl implements CscpUserDetailService {

    private final Logger log = LoggerFactory.getLogger(CscpUserDetailServiceImpl.class);

    private final Long WORK_WEIXIN_LOGIN_TYPE_CODE = 1L;

    @Value("${ctsi.login-password:ctsi1234}")
    private String rawPassword;

    @Value("${ctsi.RSA-prikey:}")
    private String rsaPrikey = "";

    private final Long  INSERT_SUCCESS_NUM = 1L;

    @Resource
    private CscpUserDetailRepository cscpUserDetailRepository;

    private final CscpUserDetailMapper cscpUserDetailMapper;

    @Resource
    private CscpUserRepository cscpUserRepository;

    @Resource
    private CscpRolesRepository cscpRolesRepository;

    @Resource
    private CscpUserRoleRepository cscpUserRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CscpUserRoleService cscpUserRoleService;

    @Autowired
    private CscpUserService cscpUserService;

    @Value("${ctsi.password-check.expire-months:3}")
    private int expireMonth;


    @Autowired
    private ContainDigitCheck containDigitCheck;

    @Autowired
    private KeyBoardCheckNew keyBoardCheckNew;

    @Autowired
    private LengthCheck lengthCheck;

    @Autowired
    private LowerCaseCheck lowerCaseCheck;

    @Autowired
    private UpperCaseCheck upperCaseCheck;

    @Autowired
    private SpecialCharCheck specialCharCheck;

    @Autowired
    private ContainUserNameCheck containUserNameCheck;

    @Autowired
    private ContainTenantAccountCheck containTenantAccountCheck;

    @Resource
    private CscpUserPasswordChangeLogRepository cscpUserPasswordChangeLogRepository;

    @Resource
    private CscpUserOrgRepository cscpUserOrgRepository;

    @Resource
    private CscpUserWorkGroupRepository cscpUserWorkGroupRepository;

    @Resource
    private CscpUserPostRepository cscpUserPostRepository;

    @Resource
    private CscpUserPostService cscpUserPostService;

    @Resource(name = "${ctsi.login.cache:guavaLoginCache}")
    LoginCache loginCache;

    @Value("${ctsi.login.bad-password-attempts:5}")
    private int badPasswordAttempts;

    public CscpUserDetailServiceImpl(CscpUserDetailMapper cscpUserDetailMapper) {
        this.cscpUserDetailMapper = cscpUserDetailMapper;
    }


    /**
     * insert a cscpUserDetail.
     *
     * @param cscpUserDetailDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    @Transactional
    public CscpUserDetailDTO insert(CscpUserDetailDTO cscpUserDetailDTO) {
        log.debug("Request to insert CscpUserDetail : {}", cscpUserDetailDTO);
        CscpUser cscpUser = cscpUserDetailMapper.toCscpUser(cscpUserDetailDTO);
        cscpUser.setPassword(passwordEncoder.encode(rawPassword));
        Long userId = cscpUserRepository.insert(cscpUser);
        cscpUserDetailDTO.setUserId(cscpUser.getId());
        log.info("@@@@@@@@@@@@ insert user id is :" + (userId.intValue() == INSERT_SUCCESS_NUM.intValue() ? true:false));
        if (StringUtils.isBlank(cscpUserDetailDTO.getAdminFlag())){
            cscpUserDetailDTO.setAdminFlag(AdminFlagConstant.NOMAL_USER);
        }
        Long tenantId = null;
        Long dtoTenantId = cscpUserDetailDTO.getTenantId();
        if ((dtoTenantId == null)) {
            // 获取当前租户id
            Long currentUserId = SecurityHxUtils.getCurrentUserId();
            tenantId = cscpUserDetailRepository.selectByUserId(currentUserId).getTenantId();
        } else {
            tenantId = dtoTenantId;
        }

        cscpUserDetailDTO.setTenantId(tenantId);

        CscpUserDetail cscpUserDetail = cscpUserDetailMapper.toEntity(cscpUserDetailDTO);
        cscpUserDetail.setRegisterTime(ZonedDateTime.now());
        cscpUserDetailRepository.insert(cscpUserDetail);
        // 保存用户角色
        cscpUserRoleService.saveUserRoles(cscpUserDetail.getUserId(), cscpUserDetailDTO.getRoles());

        cscpUserPostService.saveUserPost(cscpUserDetail.getUserId(),cscpUserDetailDTO.getPosts());
        //保存工作组和机构
//        saveUserWkOrg(cscpUserDetailDTO);
        return cscpUserDetailMapper.toDto(cscpUserDetail);
    }


    /**
     * update a cscpUserDetail.
     *
     * @param cscpUserDetailDTO the entity to update
     * @return the persisted entity
     */
    @Override
    @Transactional
    public CscpUserDetailDTO update(CscpUserDetailDTO cscpUserDetailDTO) {
        log.debug("Request to update CscpUserDetail : {}", cscpUserDetailDTO);
        CscpUserDetail cscpUserDetail = cscpUserDetailMapper.toEntity(cscpUserDetailDTO);

        cscpUserDetailRepository.updateByPrimaryKeySelective(cscpUserDetail);
        // 保存用户角色
        cscpUserRoleService.saveUserRoles(cscpUserDetailDTO.getUserId(), cscpUserDetailDTO.getRoles());

        cscpUserPostService.saveUserPost(cscpUserDetail.getUserId(),cscpUserDetailDTO.getPosts());
        return cscpUserDetailMapper.toDto(cscpUserDetail);
    }

    //增加当前管理员修改用户密码
    @Override
    public CscpUserDetailDTO userPwdUpdate(CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception {
        CscpUserDetailDTO userDetailDTO = cscpUserUpdateDTO.getCscpUserDetailDTO();
        log.debug("Request to update CscpUserPwd : {}", userDetailDTO);
        String curPwd = decryptPassword(cscpUserUpdateDTO.getCurrentUserPwd());
        if (!curPwd.isEmpty()) {
            //获取当前登录人员id
            Long uid = SecurityHxUtils.getCurrentUserId();
            //通过userId 获取当前用户用户名和密码
            CscpUserDTO currentUser = cscpUserService.findOne(uid);
            //输入管理员密码与实际密码不匹配
            if (!passwordEncoder.matches(curPwd, currentUser.getPassword())) {
                throw new Exception("管理员密码不正确!");
            }
        }

        String newPwd = decryptPassword(cscpUserUpdateDTO.getNewPassword());
        if (!newPwd.isEmpty()) {
            //获取待修改用户的id
            Long userId = userDetailDTO.getUserId();
            CscpUser cscpUser = cscpUserRepository.selectByPrimaryKey(userId);

            CheckResult checkResult = new CheckResult();

            lengthCheck.check(newPwd, checkResult);
            lowerCaseCheck.check(newPwd, checkResult);
            upperCaseCheck.check(newPwd, checkResult);
            containDigitCheck.check(newPwd, checkResult);
            specialCharCheck.check(newPwd, checkResult);
            containUserNameCheck.setUserName(cscpUser.getUsername());
            containUserNameCheck.check(newPwd, checkResult);
            String tenantAccount = SecurityHxUtils.getCurrentTenantAccount();

            if(userDetailDTO.getTenantAccount() != null){
                containTenantAccountCheck.setTenantAccount(userDetailDTO.getTenantAccount());
                containTenantAccountCheck.check(newPwd, checkResult);
            }else {
                containTenantAccountCheck.setTenantAccount(tenantAccount);
                containTenantAccountCheck.check(newPwd, checkResult);
            }
            keyBoardCheckNew.check(newPwd, checkResult);
            StringBuffer stringBuffer = new StringBuffer();
            if (checkResult.getErrorReason().size() > 0) {
                for (int i = 0; i < checkResult.getErrorReason().size(); i++) {
                    if (stringBuffer.length() > 0) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(checkResult.getErrorReason().get(i));
                }
            }
            if (checkResult.getConfirmCount() < 3) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append("密码还应包含");
                for (int i = 0; i < checkResult.getConfirmInfo().size(); i++) {
                    stringBuffer.append(checkResult.getConfirmInfo().get(i));
                    if (i < checkResult.getConfirmInfo().size() - 1) {
                        stringBuffer.append("、");
                    }
                }
                stringBuffer.append("至少");
                stringBuffer.append(3 - checkResult.getConfirmCount());
                stringBuffer.append("项");

            }

            CscpUserPasswordChangeLogExample cscpUserPasswordChangeLogExample = new CscpUserPasswordChangeLogExample();
            cscpUserPasswordChangeLogExample.createCriteria().andUserIdEqualTo(cscpUser.getId());
            cscpUserPasswordChangeLogExample.setOrderByClause("time desc");

            List<CscpUserPasswordChangeLog> changeLogs = cscpUserPasswordChangeLogRepository.
                    selectByExample(cscpUserPasswordChangeLogExample);
            if (CollectionUtils.isNotEmpty(changeLogs)) {
                for (int i = 0; i < changeLogs.size(); i++) {
                    if (passwordEncoder.matches(newPwd, changeLogs.get(i).getPassword())) {
//                    throw new Exception("密码使用过");
                        stringBuffer.append(" 密码已使用过");
                        break;
                    }
                }
            }

            if (stringBuffer.length() > 0) {
                throw new Exception(stringBuffer.toString());
            }

            CscpUserPasswordUpdate cscpUserPasswordUpdate = new CscpUserPasswordUpdate();
            cscpUserPasswordUpdate.setUserId(cscpUser.getId());
            cscpUserPasswordUpdate.setUserName(cscpUser.getUsername());
            cscpUserPasswordUpdate.setNewPassword(newPwd);
            Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
            cscpUserPasswordUpdate.setTenantId(currentTenantId);
            cscpUserService.updatePassword(cscpUserPasswordUpdate, false);
        }
        return cscpUserUpdateDTO.getCscpUserDetailDTO();
    }

    /**
     * Get all the cscpUserDetails.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserDetailDTO> findAll() {
        log.debug("Request to get all CscpUserDetails");

        List<CscpUserDetailDTO> data = cscpUserDetailRepository.selectByExample(null).stream()
                .map(cscpUserDetailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserDetailRepository.countByExample(null);
        }

        return new PageResult<CscpUserDetailDTO>(data, count, count);

    }

    /**
     * Get one cscpUserDetail.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpUserDetailDTO findOne(Long id) {
        log.debug("Request to get CscpUserDetail : {} ", id);

        CscpUserDetail cscpUserDetail = cscpUserDetailRepository.selectByPrimaryKey(id);
        return cscpUserDetailMapper.toDto(cscpUserDetail);
    }

    /**
     * Delete the cscpUserDetail .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpUserDetail : {} ", id);

        cscpUserDetailRepository.deleteByPrimaryKey(id);
    }

    private String getPageOrderBy(Pageable page) {

        if (page != null && page.getSort() != null) {

            StringBuilder sb = new StringBuilder();

            page.getSort()
                    .forEach(sort -> sb.append(sort.getProperty()).append(" ").append(sort.getDirection()).append(","));

            if (sb.length() > 1) {
                return (sb.substring(0, sb.length() - 1));
            }
        }

        return null;
    }

    /**
     * Get the cscpUserDetails.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserDetailDTO> findByCscpUserDetailDTO(CscpUserDetailDTO cscpUserDetailDTO, Pageable page) {

        log.debug("Request to find CscpUserDetails");

        CscpUserDetailExample example = new CscpUserDetailExample();

        example.setPage(page);

        Criteria critieria = example.createCriteria();

        if (cscpUserDetailDTO.getId() != null) {
            critieria.andIdEqualTo(cscpUserDetailDTO.getId());
        }
        if (cscpUserDetailDTO.getUserId() != null) {
            critieria.andUserIdEqualTo(cscpUserDetailDTO.getUserId());
        }

        if (StringUtils.isNotBlank(cscpUserDetailDTO.getFamilyName())) {
            critieria.andFamilyNameLike(String.format("%%%s%%", cscpUserDetailDTO.getFamilyName()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getName())) {
            critieria.andNameLike(String.format("%%%s%%", cscpUserDetailDTO.getName()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getMobile())) {
            critieria.andMobileLike(String.format("%%%s%%", cscpUserDetailDTO.getMobile()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getEmail())) {
            critieria.andEmailLike(String.format("%%%s%%", cscpUserDetailDTO.getEmail()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getImgPath())) {
            critieria.andImgPathLike(String.format("%%%s%%", cscpUserDetailDTO.getImgPath()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getDiscTitle())) {
            critieria.andDiscTitleLike(String.format("%%%s%%", cscpUserDetailDTO.getDiscTitle()));
        }
        if (StringUtils.isNotBlank(cscpUserDetailDTO.getDiscDetail())) {
            critieria.andDiscDetailLike(String.format("%%%s%%", cscpUserDetailDTO.getDiscDetail()));
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpUserDetailDTO> data = cscpUserDetailMapper
                .toDto(cscpUserDetailRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserDetailRepository.countByExample(example);
        }

        return new PageResult<CscpUserDetailDTO>(data, count, count);

    }

    /**
     * Get the cscpUserDetails.
     *
     * @param cscpUserDetailCriteria
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PageResult<CscpUserDetailDTO> findByCscpUserDetailCriteria(
            CscpUserDetailCriteria cscpUserDetailCriteria, Pageable page) {

        CscpUserDetailExample example = new CscpUserDetailExample();

        example.setPage(page);

        Criteria criteria = example.createCriteria();

        if (cscpUserDetailCriteria != null) {
            cscpUserDetailCriteria.buildCriteria(criteria);
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpUserDetailDTO> data = cscpUserDetailMapper
                .toDto(cscpUserDetailRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserDetailRepository.countByExample(example);
        }

        return new PageResult<CscpUserDetailDTO>(data, count, count);

    }

    @Override
    public List<CscpUserDetailDTO> selectByWorkGroupId(Long id) {
        // TODO Auto-generated method stub
        List<CscpUserDetail> list = cscpUserDetailRepository.selectByWorkGroupId(id);
        List<CscpUserDetailDTO> data = cscpUserDetailMapper.toDto(list);
        return data;
    }

    @Override
    public CscpUserDetailDTO selectByUserId(Long userId) {
        // TODO Auto-generated method stub
        log.debug("Request to get CscpUserDetail by userId : {} ", userId);

        CscpUserDetail cscpUserDetail = cscpUserDetailRepository.selectByUserId(userId);
        return cscpUserDetailMapper.toDto(cscpUserDetail);
    }

    @Override
    public PageResult<CscpUserDetailDTO> findByCscpUserDetailDtoOr(
            CscpUserDetailLike cscpUserDetailLike, Pageable page) {

        log.debug("Request to find CscpUserDetail");

        String orderBy = getPageOrderBy(page);
        Long userId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetail cscpUserDetail = cscpUserDetailRepository.selectByUserId(userId);
        Long tenantId = cscpUserDetail.getTenantId();
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpUserDetailDTO> list = cscpUserDetailRepository.
                queryUserDetailPreventSqlAttack(cscpUserDetailLike.getUsernameLike()
                        , cscpUserDetailLike.getFamilyNameLike(),
                        cscpUserDetailLike.getNameLike(), cscpUserDetailLike.getMobileLike(), cscpUserDetailLike.getEmailLike(),
                        cscpUserDetailLike.getDiscTitleLike(), cscpUserDetailLike.getDiscDetailLike(),cscpUserDetailLike.getDeptIdEquals(),
                        orderBy,tenantId);
        PageInfo<CscpUserDetailDTO> info = new PageInfo<>(list);
        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                //用户的角色信息
                List<CscpRoles> roleList = cscpUserRoleRepository.queryRoleByUserId(list.get(index).getUserId());
                if (roleList != null && roleList.size() > 0) {
                    StringBuilder roleId = new StringBuilder(roleList.get(0).getId().toString());
                    StringBuilder roleName = new StringBuilder(roleList.get(0).getName());
                    for (int i = 1; i < roleList.size(); i++) {
                        //","方式分割：如1，2  ；admin，admin2
                        roleId.append("," + roleList.get(i).getId());
                        roleName.append("," + roleList.get(i).getName());
                    }
                    list.get(index).setRoleIds(roleId.toString());
                    list.get(index).setRoleNames(roleName.toString());
                }
                List<CscpPost> postList = cscpUserPostRepository.queryPostByUserId(list.get(index).getUserId());
                if(postList != null && postList.size() > 0) {
                    StringBuilder postId = new StringBuilder(postList.get(0).getPostId().toString());
                    StringBuilder postName = new StringBuilder(postList.get(0).getPostName());
                    for (int i = 1; i < postList.size(); i++) {
                        postId.append("," + postList.get(i).getPostId());
                        postName.append("," + postList.get(i).getPostName());
                    }
                    list.get(index).setPostIds(postId.toString());
                    list.get(index).setPostNames(postName.toString());
                }

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("loginFailed:");
                stringBuffer.append(list.get(index).getUsername());
                String countKey = stringBuffer.toString();
                list.get(index).setLockStatus(false);
                if (loginCache != null) {
                    Integer countKeyValue = (Integer) loginCache.get(countKey);
                    if (countKeyValue != null && countKeyValue == badPasswordAttempts) {
                        long lockTime = loginCache.getExpire(countKey);
                        if (lockTime > 0){
                            list.get(index).setLockStatus(true);
                        }else{
                            list.get(index).setLockStatus(false);
                        }

                    }
                }
            }
        }

        return new PageResult<CscpUserDetailDTO>(list, info.getTotal(), info.getTotal());

    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserDetailDTO> findByUserId(String userId, Pageable page) {

        String orderBy = getPageOrderBy(page);

        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        //用户信息
        List<CscpUserDetailDTO> list = cscpUserDetailRepository.queryUserDetailByUserId(userId, orderBy);

        PageInfo<CscpUserDetailDTO> info = new PageInfo<>(list);

        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                //用户的角色信息
                List<CscpRoles> roleList = cscpUserRoleRepository.queryRoleByUserId(list.get(index).getUserId());
                if (roleList != null && roleList.size() > 0) {
                    StringBuilder idsb = new StringBuilder(roleList.get(0).getId().toString());
                    StringBuilder namesb = new StringBuilder(roleList.get(0).getName());
                    for (int i = 1; i < roleList.size(); i++) {
                        idsb.append("," + roleList.get(i).getId());//1,2
                        namesb.append("," + roleList.get(i).getName());//admin,system
                    }
                    list.get(index).setRoleIds(idsb.toString());
                    list.get(index).setRoleNames(namesb.toString());
                }
                List<CscpPost> postList = cscpUserPostRepository.queryPostByUserId(list.get(index).getUserId());
                if(postList != null && postList.size() > 0) {
                    StringBuilder postId = new StringBuilder(postList.get(0).getPostId().toString());
                    StringBuilder postName = new StringBuilder(postList.get(0).getPostName());
                    for (int i = 1; i < postList.size(); i++) {
                        postId.append("," + postList.get(i).getPostId());
                        postName.append("," + postList.get(i).getPostName());
                    }
                    list.get(index).setPostIds(postId.toString());
                    list.get(index).setPostNames(postName.toString());
                }
            }
        }

        return new PageResult<CscpUserDetailDTO>(list, info.getTotal(), info.getTotal());
    }

    @Override
    public CscpUserDetailDTO findByUserId(Long userId) {
        //用户信息
        CscpUserDetailDTO userDetailDTO = cscpUserDetailRepository.queryByUserId(userId);
        if (userDetailDTO != null) {
            //用户的角色信息
            List<CscpRoles> roleList = cscpUserRoleRepository.queryRoleByUserId(userDetailDTO.getUserId());
            if (roleList != null && roleList.size() > 0) {
                StringBuilder idsb = new StringBuilder(roleList.get(0).getId().toString());
                StringBuilder namesb = new StringBuilder(roleList.get(0).getName());
                for (int i = 1; i < roleList.size(); i++) {
                    idsb.append("," + roleList.get(i).getId());//1,2
                    namesb.append("," + roleList.get(i).getName());//admin,system
                }
                userDetailDTO.setRoleIds(idsb.toString());
                userDetailDTO.setRoleNames(namesb.toString());
            }
            userDetailDTO.setRolesList(roleList);
            //获取岗位信息
            List<CscpPost> postList = cscpUserPostRepository.queryPostByUserId(userDetailDTO.getUserId());
            if(postList != null && postList.size() > 0){
                StringBuilder idsb = new StringBuilder(postList.get(0).getPostId().toString());
                StringBuilder namesb = new StringBuilder(postList.get(0).getPostName());
                for(int i = 1; i < postList.size(); i++){
                    idsb.append(","+postList.get(i).getPostId());
                    namesb.append(","+postList.get(i).getPostName());
                }
                userDetailDTO.setPostIds(idsb.toString());
                userDetailDTO.setPostNames(namesb.toString());
            }
            userDetailDTO.setPostsList(postList);
            //获取工作组
/*            List<CscpUserWorkGroupDTO> cscpUserWorkGroupDTOS = cscpUserWorkGroupRepository.selectUserWorkGroupByUserId(userId);
            List<String> groups = new ArrayList<>();
            for (CscpUserWorkGroupDTO cscpUserWorkGroupDTO : cscpUserWorkGroupDTOS) {
                groups.add(cscpUserWorkGroupDTO.getGroupId().toString());
            }
            userDetailDTO.setGroups(groups);*/
            //获取所在机构
/*            CscpUserOrg cscpUserOrg = cscpUserOrgRepository.selectUserOrgByUserId(userId);
            if(cscpUserOrg != null){
                userDetailDTO.setOrgId(cscpUserOrg.getOrgId());
            }*/

        }
        return userDetailDTO;
    }

    @Override
    public List<CscpUserDetailDTO> selectByTenantId(Long tenantId) {


        return cscpUserDetailRepository.selectByTenantId(tenantId);
    }

    /**
     * 修改用户信息
     *
     * @param cscpUserUpdateDTO
     * @return
     */
    @Override
    public CscpUserDetailDTO updateUserDTO(CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception {
        CscpUserDetailDTO userDetailDTO = cscpUserUpdateDTO.getCscpUserDetailDTO();
        log.debug("Request to update CscpUserDetail : {}", userDetailDTO);

        //获取待修改用户的id
        Long userId = userDetailDTO.getUserId();
        CscpUser cscpUser = cscpUserRepository.selectByPrimaryKey(userId);
        containUserNameCheck.setUserName(cscpUser.getUsername());
        if(userDetailDTO.getTenantAccount() != null){
            containTenantAccountCheck.setTenantAccount(userDetailDTO.getTenantAccount());
        }
        CscpUserDetail cscpUserDetail = cscpUserDetailMapper.toEntity(userDetailDTO);
        //保存用户详细信息
        cscpUserDetailRepository.updateByPrimaryKeySelective(cscpUserDetail);
        // 保存用户角色
        cscpUserRoleService.saveUserRoles(userDetailDTO.getUserId(), userDetailDTO.getRoles());
        //保存用户岗位
        cscpUserPostService.saveUserPost(userDetailDTO.getUserId(),userDetailDTO.getPosts());

        return cscpUserUpdateDTO.getCscpUserDetailDTO();
    }



    /**
     * @Description: 用于查询工作流用户模块人员、角色等信息
     * @Author: sunsheng
     **/
    @Override
    public FlowUserTaskInfo findFlowUserTaskInfo() {
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        List<CscpUserDetail> userDetailList = cscpUserDetailRepository.selectAllIdAndUserName(tenantId);
        List<CscpRoles> cscpRoles = cscpRolesRepository.selectAllRoles(tenantId);
        FlowUserTaskInfo userTaskInfo = new FlowUserTaskInfo();
        userTaskInfo.setUsers(userDetailList);
        userTaskInfo.setRoles(cscpRoles);
        return userTaskInfo;
    }

    /**
     * @param deptIds
     * @Description: 根据部门id查询用户信息
     * @Author: sunsheng
     */
    @Override
    public List<CscpUser> getUsersByDeptIds(Set<Long> deptIds) {
        return cscpUserDetailRepository.getUsersByDeptIds(deptIds);
    }



    /**
     * 解密
     *
     * @param ciphertext 密码的密文
     * @return
     */

    private String decryptPassword(String ciphertext) {
        String password = ciphertext;
        try {
            if (org.apache.commons.lang.StringUtils.isNotBlank(rsaPrikey)) {
                password = new String(
                        RSAUtil.decryptPri(Base64.decodeBase64(ciphertext), Base64.decodeBase64(rsaPrikey)), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return password;
    }

    /**
     * 保存工作组和机构
     * @param cscpUserDetailDTO
     */
    private void saveUserWkOrg(CscpUserDetailDTO cscpUserDetailDTO){
        // 保存机构
        cscpUserOrgRepository.deleteByUserId(cscpUserDetailDTO.getUserId());
        if(cscpUserDetailDTO.getOrgId() != null){
            CscpUserOrg cscpUserOrg = new CscpUserOrg();
            cscpUserOrg.setUserId(cscpUserDetailDTO.getUserId());
            cscpUserOrg.setOrgId(cscpUserDetailDTO.getOrgId());
            cscpUserOrgRepository.insert(cscpUserOrg);

        }

        // 保存工作组
        cscpUserWorkGroupRepository.deleteByUserId(cscpUserDetailDTO.getUserId());
        if(cscpUserDetailDTO.getGroups() != null && cscpUserDetailDTO.getGroups().size()>0){
            CscpUserWorkGroup cscpUserWorkGroup = new CscpUserWorkGroup();
            cscpUserWorkGroup.setUserId(cscpUserDetailDTO.getUserId());
            List<String> groups = cscpUserDetailDTO.getGroups();
            for (String group : groups) {
                cscpUserWorkGroup.setGroupId(Long.valueOf(group));
                cscpUserWorkGroup.setId(null);
                cscpUserWorkGroupRepository.insert(cscpUserWorkGroup);
            }
        }
    }
}
