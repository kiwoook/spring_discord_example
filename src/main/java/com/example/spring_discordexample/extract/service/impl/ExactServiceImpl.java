package com.example.spring_discordexample.extract.service.impl;

import com.example.spring_discordexample.extract.service.ExactService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.sql.*;

@Service
@Slf4j
public class ExactServiceImpl implements ExactService {

    private static final String QUERY = "SELECT * FROM join_info";
    private final ThreadLocal<Workbook> workbookThreadLocal;
    private final ThreadLocal<Connection> connectionThreadLocal;

    @Autowired
    public ExactServiceImpl(@Value(value = "${sql.url}") String jdbcUrl,
                            @Value("${sql.username}") String username,
                            @Value("${sql.password}") String password) {
        this.workbookThreadLocal = ThreadLocal.withInitial(XSSFWorkbook::new);
        this.connectionThreadLocal = ThreadLocal.withInitial(() -> {

            log.info("jdbc url = {}, username = {}, password = {}", jdbcUrl, username, password);

            try {
                return DriverManager.getConnection(jdbcUrl, username, password);
            } catch (SQLException e) {
                throw new UncategorizedSQLException("Failed to connect to DB", null, e);
            }
        });
    }

    @Override
    public void exact() throws SQLException {
        try (Workbook workbook = workbookThreadLocal.get();
             Connection connection = connectionThreadLocal.get();
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
             FileOutputStream fileOutput = new FileOutputStream("data.xlsx")
        ) {

            ResultSet resultSet = preparedStatement.executeQuery();
            Sheet sheet = workbook.createSheet("Data");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 엑셀 헤더 생성
            Row headRow = sheet.createRow(0);
            for (int i = 0; i < columnCount; i++) {
                headRow.createCell(i).setCellValue(metaData.getColumnName(i + 1));
            }

            int rowNum = 1;

            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < columnCount; i++) {
                    row.createCell(i).setCellValue(resultSet.getString(i + 1));
                }
            }
            workbook.write(fileOutput);


        } catch (Exception e) {
            throw new SQLException(e);
        }

        connectionThreadLocal.remove();
        workbookThreadLocal.remove();
    }
}
