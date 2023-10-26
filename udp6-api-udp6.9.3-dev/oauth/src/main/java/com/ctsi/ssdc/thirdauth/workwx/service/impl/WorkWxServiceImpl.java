package com.ctsi.ssdc.thirdauth.workwx.service.impl;

import com.ctsi.ssdc.admin.constants.AdminFlagConstant;
import com.ctsi.ssdc.admin.domain.CscpDept;
import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.CscpUserDetail;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.repository.CscpDeptRepository;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRepository;
import com.ctsi.ssdc.admin.service.CscpUserRoleService;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.thirdauth.workwx.consts.WorkWxLoginConstant;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxDepartment;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxLogin;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxUserInfo;
import com.ctsi.ssdc.thirdauth.workwx.service.WorkWxService;
import com.ctsi.ssdc.thirdauth.workwx.utils.WeiXinUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class WorkWxServiceImpl implements WorkWxService {

    //企业ID
    @Value("${workwx.appid}")
    private String appid;

    @Value("${workwx.secret}")
    private String secret;


    @Value("${workwx.tongXunLuSecret}")
    private String tongXunLuSecret;

    //应用AgentId
    @Value("${workwx.agentid}")
    private String agentid;

    //第三方网站指定自己的端口
    @Value("${workwx.redirectUri}")
    private String redirectUri;

    private final static String WORK_WEIXIN_LOGIN_FLAG = "thirdOauth";

    @Value("${ctsi.login-password:ctsi1234}")
    private String rawPassword;

    @Value("${ctsi.RSA-prikey:}")
    private String rsaPrikey = "";

    private final Long  INSERT_SUCCESS_NUM = 1L;


    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    private CscpDeptRepository cscpDeptRepository;


    @Resource
    private CscpUserRepository cscpUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CscpUserRoleService cscpUserRoleService;

    @Autowired
    private CscpUserService cscpUserService;

    private final Long WORK_WEIXIN_LOGIN_TYPE_CODE = 1L;



    /**
     * 获取token
     *
     * @return
     */
    @Override
    @Cacheable(key = WorkWxLoginConstant.WX_TOKEN, value = "wxToken")
    public String getAccessToken() {
        String token = WeiXinUtil.getFirstAccessToken(appid, secret);
        return token;
    }

    @Override
    public String getUserId(String code) {
        String token = this.getAccessToken();
        String userId = WeiXinUtil.getUserIdByToken(token, code);
        return userId;
    }

    public String getUserId(String code,String accessToken) {
        String userId = WeiXinUtil.getUserIdByToken(accessToken, code);
        return userId;
    }

    @Override
    public WorkWxUserInfo getUserInfo(String userId) {
        //逻辑判断token是否过期,如果过期刷新,没有过期直接拿来使用
        String token = getAccessToken();
        //根据userID和token获取用户的基本信息
        WorkWxUserInfo weiXinUserInfoDTO = WeiXinUtil.getUserInfoById(token, userId);
        return weiXinUserInfoDTO;
    }

    @Override
    public WorkWxLogin loginQrCode() throws UnsupportedEncodingException {
        String redirectUri1 = URLEncoder.encode(redirectUri, "utf-8");
        WorkWxLogin weiXinLoginDTO = new WorkWxLogin();
        weiXinLoginDTO.setAppid(appid);
        weiXinLoginDTO.setAgentid(agentid);
        weiXinLoginDTO.setRedirectUri(redirectUri1);
        return weiXinLoginDTO;
    }

    @Override
    public CscpUserDetailDTO oauth2Login(String code) {
        String userId = this.getUserId(code);
        if(StringUtils.isBlank(userId)){
            return  null;
        }
        // 根据userId,查询用户信息（手机号+用户名）
        WorkWxUserInfo userInfo = this.getUserInfo(userId);
        // 用户id,如admin等,根据手机号+用户名查询用户是否存在
        String name = userInfo.getUserid();
        String mobile = userInfo.getMobile();
        CscpUserDetailDTO user = cscpUserDetailRepository.findUserByNameAndMobile(name, mobile);

        return user;
    }

    @Override
    public List<WorkWxUserInfo> getAllUser(List<WorkWxDepartment> departmentWxDtoList) {
        //先获取所有的部门列表
        Long departmentId = 0L;
        //找出顶级部门：parentId:0
        for (WorkWxDepartment departmentWxDto : departmentWxDtoList) {
            if (departmentWxDto.getParentid() == 0) {
                departmentId = departmentWxDto.getId();
                break;
            }
        }
        String token = getTongXunLuToken();
        return WeiXinUtil.getDepartmentUsersDetail(token, departmentId, 1);
    }

    @Override
    public List<WorkWxDepartment> getDepartment(Long id) {
        String token = getTongXunLuToken();
        List<WorkWxDepartment> list = WeiXinUtil.getDepartmentList(token, id);
        return list;
    }

    /**
     * 通讯录的token
     *
     * @return
     */
    @Cacheable(key = WorkWxLoginConstant.WX_TONG_XUN_LU_TOKEN, value = "wxToken")
    public String getTongXunLuToken() {
        String token = WeiXinUtil.getFirstAccessToken(appid, tongXunLuSecret);
        return token;
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void syncDept(List<WorkWxDepartment> departmentList) {
        // 预防部门结构变化,删除所有导入部门信息，createBy为thirdOauth,
        cscpDeptRepository.deleteThirdOauth();
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        for (WorkWxDepartment departmentWxDto : departmentList) {
            CscpDept info = cscpDeptRepository.selectByPrimaryKey(departmentWxDto.getParentid());
            CscpDept cscpDept = new CscpDept();
            cscpDept.setCreateBy(WORK_WEIXIN_LOGIN_FLAG);
            cscpDept.setDeptId(departmentWxDto.getId());
            cscpDept.setDeptName(departmentWxDto.getName());
            cscpDept.setParentId(departmentWxDto.getParentid());
            cscpDept.setOrderNum(departmentWxDto.getOrder());
            cscpDept.setTenantId(tenantId);
            cscpDept.setCreateTime(ZonedDateTime.now());
            if ("0".equals(departmentWxDto.getParentid().toString())){
                cscpDept.setAncestors(",0,");
            } else {
                cscpDept.setAncestors(info.getAncestors() + departmentWxDto.getParentid()+",");
            }
            cscpDeptRepository.insert(cscpDept);
        }
    }
    /**
     * 同步企业微信人员至数据库
     * @param weiXinUserList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncUser(List<WorkWxUserInfo> weiXinUserList) {
        //批量删除第三方用户
        List<Long> userIds = cscpUserDetailRepository.selectUserIdsByThirdType("1");
        if (!userIds.isEmpty()){
            cscpUserRepository.deleteByIds(userIds);
            cscpUserDetailRepository.deleteByUserIds(userIds);
        }
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        List<CscpUserDetailDTO> currentUsers = cscpUserDetailRepository.selectByTenantId(1516727656356179970L);
        // 提取当前已有用户的手机号和用户名
        Map<String, String> userNameAndMobileMap = currentUsers.stream().collect(Collectors.toMap(CscpUserDetailDTO::getUsername, CscpUserDetailDTO::getMobile));
        // 过滤通讯录名单
        weiXinUserList = weiXinUserList.stream().filter(new Predicate<WorkWxUserInfo>() {
            @Override
            public boolean test(WorkWxUserInfo weiXinUserInfoDTO) {
                boolean flag = true;
                if (userNameAndMobileMap.containsKey(weiXinUserInfoDTO.getUserid())){
                    String value = userNameAndMobileMap.get(weiXinUserInfoDTO.getUserid());
                    if(StringUtils.isNotBlank(value) && value.equals(weiXinUserInfoDTO.getMobile())){
                        flag = false;
                    }
                };
                return flag;
            }
        }).collect(Collectors.toList());
        for (WorkWxUserInfo userInfoDTO : weiXinUserList) {
            CscpUser cscpUser = new CscpUser();
            cscpUser.setPassword(passwordEncoder.encode(rawPassword));
            cscpUser.setUsername(userInfoDTO.getUserid());
            cscpUserRepository.insert(cscpUser);
            CscpUserDetail userDetail = new CscpUserDetail();
            userDetail.setName(userInfoDTO.getName());
            userDetail.setUserId(cscpUser.getId());
            userDetail.setMobile(userInfoDTO.getMobile());
            userDetail.setEmail(userInfoDTO.getEmail());
            userDetail.setAdminFlag(AdminFlagConstant.NOMAL_USER);
            userDetail.setDeptId(userInfoDTO.getDepartment().get(0));
            userDetail.setTenantId(tenantId);
            userDetail.setThirdType(WORK_WEIXIN_LOGIN_TYPE_CODE);
            cscpUserDetailRepository.insert(userDetail);
        }
    }
}
