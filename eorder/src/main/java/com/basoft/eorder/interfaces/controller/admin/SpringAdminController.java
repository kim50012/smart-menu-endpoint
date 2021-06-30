package com.basoft.eorder.interfaces.controller.admin;


import com.basoft.eorder.application.base.query.AdminQueryFacade;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/api/v1")
@ResponseBody
public class SpringAdminController extends CQRSAbstractController {

//    private ObjectMapper objectMapper = new ObjectMapper();
    private AdminQueryFacade adminQueryFacade;

    SpringAdminController(){}


    @Autowired
    public SpringAdminController(
            CommandHandleEngine handleEngine,
            QueryHandler queryHandler,
            AdminQueryFacade adminQueryFacade

    ){
        super(queryHandler,handleEngine);
        this.adminQueryFacade =adminQueryFacade;
    }


    /**
     * ADMIN-商品分类查询|店铺标签查询|产品标签查询
     *
     * @param request
     * @param type         业务类型 1-餐厅 2-医院 3-购物 4-酒店
     * @param functionType 用途和功能类型 1-商品分类 2-店铺标签 3-产品标签
     * @return
     */
    @GetMapping("/category")
    @ResponseBody
    public Object queryCategoryExec(HttpServletRequest request,
                                    @RequestParam(value = "type", defaultValue = "") String type,
                                    @RequestParam(value = "functionType", defaultValue = "") String functionType) {
        return adminQueryFacade.getRootCategory(type, functionType);
    }

    /**
     * ADMIN-店铺标签查询|产品标签查询
     *
     * @param request
     * @param type         业务类型 1-餐厅 2-医院 3-购物 4-酒店
     * @param functionType 用途和功能类型 2-店铺标签 3-产品标签
     * @param manageType 标签管理方 1-Admin CMS 2-Manager CMS 3-All CMS
     * @return
     */
    @GetMapping("/admintags")
    @ResponseBody
    public Object queryCategoryExec(HttpServletRequest request,
                                    @RequestParam(value = "type", defaultValue = "") String type,
                                    @RequestParam(value = "functionType", defaultValue = "") String functionType,
                                    @RequestParam(value = "manageType", defaultValue = "") String manageType) {
        return adminQueryFacade.getTags(type, functionType, manageType);
    }


    @GetMapping("/option")
    @ResponseBody
    public Object queryOptionExec(HttpServletRequest request,@RequestParam(value = "type", defaultValue = "") String type) {
        return adminQueryFacade.getRootOption(type);
    }

}
