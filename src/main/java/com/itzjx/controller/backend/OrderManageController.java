package com.itzjx.controller.backend;

import com.github.pagehelper.PageInfo;
import com.itzjx.annotation.Admin;
import com.itzjx.common.Const;
import com.itzjx.common.ResponseCode;
import com.itzjx.common.ServerResponse;
import com.itzjx.pojo.User;
import com.itzjx.service.IOrderService;
import com.itzjx.service.IUserService;
import com.itzjx.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @Admin
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //查询订单列表
        return iOrderService.manageList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    @Admin
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo) {
        //查询某订单详情
        return iOrderService.manageDetail(orderNo);
    }


    @RequestMapping("search.do")
    @Admin
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //与detail类似
        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    /**
     * 前台下订单之后，后台发货
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("send_goods.do")
    @Admin
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo) {
        //订单成功支付后发货
        return iOrderService.manageSendGoods(orderNo);
    }


}
