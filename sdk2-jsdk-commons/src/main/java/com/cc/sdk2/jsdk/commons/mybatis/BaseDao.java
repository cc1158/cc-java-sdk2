package com.cc.sdk2.jsdk.commons.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author sen.hu@zhaopin.com
 * @date 16:08 2021/2/3
 */
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
