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
import top.zywork.service.CooperationSubjectService;
import top.zywork.vo.CooperationSubjectVo;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenfeilong on 2017/10/19.
 */
@Controller
@RequestMapping("cooperationSubject")
public class CooperationSubjectController {
    @Resource
    private CooperationSubjectService cooperationSubjectService;
    @RequestMapping("cooperationSubjectList")
    @ResponseBody
    public PagingBean cooperationSubjectList(int pageSize, int pageIndex) throws  Exception{
        PagingBean pagingBean = new PagingBean();
        pagingBean.setTotal(cooperationSubjectService.count());
        pagingBean.setPageSize(pageSize);
        pagingBean.setCurrentPage(pageIndex);
        pagingBean.setrows(cooperationSubjectService.listPage(new PageQuery(pagingBean.getStartIndex(),pagingBean.getPageSize())));
        return pagingBean;
    }
    @RequestMapping("/cooperationSubjectAddSave")
    @ResponseBody
    public Message addSavecooperationSubject(CooperationSubjectVo web) throws  Exception {
        try{
            web.setIsActive(ActiveStatusEnum.ACTIVE.getValue().byteValue());
            cooperationSubjectService.save(web);
            return  Message.success("新增成功!");
        }catch (Exception E){
            return Message.fail("新增失败!");
        }

    }
    @RequestMapping("/findCooperationSubject/{id}")
    @ResponseBody
    public CooperationSubjectVo findcooperationSubject(@PathVariable("id") long id){
        CooperationSubjectVo cooperationSubject = cooperationSubjectService.getById(id);
        return cooperationSubject;
    }
    @RequestMapping("/cooperationSubjectUpdateSave")
    @ResponseBody
    public Message updatecooperationSubject(CooperationSubjectVo cooperationSubject) throws  Exception{
        try{
            cooperationSubjectService.update(cooperationSubject);
            return  Message.success("修改成功!");
        }catch (Exception e){
            return Message.fail("修改失败!");
        }
    }
    @RequestMapping("/deleteManyCooperationSubject")
    @ResponseBody
    public Message deleteManyWeb(@Param("manyId") String manyId) throws  Exception{
        try{
            String str[] = manyId.split(",");
            for (String s: str) {
                cooperationSubjectService.removeById(Long.parseLong(s));
            }
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return  Message.fail("删除失败!");
        }
    }
    @RequestMapping("/deleteCooperationSubject/{id}")
    @ResponseBody
    public Message deleteWeb(@PathVariable("id") long id) throws  Exception{
        try{
            cooperationSubjectService.removeById(id);
            return Message.success("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return Message.fail("删除失败!");
        }
    }
    @RequestMapping("/cooperationSubjectPage")
    public String table() throws  Exception{
        return "cooperationSubject/cooperationSubjectList";
    }
    @RequestMapping("updateStatus/{id}/{status}")
    @ResponseBody
    public Message updateStatus(@PathVariable("id") long id,@PathVariable("status") int status) throws  Exception{
        try{
            cooperationSubjectService.updateStatus(new StatusQuery(id,status));
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