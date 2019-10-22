package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.RateBean;
import com.zgj.mps.bean.RateStasticBean;
import com.zgj.mps.model.Rate;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

/**
 * 资源评论接口
 * @author GaoJunZhang
 */
public interface IRateService extends IService<Rate> {
    Page<RateBean> pageRate(RateBean rateBean, PageVo pageVo, SearchVo searchVo);

    public Page<RateStasticBean> pageRateStastic(RateBean rateBean, PageVo pageVo, SearchVo searchVo);
}