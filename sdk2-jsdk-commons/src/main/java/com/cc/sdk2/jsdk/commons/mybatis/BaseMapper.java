package com.cc.sdk2.jsdk.commons.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author sen.hu@zhaopin.com
 * @date 15:07 2021/2/3
 */
public interface BaseMapper<T, P> {

    /**
     * 根据主键id查询
     * @param id
     * @return  返回查询对象
     */
    T selectByPrimaryKey(P id);

    /**
     * 更具条件查找一个
     * @param clause
     * @return
     */
    T selectOne(Map<String, Object> clause);

    /**
     * 全字段插入
     * @param record
     * @return  返回影响行数
     */
    Integer insert(T record);

    /**
     * 插入不为null
     * @param record
     * @return  返回影响行数
     */
    Integer insertSelective(T record);

    /**
     * 插入并生成pkid
     * @param record
     * @return
     */
    Integer insertSelectiveAndGenPK(T record);

    /**
     * 更新不为null
     * @param record
     * @return  返回影响行数
     */
    Integer updateByPrimaryKeySelective(T record);

    /**
     * 全字段更新
     * @param record
     * @return
     */
    Integer updateByPrimaryKey(T record);

    /**
     * 更新含有特殊字段记录
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(T record);


    /**
     * 根据主键删除
     * @param id    主键
     * @return 返回影响行数
     */
    Integer deleteByPrimaryKey(P id);

    /**
     * 条件列表查询
     * @param map
     * @return  返回查询列表
     */
    List<T> selectList(Map<String, Object> map);

    /**
     * 条件统计
     * @param map
     * @return  返回统计数量
     */
    Integer selectCount(Map<String, Object> map);

    /**
     * 分页查询mapper
     * @param clause
     * @param pageNumber
     * @param size
     * @return
     */
    List<T> selectListByPage(@Param("clause") Map<String, Object> clause, @Param("start") Integer pageNumber, @Param("size") Integer size);



}
