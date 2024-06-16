package org.m.web.controller.system;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.m.common.entity.po.MenuSettingPo;
import org.m.web.service.IMenuSettingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 用户表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/system/menu/")
@Tag(name = "菜单")
public class MenuController {


    @Resource
    private IMenuSettingService iMenuSettingService;

    @RequestMapping("adminMenu")
    public SaResult adminMenu() throws IOException {
        List<MenuSettingPo> list = iMenuSettingService.list();
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setDeep(3);
        List<Tree<String>> treeNodes = TreeUtil.build(list, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId() + "");
                    tree.setParentId(treeNode.getParent() + "");
                    tree.put("path", treeNode.getPath());
                    tree.put("name", treeNode.getName());
                    if(StrUtil.isNotEmpty(treeNode.getRedirect())){
                        tree.put("redirect", treeNode.getRedirect());
                    }
                    if(StrUtil.isNotEmpty(treeNode.getComponent())){
                        tree.put("component", treeNode.getComponent());
                    }
                    tree.put("meta", treeNode.getMeta());
                });
        return SaResult.data(treeNodes);
    }
}