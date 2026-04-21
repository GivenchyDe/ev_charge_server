package org.example.ev_charge_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.ev_charge_server.entity.Admin;
import org.example.ev_charge_server.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private AdminMapper adminMapper;

    //http://127.0.0.1:8080/admin/list
    @GetMapping("/admin/list")
    public List<Admin> list() {
//        // 1. 创建一个空的 QueryWrapper（查询所有数据）
//        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
//        // 2. 调用 selectList 方法
//        return adminMapper.selectList(queryWrapper);
        return adminMapper.selectList(null);
    }
}
