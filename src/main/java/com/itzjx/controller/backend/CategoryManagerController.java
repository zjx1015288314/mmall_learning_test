package com.itzjx.controller.backend;

import com.itzjx.annotation.Admin;
import com.itzjx.common.Const;
import com.itzjx.common.ResponseCode;
import com.itzjx.common.ServerResponse;
import com.itzjx.pojo.User;
import com.itzjx.service.ICategoryService;
import com.itzjx.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryManagerController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @Admin
    public ServerResponse addCategory(
            HttpSession session, String categoryName,
            @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.addCategory(categoryName, parentId);

    }

    @RequestMapping("set_category_name.do")
    @Admin
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        //更新categoryName
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    @RequestMapping("get_category.do")
    @Admin
    public ServerResponse getChildrenParallelCategory(
            HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //查询子节点的category信息，并且不递归，保持平级
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    @RequestMapping("get_deep_category.do")
    @Admin
    public ServerResponse getCategoryAndDeepChildrenCategory(
            HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //查询当前节点的id和递归子节点的id
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
