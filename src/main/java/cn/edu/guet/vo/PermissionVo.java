package cn.edu.guet.vo;
import cn.edu.guet.bean.Permission;
import java.util.List;

//View Object
public class PermissionVo {
    private String status;
    private List<Permission> permissions;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
