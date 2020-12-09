package com.cockpit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cockpit.model.SafeCodeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface SafeCodeDao extends BaseMapper<SafeCodeModel> {
}
