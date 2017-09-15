package com.ajdconsulting.pra.clubmanager.data.export.excel;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.JDBC4CallableStatement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public QueryResult(List<Map<String, Object>> result) {
        umeshDengale = result;
    }

    public QueryResult(String query) throws SQLException, IOException {
        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.

        Path currentRelativepath = Paths.get("");
        String pathString = currentRelativepath.toAbsolutePath().toString();
        List<String> configLines =
            Files.readAllLines(Paths.get(pathString+"/src/main/resources/config/application-dev.yml"));
        String url = null;
        String userName = null;
        String password = null;
        boolean datasourceFound = false;
        for (String line : configLines) {
            if (!datasourceFound) {
                datasourceFound = line.contains("datasource:");
            }
            if (datasourceFound) {
                if (url == null) {
                    url = crudeYamlParse(line, "url");
                }
                if (userName == null) {
                    userName = crudeYamlParse(line, "username");
                }
                if (password == null) {
                    password = crudeYamlParse(line, "password");
                }
            }
            boolean valuesFound = ((url != null) && (userName!=null) && (password!=null));
            if (valuesFound) break;
        }

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        umeshDengale = jdbcTemplate.queryForList(query);
    }

    private String crudeYamlParse(String line, String name) {
        String value = null;
        boolean lineHasName = line.contains(name);
        if (lineHasName) {
            line = StringUtils.trim(line);
            String[] splitValue = line.split(name+": ");
            if (splitValue.length == 2) {
                value = splitValue[1];
            } else {
                value = "";
            }
        }
        return value;
    }

    public List<Map<String, Object>> getUmeshDengale() {
        return umeshDengale;
    }


}
