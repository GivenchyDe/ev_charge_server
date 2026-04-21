package org.example.ev_charge_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.ev_charge_server.entity.Admin;

//管理员模块的映射器（实现实体类和数据表的映射关系）
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
