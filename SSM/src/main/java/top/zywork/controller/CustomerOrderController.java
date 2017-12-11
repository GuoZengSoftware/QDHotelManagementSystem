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
import top.zywork.service.CustomerOrderService;
import top.zywork.service.EmployeeService;
import top.zywork.service.HotelService;
import top.zywork.service.HouseService;
import top.zywork.vo.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenfeilong on 2017/10/21.
 */
@Controller
@RequestMapping("customerOrder")
public class CustomerOrderController {

    @Resource
    private CustomerOrderService customerOrderService;
    @Resource
    private HotelService hotelService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private HouseService houseService;
    @RequestMapping("customerOrderList")
    @ResponseBody
    public PagingBean customerOrderList(int pageSize, int pageIndex, String searchVal, HttpSession session) throws  Exception{
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        //获取该用户所属的酒店id
        HotelVo hotelVo = hotelService.findHotel(userVo.getId());
        //分页参数
        PagingBean pagingBean = new PagingBean();
        pagingBean.setPageSize(pageSize);
        pagingBean.setCurrentPage(pageIndex);
        //赋值给pagequery对象
        PageQuery pageQuery = new PageQuery();
        pageQuery.setHotelId(hotelVo.getId());
        pageQuery.setCompanyId(userVo.getCompanyId());
        pageQuery.setSearchVal(searchVal);
        pageQuery.setPageSize(pagingBean.getPageSize());
        pageQuery.setPageNo(pagingBean.getStartIndex());
        pagingBean.setTotal(customerOrderService.count(pageQuery));
        pagingBean.setrows(customerOrderService.listPage(pageQuery));
        return pagingBean;
    }
    @RequestMapping("/customerOrderAddSave")
    @ResponseBody
    public Message addSavecustomerOrder(CustomerOrderVo customerOrder,HttpSession session) throws  Exception {
        try{
            UserVo userVo = (UserVo) session.getAttribute("userVo");
            UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
            //如果是店长新增现金流水账目
            if(userRoleVo.getRoleVo().getTitle().equals("店长")){
                HotelVo hotelVo = hotelService.findHotel(userVo.getId());
                customerOrder.setHotelId(hotelVo.getId());
                customerOrder.setShopManagerId(userVo.getId());
            }else if(userRoleVo.getRoleVo().getTitle().equals("录入员")){
                EmployeeVo employeeVo = employeeService.getHotelId(userVo.getId());
                customerOrder.setHotelId(employeeVo.getHotelId());
                customerOrder.setShopManagerId(employeeVo.getUserId());
            }
            customerOrder.setRemark("暂无批注");
            customerOrder.setIsCheck((byte) 0);
            customerOrder.setCheckRemark("未审核");
            customerOrder.setIsActive(ActiveStatusEnum.ACTIVE.getValue().byteValue());
            customerOrder.setCompanyId(userVo.getCompanyId());
            customerOrderService.save(customerOrder);
            return  Message.success("新增成功!");
        }catch (Exception E){
            return Message.fail("新增失败!");
        }

    }
    //获取房间类型列表，返回select2对象的数据
    @RequestMapping("getOthersHotel")
    @ResponseBody
    public List<Select2Vo> getOthersHotel(HttpSession session){
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
        PageQuery pageQuery = new PageQuery();
        pageQuery.setCompanyId(userVo.getCompanyId());
        if(userRoleVo.getRoleVo().getTitle().equals("店长")){
            HotelVo hotelVo = hotelService.findHotel(userVo.getId());
            pageQuery.setHotelId(hotelVo.getId());
        }else if(userRoleVo.getRoleVo().getTitle().equals("录入员")){
            EmployeeVo employeeVo = employeeService.getHotelId(userVo.getId());
            pageQuery.setHotelId(employeeVo.getHotelId());
        }
        List<Select2Vo> typeList=customerOrderService.getOthersHotel(pageQuery);
        return  typeList;
    }
    //获取房间类型列表，返回select2对象的数据
    @RequestMapping("getTypeList")
    @ResponseBody
    public List<Select2Vo> getTypeList(HttpSession session){
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        List<Select2Vo> typeList=houseService.houseTypeList(userVo.getCompanyId());
        return  typeList;
    }
    @RequestMapping("getHouse/{id}")
    @ResponseBody
    public List<Select2Vo> getHouse(HttpSession session,@PathVariable("id") Long id){
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
        PageQuery pageQuery = new PageQuery();
        pageQuery.setCompanyId(userVo.getCompanyId());
        if(userRoleVo.getRoleVo().getTitle().equals("店长")){
            HotelVo hotelVo = hotelService.findHotel(userVo.getId());
            pageQuery.setHotelId(hotelVo.getId());
        }else if(userRoleVo.getRoleVo().getTitle().equals("录入员")){
            EmployeeVo employeeVo = employeeService.getHotelId(userVo.getId());
            pageQuery.setHotelId(employeeVo.getHotelId());
        }
        List<Select2Vo> typeList=customerOrderService.getHouse(pageQuery,id);
        return  typeList;
    }
    @RequestMapping("/getSubject")
    @ResponseBody
    public List<Select2Vo> getSubject(HttpSession session) throws  Exception {
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        List<Select2Vo> subjectList = customerOrderService.getSubject(userVo.getCompanyId());
        return  subjectList;
    }
    @RequestMapping("/getWeb")
    @ResponseBody
    public List<Select2Vo> getWeb(HttpSession session) throws  Exception {
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        List<Select2Vo> webList = customerOrderService.getWeb(userVo.getCompanyId());
        return  webList;
    }
    @RequestMapping("/findCustomerOrder/{id}")
    @ResponseBody
    public CustomerOrderVo findcustomerOrder(@PathVariable("id") long id){
        CustomerOrderVo customerOrder = customerOrderService.getById(id);
        return customerOrder;
    }
    @RequestMapping("/customerOrderShenHe")
    @ResponseBody
    public Message customerOrderShenHe(CustomerOrderVo customerOrder,HttpSession session) throws  Exception{
        try{
            UserVo user = (UserVo) session.getAttribute("userVo");
            UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
            if(userRoleVo.equals("录入员")){
                return Message.fail("审核失败，你无权限!");
            }else{
                customerOrder.setHander(user.getId());
                customerOrderService.updateCashStatus(customerOrder);
                return  Message.success("审核成功!");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("审核失败!");
        }
    }
    @RequestMapping("/checkerCustomerOrder")
    @ResponseBody
    public Message checkerCustomerOrder(CustomerOrderVo customerOrder,HttpSession session,String manyId) throws  Exception{
        try{
            List<CustomerOrderVo> customerOrderVoList = new ArrayList<>();
            UserVo user = (UserVo) session.getAttribute("userVo");
            UserRoleVo userRoleVo = (UserRoleVo) session.getAttribute("userRole");
            String accounts[] =  manyId.split(",");
            if(userRoleVo.equals("录入员")){
                return Message.fail("审核失败，你无权限!");
            }else{
                for (String str:accounts) {
                    CustomerOrderVo customerOrderVo = new CustomerOrderVo();
                    customerOrderVo.setCheckRemark(customerOrder.getCheckRemark());
                    customerOrderVo.setIsCheck(customerOrder.getIsCheck());
                    customerOrderVo.setHander(user.getId());
                    if(!str.equals("")){
                        customerOrderVo.setId(Long.parseLong(str));
                    }
                    customerOrderVoList.add(customerOrderVo);
                }
                customerOrderService.checkerManyCashAccount(customerOrderVoList);
                return  Message.success("批量审核成功!");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("批量审核失败!");
        }
    }
    @RequestMapping("/customerOrderUpdateSave")
    @ResponseBody
    public Message updatecustomerOrder(CustomerOrderVo customerOrder) throws  Exception{
        try{
            customerOrderService.update(customerOrder);
            return  Message.success("修改成功!");
        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("修改失败!");
        }
    }
    @RequestMapping("/customerOrderUpdateRemark")
    @ResponseBody
    public Message customerOrderUpdateRemark(CustomerOrderVo customerOrder) throws  Exception{
        try{
            customerOrderService.updateRemark(customerOrder);
            return  Message.success("批注成功!");
        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("批注失败!");
        }
    }
    @RequestMapping("/deleteManyCustomerOrder")
    @ResponseBody
    public Message deleteManycustomerOrder(@Param("manyId") String manyId) throws  Exception{
        try{
            String str[] = manyId.split(",");
            for (String s: str) {
                customerOrderService.removeById(Long.parseLong(s));
            }
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return  Message.fail("删除失败!");
        }
    }
    @RequestMapping("/deleteCustomerOrder/{id}")
    @ResponseBody
    public Message deletecustomerOrder(@PathVariable("id") long id) throws  Exception{
        try{
            customerOrderService.removeById(id);
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("删除失败!");
        }
    }
    @RequestMapping("/customerOrderPage")
    public String table() throws  Exception{
        return "moneyItems/customerOrderList";
    }
    @RequestMapping("updateStatus/{id}/{status}")
    @ResponseBody
    public Message updateStatus(@PathVariable("id") long id,@PathVariable("status") int status) throws  Exception{
        try{
            customerOrderService.updateStatus(new StatusQuery(id,status));
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