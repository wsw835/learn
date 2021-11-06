package com.wensw.transaction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(String tableName, String[] fields, String[] values) {
        String sqlTemplate = " INSERT INTO  %s ( %s ) values ( %s ) ";
        String sql = String.format(sqlTemplate, tableName, StringUtils.join(fields, ","), StringUtils.join(values
                , ","));
        System.out.println(sql);
        int res = jdbcTemplate.update(sql);
        int a = 10 / 0;
        return res;
    }

}
