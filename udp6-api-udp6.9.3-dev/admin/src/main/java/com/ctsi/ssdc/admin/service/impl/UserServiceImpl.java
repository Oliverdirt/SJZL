package com.ctsi.ssdc.admin.service.impl;

import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.repository.CscpMenusRepository;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRoleRepository;
import com.ctsi.ssdc.admin.service.UserService;
import com.ctsi.ssdc.exception.DuplicateUserNameException;
import com.ctsi.ssdc.model.MenuItemBean;
import com.ctsi.ssdc.model.TUserDetail;
import com.ctsi.ssdc.util.CopyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    private CscpUserRepository cscpUserRepository;

    @Autowired
    private CscpMenusRepository cscpMenusRepository;

    @Autowired
    private CscpUserRoleRepository cscpUserRoleRepository;

    public List<CscpUser> searchAll() {
        return cscpUserRepository.selectByExample(null);
    }


    @Override
    public Optional<CscpUser> findByUserName(String userName) {
        CscpUserExample example = new CscpUserExample();
        example.createCriteria().andUsernameEqualTo(userName);
        List<CscpUser> list = cscpUserRepository.selectByExample(example);

        if (list.size() > 1) {
            throw new DuplicateUserNameException("多个用户同名，请跟管理员确认");
        }
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        return Optional.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDetailForLogin(String userId) {
        CscpUserDetailExample example = new CscpUserDetailExample();
        example.createCriteria().andUserIdEqualTo(Long.parseLong(userId));
        List<CscpUserDetail> tudl = cscpUserDetailRepository.selectByExample(example);

        CscpUserDetail tud;
        boolean isnew = false;
        if (tudl == null || tudl.isEmpty()) {
            tud = new CscpUserDetail();
            tud.setRegisterTime(ZonedDateTime.now());
            tud.setUserId(Long.parseLong(userId));
            isnew = true;
        } else {
            tud = tudl.get(0);
        }
        tud.setLastLogin(ZonedDateTime.now());

        tud.setRegisterTime(ZonedDateTime.now());
        if (isnew) {

            cscpUserDetailRepository.insert(tud);
        } else {

            cscpUserDetailRepository.updateByPrimaryKeySelective(tud);
        }
    }
    @Override
    public void updateUserDetail(TUserDetail t) {
        CscpUserDetail detail = new CscpUserDetail();
        try {
            CopyUtil.copy(t, detail);
            detail.setLastLogin(t.getLastLogin());
            detail.setRegisterTime(t.getRegisterTime());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        cscpUserDetailRepository.updateByPrimaryKeySelective(detail);

    }

    @Override
    public String insertUserInfo(String userName, String password) {
        CscpUser record = new CscpUser();
        record.setUsername(userName);
        record.setPassword(password);
        cscpUserRepository.insert(record);

        return record.getId().toString();
    }

    @Transactional
    public String insertUserInfoBag(String userName, String password) {
        String result = this.insertUserInfo(userName, password);

        this.saveUserRoles(result, "3");
        return result;
    }

    public MenuItemBean findMenuList(String userId) {

        try {
            List l = cscpMenusRepository.selectByUserId(Long.parseLong(userId));
            HashMap<String, MenuItemBean> itemMap = new HashMap<String, MenuItemBean>();
            List list = new ArrayList<MenuItemBean>();
            for (int i = 0; i < l.size(); i++) {
                MenuItemBean mib = new MenuItemBean();
                String[] item = (String[]) l.get(i);
                if ("parent".equals(item[5]) || item[3] == null) {
                    mib.setHref("#");
                } else {
                    mib.setSref(item[3]);
                }
                mib.setTitle(item[1]);
                mib.setIcon(item[2]);
                mib.setId(item[0]);

                if (itemMap.get(item[4]) != null) {
                    MenuItemBean pmib = (MenuItemBean) itemMap.get(item[4]);
                    List<MenuItemBean> subitem = (pmib).getItems();
                    if (subitem == null) {
                        subitem = new ArrayList<MenuItemBean>();
                        pmib.setItems(subitem);
                    }
                    subitem.add(mib);

                } else {
                    list.add(mib);
                }
                itemMap.put(item[0], mib);
            }

            MenuItemBean ex = new MenuItemBean();
            ex.setItems(list);

            return ex;

        } catch (Exception e) {
            logger.error("findMenuList", e);
            return new MenuItemBean();
        }
    }

    @Override
    @Transactional
    public String saveUserRoles(String userId, String roles) {
        try {
            String[] ms = roles.split(",");

            CscpUserRoleExample example = new CscpUserRoleExample();
            example.or().andUserIdEqualTo(Long.parseLong(userId));
            cscpUserRoleRepository.deleteByExample(example);

            for (int i = 0; i < ms.length; i++) {
                CscpUserRole record = new CscpUserRole();
                record.setUserId(Long.parseLong(userId));
                record.setRoleId(Long.parseLong(ms[i]));
                cscpUserRoleRepository.insert(record);
            }

            return "true";
        } catch (Exception e) {
            logger.error("saveUserRoles", e);
            return "false";
        }
    }

    public MenuItemBean fallbackFindMenuList(String userId) {
        logger.debug("find menu list error:" + userId);
        return null;
    }

    @Override
    public MenuItemBean findUserMenuItems(String userId) {
        return this.findMenuList(userId);
    }

    @Override
    public CscpUser findByUserId(Long userId) throws Exception {
        CscpUserExample example = new CscpUserExample();
        example.createCriteria().andIdEqualTo(userId);
        List<CscpUser> list = cscpUserRepository.selectByExample(example);
        if (list.size() > 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateUser(CscpUser user) throws Exception {
        cscpUserRepository.updateByPrimaryKeySelective(user);
    }

    @Override
    public TUserDetail findUserDetailByUserId(String userId) {
        CscpUserDetailExample example = new CscpUserDetailExample();
        example.createCriteria().andUserIdEqualTo(Long.parseLong(userId));
        List<CscpUserDetail> list = cscpUserDetailRepository.selectByExample(example);
        if (list != null && list.size() > 0) {
            CscpUserDetail detail = list.get(0);
            TUserDetail t = new TUserDetail();
            try {
                CopyUtil.copy(detail, t);
                t.setLastLogin(detail.getLastLogin());
                t.setRegisterTime(detail.getRegisterTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return t;
        }
        return null;
    }

    @Override
    public void insertUserDetail(TUserDetail detail) {
        CscpUserDetail d = new CscpUserDetail();
        try {
            CopyUtil.copy(detail, d);
            d.setLastLogin(detail.getLastLogin());
            d.setRegisterTime(detail.getRegisterTime());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        cscpUserDetailRepository.insert(d);
    }

    @Override
    public String findUserDisplayName(String userId) throws Exception {
        if (userId == null || "".equals(userId)) {
            return "";
        }

        TUserDetail detail = findUserDetailByUserId(userId);
        CscpUser user = this.findByUserId(Long.parseLong(userId));
        if (detail == null) {
            return user.getUsername();
        }
        String display = (detail.getFamilyName() == null ? "" : detail.getFamilyName())
                + (detail.getName() == null ? "" : detail.getName());
        if ("".equals(display)) {
            return user.getUsername();
        }
        return display;
    }

    @Override
    public String findUserDisplayNameByUsername(String userName) throws Exception {

        if (StringUtils.isEmpty(userName)) {
            return StringUtils.EMPTY;
        }

        return this.findByUserName(userName).map(user -> {

            TUserDetail detail = findUserDetailByUserId(String.valueOf(user.getId()));

            if (detail == null) {
                return user.getUsername();
            }

            String display = StringUtils.join(detail.getFamilyName(), detail.getName());

            return StringUtils.isEmpty(display) ? user.getUsername() : display;

        }).orElse(StringUtils.EMPTY);


    }

    @Override
    public Optional<CscpUserDetailDTO> finUserByUsernameAndTenantId(Long tenantId, String username) {
        CscpUserDetailDTO cscpUserDetailDTO = cscpUserDetailRepository.finUserByUsernameAndTenantId(tenantId, username);
        if (cscpUserDetailDTO != null) {
            return Optional.of(cscpUserDetailDTO);
        }
        return Optional.empty();
    }

}
