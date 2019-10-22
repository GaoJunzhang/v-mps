package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownStatusBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.model.Download;
import com.zgj.mps.service.IDownloadService;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.SearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "下载资源管理接口")
@RequestMapping("/download")
@Transactional
public class DownloadController {

    @Autowired
    private IDownloadService iDownloadService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Download> get(@PathVariable String id) {

        Download download = iDownloadService.getById(id);
        return new ResultUtil<Download>().setData(download);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Download>> getAll() {

        List<Download> list = iDownloadService.list();
        return new ResultUtil<List<Download>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                                                @ModelAttribute SearchVo searchVo,
                                                @ModelAttribute DownloadBean downloadBean) {

        Page<DownloadBean> downloadBeanPage = iDownloadService.pageDownload(downloadBean, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", downloadBeanPage.getRecords());
        map.put("pageNo", downloadBeanPage.getPages());
        map.put("totalCount", downloadBeanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Download> saveOrUpdate(@ModelAttribute Download download) {

        if (download.getCreateTime() == null) {
            download.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (download.getStatus() == 1){
            download.setFinishTime(new Timestamp(System.currentTimeMillis()));
        }
        if (iDownloadService.saveOrUpdate(download)) {
            return new ResultUtil<Download>().setSuccessMsg("成功");
        }
        return new ResultUtil<Download>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iDownloadService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/downProcessByDevictType", method = RequestMethod.GET)
    @ApiOperation(value = "查询下载数据")
    public Result<List<DownStatusBean>> downProcessByDevictType(@RequestParam(value = "type",required = true) String type){
        List<DownStatusBean> list = iDownloadService.downProcessByDevictType(type);
        return new ResultUtil<List<DownStatusBean>>().setData(list);
    }
}
