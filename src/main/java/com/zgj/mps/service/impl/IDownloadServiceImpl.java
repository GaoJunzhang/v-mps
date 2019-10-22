package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DownStatusBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.dao.mapper.DownloadMapper;
import com.zgj.mps.model.Download;
import com.zgj.mps.service.IDownloadService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 下载资源接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDownloadServiceImpl extends ServiceImpl<DownloadMapper, Download> implements IDownloadService {

    @Autowired
    private DownloadMapper downloadMapper;

    public Page<DownloadBean> pageDownload(DownloadBean downloadBean, PageVo pageVo, SearchVo searchVo) {
        Page<DownloadBean> pageDownload = new Page<DownloadBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return downloadMapper.pageDownload(pageDownload,downloadBean); //自定义方法，多表
    }

    public List<DownStatusBean> downProcessByDevictType(String type){
        return downloadMapper.downProcessByDevictType(type);
    }
}