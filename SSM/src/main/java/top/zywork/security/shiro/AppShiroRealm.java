package top.zywork.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import top.zywork.query.UserAccountPasswordQuery;
import top.zywork.service.PermissionService;
import top.zywork.service.RoleService;
import top.zywork.service.UserService;
import top.zywork.vo.PermissionVo;
import top.zywork.vo.RoleVo;
import top.zywork.vo.UserVo;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义的Shiro Realm<br />
 * 创建于2017-09-05
 *
 * @author 王振宇
 * @version 1.0
 */
@Component
public class AppShiroRealm extends AuthorizingRealm {

    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<RoleVo> roleVoList = roleService.listByAccount(username);
        for (RoleVo roleVo : roleVoList) {
            authorizationInfo.addRole(roleVo.getTitle());
        }
        List<PermissionVo> permissionVoList = permissionService.listByAccount(username);
        for (PermissionVo permissionVo : permissionVoList) {
            authorizationInfo.addStringPermission(permissionVo.getPermission());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();
        String password = String.valueOf((char[])authenticationToken.getCredentials());
        UserVo userVo = userService.getByAccountPassword(new UserAccountPasswordQuery(username, password));
        if (userVo != null) {
            return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), getName());
        }
        return null;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Resource
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Resource
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
}
