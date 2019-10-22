package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgj.mps.bean.UserDResourceBean;
import com.zgj.mps.model.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 资源数据处理层
 * @author GaoJunZhang
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    List<UserDResourceBean> reourceListByUid(@Param("uid") String uid);
}