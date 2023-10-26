package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.CscpUserExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserPasswordUpdate;
import com.ctsi.ssdc.admin.repository.*;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserMapper;
import com.ctsi.ssdc.cache.LoginCache;
import com.ctsi.ssdc.model.*;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.RSAUtil;
import com.github.pagehelper.PageHelper;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpUser.
 *
 * @author ctsi biyi generator
 */
@Service
public class CscpUserServiceImpl implements CscpUserService {

    private final Logger log = LoggerFactory.getLogger(CscpUserServiceImpl.class);

    @Autowired
    private CscpUserRepository cscpUserRepository;

    private final CscpUserMapper cscpUserMapper;

    @Autowired
    private CscpUserWorkGroupRepository cscpUserWorkGroupRepository;

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    private CscpDeptRepository cscpDeptRepository;

    @Autowired
    private CscpRoleMenuRepository cscpRoleMenuRepository;

    @Autowired
    private CscpUserOrgRepository cscpUserOrgRepository;

    @Autowired
    private CscpUserRoleRepository cscpUserRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CscpUserPasswordChangeLogRepository cscpUserPasswordChangeLogRepository;


    @Value("${ctsi.RSA-prikey:}")
    private String rsaPrikey = "";

    @Value("${ctsi.login-password:#{null}}")
    private String defaultPassward;

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
    private CscpUserPostRepository cscpUserPostRepository;

    @Resource(name = "${ctsi.login.cache:guavaLoginCache}")
    LoginCache loginCache;

    public CscpUserServiceImpl(CscpUserMapper cscpUserMapper) {
        this.cscpUserMapper = cscpUserMapper;
    }

    /**
     * insert a cscpUser.
     *
     * @param cscpUserDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpUserDTO insert(CscpUserDTO cscpUserDTO) {
        log.debug("Request to insert CscpUser : {}", cscpUserDTO);

        CscpUser cscpUser = cscpUserMapper.toEntity(cscpUserDTO);
        cscpUserRepository.insert(cscpUser);

        CscpUserDetail userDetail = new CscpUserDetail();
        userDetail.setUserId(cscpUser.getId());
        userDetail.setRegisterTime(ZonedDateTime.now());
        cscpUserDetailRepository.insert(userDetail);

        return cscpUserMapper.toDto(cscpUser);
    }

    /**
     * update a cscpUser.
     *
     * @param cscpUserDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpUserDTO update(CscpUserDTO cscpUserDTO) {
        log.debug("Request to update CscpUser : {}", cscpUserDTO);

        CscpUser cscpUser = cscpUserMapper.toEntity(cscpUserDTO);
        cscpUserRepository.updateByPrimaryKeySelective(cscpUser);
        return cscpUserMapper.toDto(cscpUser);
    }


    /**
     * Get all the cscpUsers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserDTO> findAll() {
        log.debug("Request to get all CscpUsers");

        List<CscpUserDTO> data = cscpUserRepository.selectByExample(null).stream()
                .map(cscpUserMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserRepository.countByExample(null);
        }

        return new PageResult<CscpUserDTO>(data, count, count);

    }

    /**
     * Get one cscpUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpUserDTO findOne(Long id) {
        log.debug("Request to get CscpUser : {} ", id);

        CscpUser cscpUser = cscpUserRepository.selectByPrimaryKey(id);
        return cscpUserMapper.toDto(cscpUser);
    }

    /**
     * Delete the cscpUser .
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CscpUser : {} ", id);
        try {
            // 删除用户
            cscpUserRepository.deleteByPrimaryKey(id);

            //删除角色关联信息
            CscpUserRoleExample cscpUserRoleExample = new CscpUserRoleExample();
            CscpUserRoleExample.Criteria criteria = cscpUserRoleExample.createCriteria();
            criteria.andUserIdEqualTo(id);
            cscpUserRoleRepository.deleteByExample(cscpUserRoleExample);

            //删除岗位关联信息
            CscpUserPostExample cscpUserPostExample = new CscpUserPostExample();
            CscpUserPostExample.Criteria criteria1= cscpUserPostExample.createCriteria();
            criteria1.andUserIdEqualTo(id);
            cscpUserPostRepository.deleteByExample(cscpUserPostExample);

            //删除用户详细信息
            CscpUserDetailExample cscpUserDetailExample = new CscpUserDetailExample();
            CscpUserDetailExample.Criteria userDetailExampleCriteria = cscpUserDetailExample.createCriteria();
            userDetailExampleCriteria.andUserIdEqualTo(id);
            cscpUserDetailRepository.deleteByExample(cscpUserDetailExample);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String getPageOrderBy(Pageable page) {

        if (page != null && page.getSort() != null) {

            StringBuilder sb = new StringBuilder();

            page.getSort().forEach(sort -> sb.append(sort.getProperty())
                    .append(" ").append(sort.getDirection()).append(","));

            if (sb.length() > 1) {
                return (sb.substring(0, sb.length() - 1));
            }
        }

        return null;
    }

    /**
     * Get the cscpUsers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserDTO> findByCscpUserDTO(CscpUserDTO cscpUserDTO, Pageable page) {

        log.debug("Request to find CscpUsers");

        CscpUserExample example = new CscpUserExample();

        example.setPage(page);

        Criteria critieria = example.createCriteria();

        if (cscpUserDTO.getId() != null) {
            critieria.andIdEqualTo(cscpUserDTO.getId());
        }

        if (StringUtils.isNotBlank(cscpUserDTO.getUsername())) {
            critieria.andUsernameLike(String.format("%%%s%%", cscpUserDTO.getUsername()));
        }
        if (StringUtils.isNotBlank(cscpUserDTO.getPassword())) {
            critieria.andPasswordLike(String.format("%%%s%%", cscpUserDTO.getPassword()));
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpUserDTO> data = cscpUserMapper.toDto(cscpUserRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserRepository.countByExample(example);
        }

        return new PageResult<CscpUserDTO>(data, count, count);

    }

    /**
     * Get the cscpUsers.
     *
     * @param cscpUserCriteria
     * @param page
     * @return
     */
    @Override
    public PageResult<CscpUserDTO> findByCscpUserCriteria(CscpUserCriteria cscpUserCriteria, Pageable page) {

        CscpUserExample example = new CscpUserExample();

        example.setPage(page);

        Criteria criteria = example.createCriteria();

        if (cscpUserCriteria != null) {
            cscpUserCriteria.buildCriteria(criteria);
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpUserDTO> data = cscpUserMapper.toDto(cscpUserRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpUserRepository.countByExample(example);
        }

        return new PageResult<CscpUserDTO>(data, count, count);

    }

    @Override
    public UpdatePasswordResult updatePassword(
            CscpUserPasswordUpdate cscpUserPasswordUpdate, boolean checkOld) throws Exception {
        //cscpUserPasswordChangeLogRepository
        CscpUserDetailDTO cscpUserDetailDTO = cscpUserDetailRepository.finUserByUsernameAndTenantId(cscpUserPasswordUpdate.getTenantId(), cscpUserPasswordUpdate.getUserName());
        CscpUser cscpUser = new CscpUser();
        if (cscpUserDetailDTO != null) {
            cscpUser.setId(cscpUserDetailDTO.getUserId());
            cscpUser.setUsername(cscpUserDetailDTO.getUsername());
            cscpUser.setPassword(cscpUserDetailDTO.getPassword());
        }else{
            Long userId = cscpUserPasswordUpdate.getUserId();
            if (userId != null) {
                cscpUser = cscpUserRepository.selectByPrimaryKey(userId);
            } else {
                cscpUser = findByUsername(SecurityHxUtils.getCurrentUserName());
            }
        }


//        if (cscpUser == null) {
//            throw new Exception("用户不存在!");
//        }
        if (checkOld) {
            String inputPwd = decryptPassword(cscpUserPasswordUpdate.getOldPassword());
            if (!passwordEncoder.matches(inputPwd, cscpUser.getPassword())) {
                throw new Exception("与原密码不符!");
            }
        }
        //校验密码复杂度
        String newPassword = decryptPassword(cscpUserPasswordUpdate.getNewPassword());


        CheckResult checkResult = new CheckResult();

        lengthCheck.check(newPassword, checkResult);
        lowerCaseCheck.check(newPassword, checkResult);
        upperCaseCheck.check(newPassword, checkResult);
        containDigitCheck.check(newPassword, checkResult);
        specialCharCheck.check(newPassword, checkResult);
        containUserNameCheck.setUserName(cscpUser.getUsername());
        containUserNameCheck.check(newPassword, checkResult);
        if(cscpUserPasswordUpdate.getTenantAccount() != null){
            containTenantAccountCheck.setTenantAccount(cscpUserPasswordUpdate.getTenantAccount());
            containTenantAccountCheck.check(newPassword, checkResult);
        }
        keyBoardCheckNew.check(newPassword, checkResult);

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
            stringBuffer.append("密码应包含");
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
                if (passwordEncoder.matches(newPassword, changeLogs.get(i).getPassword())) {
//                    throw new Exception("密码使用过");
                    stringBuffer.append(" 密码已使用过");
                }
            }
        }

        if (stringBuffer.length() > 0) {
            throw new Exception(stringBuffer.toString());
        }

        cscpUser.setPassword(passwordEncoder.encode(decryptPassword(cscpUserPasswordUpdate.getNewPassword())));
        cscpUserRepository.updateByPrimaryKey(cscpUser);

        CscpUserPasswordChangeLog cscpUserPasswordChangeLog = new CscpUserPasswordChangeLog();
        cscpUserPasswordChangeLog.setUserId(cscpUser.getId());
        cscpUserPasswordChangeLog.setTime(ZonedDateTime.now());
        cscpUserPasswordChangeLog.setPassword(passwordEncoder.encode(
                decryptPassword(cscpUserPasswordUpdate.getNewPassword())));
        cscpUserPasswordChangeLogRepository.insert(cscpUserPasswordChangeLog);

        return new UpdatePasswordResult(true, cscpUser.getId(),
                cscpUserPasswordUpdate.getNewPassword(), cscpUser.getUsername());
    }

    @Override
    public boolean existByUsername(String username) {
//        CscpUserExample example = new CscpUserExample();
//
//        Criteria criteria = example.createCriteria();
//        if (StringUtils.isNotBlank(username)) {
//            criteria.andUsernameEqualTo(username);
//        }
        Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
        Long countByUsername = cscpUserRepository.countUserByName(username,currentTenantId);


//        Long countByUsername = cscpUserRepository.countByExample(example);

        if (countByUsername > 0) {
            return true;
        }

        return false;
    }

    @Override
    public CscpUser findByUsername(String username) {
        CscpUserExample example = new CscpUserExample();

        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(username)) {
            criteria.andUsernameEqualTo(username);
            List<CscpUser> users = cscpUserRepository.selectByExample(example);
            if (CollectionUtils.isNotEmpty(users)) {
                return users.get(0);
            }
        }


        return null;
    }

    @Override
    public int passwordNeedChange(Long userId, String password) {

        if (StringUtils.isNotBlank(defaultPassward) && defaultPassward.equals(password)) {
            return 1;
        }

        CscpUserPasswordChangeLogExample cscpUserPasswordChangeLogExample = new CscpUserPasswordChangeLogExample();
        CscpUserPasswordChangeLogExample.Criteria criteria = cscpUserPasswordChangeLogExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        cscpUserPasswordChangeLogExample.setOrderByClause("time desc");


        List<CscpUserPasswordChangeLog> changeLogs = cscpUserPasswordChangeLogRepository.
                selectByExample(cscpUserPasswordChangeLogExample);
        if (CollectionUtils.isNotEmpty(changeLogs)) {
            if (changeLogs.get(0).getTime().plusMonths(expireMonth).compareTo(ZonedDateTime.now()) < 0) {
                return 2;
            }

        } else {
            //未找到修改密码记录，为老用户，为其创建一条修改记录
            CscpUserPasswordChangeLog cscpUserPasswordChangeLog = new CscpUserPasswordChangeLog();
            cscpUserPasswordChangeLog.setUserId(userId);
            cscpUserPasswordChangeLog.setPassword(passwordEncoder.encode(password));
            cscpUserPasswordChangeLog.setTime(ZonedDateTime.now());
            cscpUserPasswordChangeLogRepository.insert(cscpUserPasswordChangeLog);
        }
        return 0;
    }

    /**
     * 解密
     *
     * @param ciphertext 密码的密文
     * @return
     */

    @Override
    public String decryptPassword(String ciphertext) {
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

    @Override
    public boolean deblockingAccount(String username) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("loginFailed:");
        stringBuffer.append(username);
        String countKey = stringBuffer.toString();
        if (loginCache != null) {
            Integer countKeyValue = (Integer) loginCache.get(countKey);
            if (countKeyValue != null) {
                long lockTime = loginCache.getExpire(countKey);
                if (lockTime > 0){
                    return loginCache.delete(countKey);
                }
            }
        }
        return false;
    }

    public static class UpdatePasswordResult {
        boolean result;
        long userId;
        String password;
        String userName;

        public UpdatePasswordResult(boolean result, long userId, String password, String userName) {
            this.result = result;
            this.userId = userId;
            this.password = password;
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
