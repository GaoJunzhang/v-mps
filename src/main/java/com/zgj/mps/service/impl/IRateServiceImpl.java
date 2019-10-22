package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.RateBean;
import com.zgj.mps.bean.RateStasticBean;
import com.zgj.mps.dao.mapper.RateMapper;
import com.zgj.mps.model.Rate;
import com.zgj.mps.service.IRateService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资源评论接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IRateServiceImpl extends ServiceImpl<RateMapper, Rate> implements IRateService {

    @Autowired
    private RateMapper rateMapper;

    public Page<RateBean> pageRate(RateBean rateBean, PageVo pageVo, SearchVo searchVo) {
        Page<RateBean> pageRate = new Page<RateBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return rateMapper.pageRate(pageRate,rateBean); //自定义方法，多表
    }

    public Page<RateStasticBean> pageRateStastic(RateBean rateBean, PageVo pageVo, SearchVo searchVo) {
        Page<RateStasticBean> pageRate = new Page<RateStasticBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return rateMapper.pageRateStastic(pageRate,rateBean); //自定义方法，多表
    }
}