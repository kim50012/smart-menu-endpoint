package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.domain.DatabaseHealthCheckRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDatabaseHealthCheckRepoImpl extends BaseRepository implements DatabaseHealthCheckRepository {
    @Override
    public String getData() {
        final List<String> data = this.getJdbcTemplate().query(
                "select check_data from base_health_check limit 1",
                new Object[]{},
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString("check_data");
                    }
                });
        return data.isEmpty() ? null : data.get(0);
    }
}