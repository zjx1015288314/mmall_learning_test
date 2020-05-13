package com.itzjx.controller.backend;

import com.google.common.collect.Maps;

import com.itzjx.annotation.Admin;
import com.itzjx.common.Const;
import com.itzjx.common.ResponseCode;
import com.itzjx.common.ServerResponse;
import com.itzjx.pojo.Product;
import com.itzjx.pojo.User;
import com.itzjx.service.IFileService;
import com.itzjx.service.IProductService;
import com.itzjx.service.IUserService;
import com.itzjx.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 保存(不需要id)或者更新(需要id)商品
     *
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @Admin
    public ServerResponse productSave(Product product) {
        //添加或者更新商品信息,如展示图
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 修改产品在售/下架/删除
     *
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @Admin
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        return iProductService.setSaleStatus(productId, status);
    }

    @RequestMapping("detail.do")
    @Admin
    public ServerResponse getDetail(Integer productId) {
        //获取商品详情
        return iProductService.manageProductDetail(productId);

    }

    @RequestMapping("list.do")
    @Admin
    public ServerResponse getList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    @RequestMapping("search.do")
    @Admin
    public ServerResponse productSearch(String productName, Integer productId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //根据商品名或者ID搜索
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping("upload.do")
    @Admin
    public ServerResponse upload(
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request) {
            //先将资源保存到本项目部署路径下upload文件夹，再上传到文件服务器，最后删除upload下文件
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping("richtext_img_upload.do")
    public Map richtextImgUpload(
            HttpSession session,
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//            "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }

}
