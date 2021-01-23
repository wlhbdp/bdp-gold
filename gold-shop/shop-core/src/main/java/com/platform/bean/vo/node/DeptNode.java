package com.platform.bean.vo.node;

import com.platform.bean.entity.system.Dept;

import java.util.ArrayList;
import java.util.List;

/**
 * DeptNode
 *
 * @author wlhbdp
 * @version 2020/9/15 0015
 */
public class DeptNode extends Dept {

    private List<DeptNode> children = new ArrayList<>(10);

    public List<DeptNode> getChildren() {
        return children;
    }

    public void setChildren(List<DeptNode> children) {
        this.children = children;
    }
}
