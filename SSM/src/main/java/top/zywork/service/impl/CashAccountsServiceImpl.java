package top.zywork.service.impl;

import org.springframework.stereotype.Service;
import top.zywork.dao.CashAccountsDAO;
import top.zywork.query.PageQuery;
import top.zywork.query.StatusQuery;
import top.zywork.service.CashAccountsService;
import top.zywork.vo.CashAccountsVo;
import top.zywork.vo.Select2Vo;
import top.zywork.vo.SumCashVo;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by chenfeilong on 2017/12/3.
 */
@Service
public class CashAccountsServiceImpl implements CashAccountsService{

    @Resource
    private CashAccountsDAO cashAccountsDAO;
    @Override
    public void save(CashAccountsVo cashAccountsVo) {
        cashAccountsDAO.save(cashAccountsVo);
    }

    @Override
    public void remove(CashAccountsVo cashAccountsVo) {
        cashAccountsDAO.remove(cashAccountsVo);
    }

    @Override
    public void removeById(Long id) {
        cashAccountsDAO.removeById(id);
    }

    @Override
    public void update(CashAccountsVo cashAccountsVo) {
        cashAccountsDAO.update(cashAccountsVo);
    }

    @Override
    public void updateStatus(StatusQuery statusQuery) {
        cashAccountsDAO.updateStatus(statusQuery);
    }

    @Override
    public CashAccountsVo getById(Long id) {
        return cashAccountsDAO.getById(id);
    }

    @Override
    public List<CashAccountsVo> listAll() {
        return cashAccountsDAO.listAll();
    }

    @Override
    public List<CashAccountsVo> listPage(PageQuery pageQuery) {
        return cashAccountsDAO.listPage(pageQuery);
    }

    @Override
    public long count(PageQuery pageQuery) {
        return cashAccountsDAO.count(pageQuery);
    }

    @Override
    public void updateCashStatus(CashAccountsVo cashAccountsVo) {
        cashAccountsDAO.updateCashStatus(cashAccountsVo);
    }

    @Override
    public List<Select2Vo> getSubject(Long companyId) {
        return cashAccountsDAO.getSubject(companyId);
    }

    @Override
    public void updateRemark(CashAccountsVo cashAccountsVo) {
        cashAccountsDAO.updateRemark(cashAccountsVo);
    }

    @Override
    public void checkerManyCashAccount(List<CashAccountsVo> cashAccountsVoList) {
        cashAccountsDAO.checkerManyCashAccount(cashAccountsVoList);
    }

    @Override
    public List<Select2Vo> getHotel(Long companyId) {
        return cashAccountsDAO.getHotel(companyId);
    }

    @Override
    public List<Select2Vo> getPayType(Long companyId) {
        return cashAccountsDAO.getPayType(companyId);
    }

    @Override
    public SumCashVo sumCash(Date dateVal, Long companyId) {
        return cashAccountsDAO.sumCash(dateVal, companyId);
    }

    @Override
    public List<CashAccountsVo> listPages(PageQuery pageQuery) {
        return cashAccountsDAO.listPages(pageQuery);
    }

    @Override
    public long counts(PageQuery pageQuery) {
        return cashAccountsDAO.counts(pageQuery);
    }
}
