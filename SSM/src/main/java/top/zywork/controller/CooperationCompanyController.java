package top.zywork.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zywork.common.Message;
import top.zywork.common.PagingBean;
import top.zywork.enums.ActiveStatusEnum;
import top.zywork.query.PageQuery;
import top.zywork.query.StatusQuery;
import top.zywork.service.CooperationCompanyService;
import top.zywork.service.EmployeeService;
import top.zywork.service.HotelService;
import top.zywork.vo.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenfeilong on 2017/12/6.
 */
@Controller
@RequestMapping("cooperationCompany")
public class CooperationCompanyController {
    @Resource
    private CooperationCompanyService cooperationCompanyService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private HotelService hotelService;
    @RequestMapping("cooperationCompanyList")
    @ResponseBody
    public PagingBean cooperationCompanyList(int pageSize, int pageIndex, String searchVal, HttpSession session) throws  Exception{
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
        EmployeeVo employeeVo = employeeService.getHotelId(userVo.getId());
        PagingBean pagingBean = new PagingBean();
        pagingBean.setPageSize(pageSize);
        pagingBean.setCurrentPage(pageIndex);
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(pagingBean.getStartIndex());
        pageQuery.setPageSize(pagingBean.getPageSize());
        pageQuery.setCompanyId(userVo.getCompanyId());
        pageQuery.setSearchVal(searchVal);
        //总管理员查询商家
        if(userRoleVo.getRoleVo().getTitle().equals("店长")){
            HotelVo hotelVo = hotelService.findHotel(userVo.getId());
            pageQuery.setHotelId(hotelVo.getId());
        }
        //员工查询商家
        if(employeeVo!=null){
            if(employeeVo.getHotelId()!=-1){
                pageQuery.setHotelId(employeeVo.getHotelId());
            }
        }
        pagingBean.setTotal(cooperationCompanyService.count(pageQuery));

        pagingBean.setrows(cooperationCompanyService.listPage(pageQuery));
        return pagingBean;
    }
    @RequestMapping("/cooperationCompanyAddSave")
    @ResponseBody
    public Message addSaveCooperationCompany(CooperationCompanyVo cooperationCompany, HttpSession session) throws  Exception {
        try{
            UserVo userVo = (UserVo) session.getAttribute("userVo");
            UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
            EmployeeVo employeeVo = employeeService.getHotelId(userVo.getId());
            if(userRoleVo.getRoleVo().getTitle().equals("录入员")){
                if(employeeVo.getHotelId()!=-1){
                    cooperationCompany.setHotelId(employeeVo.getHotelId());
                    cooperationCompany.setShopManagerId(employeeVo.getUserId());
                }
            }else if(userRoleVo.getRoleVo().getTitle().equals("店长")){
                cooperationCompany.setShopManagerId(userVo.getId());
                HotelVo hotelVo = hotelService.findHotel(userVo.getId());
                cooperationCompany.setHotelId(hotelVo.getId());
            }
            cooperationCompany.setIsActive(ActiveStatusEnum.ACTIVE.getValue().byteValue());
            cooperationCompany.setCompanyId(userVo.getCompanyId());
            cooperationCompanyService.save(cooperationCompany);
            return  Message.success("新增成功!");
        }catch (Exception E){
            return Message.fail("新增失败!");
        }

    }
    @RequestMapping("/findCooperationCompany/{id}")
    @ResponseBody
    public CooperationCompanyVo findcooperationCompany(@PathVariable("id") long id){
        CooperationCompanyVo cooperationCompany = cooperationCompanyService.getById(id);
        return cooperationCompany;
    }
    @RequestMapping("/cooperationCompanyUpdateSave")
    @ResponseBody
    public Message updatecooperationCompany(CooperationCompanyVo cooperationCompany) throws  Exception{
        try{
            cooperationCompanyService.update(cooperationCompany);
            return  Message.success("修改成功!");
        }catch (Exception e){
            return Message.fail("修改失败!");
        }
    }
    @RequestMapping("/deleteManyCooperationCompany")
    @ResponseBody
    public Message deleteManycooperationCompany(@Param("manyId") String manyId) throws  Exception{
        try{
            String str[] = manyId.split(",");
            for (String s: str) {
                cooperationCompanyService.removeById(Long.parseLong(s));
            }
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return  Message.fail("删除失败!");
        }
    }
    @RequestMapping("/deleteCooperationCompany/{id}")
    @ResponseBody
    public Message deletecooperationCompany(@PathVariable("id") long id) throws  Exception{
        try{
            cooperationCompanyService.removeById(id);
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("删除失败!");
        }
    }
    @RequestMapping("/cooperationCompanyPage")
    public String table() throws  Exception{
        return "moneyItems/cooperationCompanyList";
    }
    @RequestMapping("updateStatus/{id}/{status}")
    @ResponseBody
    public Message updateStatus(@PathVariable("id") long id,@PathVariable("status") int status) throws  Exception{
        try{
            cooperationCompanyService.updateStatus(new StatusQuery(id,status));
            return Message.success("ok");
        }catch (Exception e){
            return  Message.fail("fail");
        }
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
