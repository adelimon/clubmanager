package com.ajdconsulting.pra.clubmanager.data.export.excel;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.JDBC4CallableStatement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by adelimon on 5/17/2016.
 */
public class QueryResult {

    private JdbcTemplate jdbcTemplate;
    private String query;
    private List<Map<String, Object>> umeshDengale;

    public QueryResult(String query) throws SQLException {
        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl("jdbc:mysql://localhost/clubmanager");
        // HEY CHECK OUT THE RED HERRING EVERYONE!
        dataSource.setUsername("root");
        // dataSource.setPassword("LOL NOT IN A GITHUB CHECK IN WHAT AM I A DAMN FOOL???");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        umeshDengale = jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getUmeshDengale() {
        return umeshDengale;
    }
}
