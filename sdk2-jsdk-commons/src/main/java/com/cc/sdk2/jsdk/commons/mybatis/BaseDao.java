package com.cc.sdk2.jsdk.commons.mybatis;

import java.util.List;
import java.util.Map;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public abstract class BaseDao<T, P> {

    protected BaseMapper<T, P> baseMapper;

    protected abstract void setBaseMapper();

    public T find(P id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    public T find(Map<String, Object> clause) {
        return baseMapper.selectOne(clause);
    }

    public Integer add(T obj) {
        return baseMapper.insertSelective(obj);
    }

    public Integer update(T obj) {
        return baseMapper.updateByPrimaryKeySelective(obj);
    }

    public Integer delete(P id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    public List<T> findList(Map<String, Object> map) {
        return baseMapper.selectList(map);
    }

    public Integer countByClause(Map<String, Object> map) {
        return baseMapper.selectCount(map);
    }

    public List<T> findListByPage(Map<String, Object> clause, int start, int size) {
        return baseMapper.selectListByPage(clause, start, size);
    }


}
