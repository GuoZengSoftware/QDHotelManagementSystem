package top.zywork.service.impl;

import org.springframework.stereotype.Service;
import top.zywork.dao.HouseOthersItemDAO;
import top.zywork.query.PageQuery;
import top.zywork.query.StatusQuery;
import top.zywork.service.HouseOthersItemService;
import top.zywork.vo.HouseOthersItemVo;
import top.zywork.vo.Select2Vo;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chenfeilong on 2017/12/12.
 */
@Service
public class HouseOthersItemServiceImpl implements HouseOthersItemService {
    @Resource
    private HouseOthersItemDAO houseOthersItemDAO;
    @Override
    public void save(HouseOthersItemVo houseOthersItemVo) {
        houseOthersItemDAO.save(houseOthersItemVo);
    }

    @Override
    public void remove(HouseOthersItemVo houseOthersItemVo) {
        houseOthersItemDAO.remove(houseOthersItemVo);
    }

    @Override
    public void removeById(Long id) {
        houseOthersItemDAO.removeById(id);
    }

    @Override
    public void update(HouseOthersItemVo houseOthersItemVo) {
        houseOthersItemDAO.update(houseOthersItemVo);
    }

    @Override
    public void updateStatus(StatusQuery statusQuery) {
        houseOthersItemDAO.updateStatus(statusQuery);
    }

    @Override
    public HouseOthersItemVo getById(Long id) {
        return houseOthersItemDAO.getById(id);
    }

    @Override
    public List<HouseOthersItemVo> listAll() {
        return houseOthersItemDAO.listAll();
    }

    @Override
    public List<HouseOthersItemVo> listPage(PageQuery pageQuery) {
        return houseOthersItemDAO.listPage(pageQuery);
    }

    @Override
    public long count(PageQuery pageQuery) {
        return houseOthersItemDAO.count(pageQuery);
    }

    @Override
    public List<Select2Vo> subjectList(Long companyId) {
        return houseOthersItemDAO.subjectList(companyId);
    }

    @Override
    public List<HouseOthersItemVo> listPages(PageQuery pageQuery) {
        return houseOthersItemDAO.listPages(pageQuery);
    }

    @Override
    public Long counts(PageQuery pageQuery) {
        return houseOthersItemDAO.counts(pageQuery);
    }
}
