package top.zywork.service;

import top.zywork.query.PageQuery;
import top.zywork.vo.CashAccountsVo;
import top.zywork.vo.CooperationAccountsVo;
import top.zywork.vo.Select2Vo;

import java.util.List;

/**
 * Created by chenfeilong on 2017/12/3.
 */
public interface CooperationAccountsService extends BaseService<CooperationAccountsVo>{
    void updateCashStatus(CooperationAccountsVo cooperationAccountsVo);
    List<Select2Vo> getSubject(Long companyId);
    List<Select2Vo> getCooperationCompany(PageQuery pageQuery);
    void updateRemark(CooperationAccountsVo cooperationAccountsVo);
    void checkerManyCashAccount(List<CooperationAccountsVo> cooperationAccountsVos);
}
