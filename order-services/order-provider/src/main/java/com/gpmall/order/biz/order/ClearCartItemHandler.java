package com.gpmall.order.biz.order;/**
 * Created by mic on 2019/8/1.
 */

import com.gpmall.commons.tool.exception.BizException;
import com.gpmall.order.biz.pipeline.HandlerChain;
import com.gpmall.order.constant.OrderRetCode;
import com.gpmall.shopping.ICartService;
import com.gpmall.shopping.dto.ClearCartItemRequest;
import com.gpmall.shopping.dto.ClearCartItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 腾讯课堂搜索【咕泡学院】
 * 官网：www.gupaoedu.com
 * 风骚的Mic 老师
 * create-date: 2019/8/1-下午5:05
 * 将购物车中的缓存失效
 */
@Slf4j
@Component
@Order(3)
public class ClearCartItemHandler extends AbstractOrderHandler{

    @Reference
    ICartService cartService;


    @Override
    public void handler(OrderContext context, HandlerChain<OrderContext, BizException> handlers) throws BizException {
        log.info("begin - ClearCartItemHandler,context:"+context);
        //TODO 缓存失效和下单是属于两个事物操作，需要保证成功，后续可以改造成消息队列的形式来实现
        ClearCartItemRequest request=new ClearCartItemRequest();
        request.setProductIds(context.getBuyProductIds());
        request.setUserId(context.getRequest().getUserId());
        ClearCartItemResponse response=cartService.clearCartItemByUserID(request);
        if(OrderRetCode.SUCCESS.getCode().equals(response.getCode())){
            handlers.handleNext(context);
        }else{
            throw new BizException(response.getCode(),response.getMsg());
        }
    }
}
