package com.zgj.mps.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zgj.mps.dao.RoleRepository;
import com.zgj.mps.model.Role;
import com.zgj.mps.service.RoleService;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    public Page<Role> findByCondition(Role role, SearchVo searchVo, Pageable pageable) {

        return roleRepository.findAll(new Specification<Role>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<Date> createTimeField = root.get("createTime");
                List<Predicate> list = new ArrayList<Predicate>();
                if (StrUtil.isNotBlank(searchVo.getStartDate()) && StrUtil.isNotBlank(searchVo.getEndDate())) {
                    Date start = DateUtil.parse(searchVo.getStartDate());
                    Date end = DateUtil.parse(searchVo.getEndDate());
                    list.add(cb.between(createTimeField, start, DateUtil.endOfDay(end)));
                }
                if (StrUtil.isNotBlank(role.getName())) {
                    list.add(cb.like(root.get("name"), "%" + role.getName() + "%"));
                }
                list.add(cb.equal(root.get("isDelete"), 0));
                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    public List<Role> findAllByRole(String role) {
        return roleRepository.findByRoleAndIsDelete(role, (short) 0);
    }

    public void updateIsDelete(Long id) {
        roleRepository.updateIsDelete(id);
    }

    public List<Role> findAllByIsDelete(Short isDelete) {
        return roleRepository.findAllByIsDelete(isDelete);
    }
}