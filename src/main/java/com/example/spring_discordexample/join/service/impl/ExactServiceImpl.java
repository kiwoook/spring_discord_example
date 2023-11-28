package com.example.spring_discordexample.join.service.impl;

import com.example.spring_discordexample.join.service.ExactService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.sql.*;

@Service
public class ExactServiceImpl implements ExactService {

    private static final String QUERY = "SELECT * FROM join_info";
    private final ThreadLocal<Workbook> workbookThreadLocal = ThreadLocal.withInitial(XSSFWorkbook::new);
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    private final ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });

    @Override
    public void exact() {
        try (Workbook workbook = workbookThreadLocal.get();
             Connection connection = connectionThreadLocal.get();
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
             FileOutputStream fileOutput = new FileOutputStream("data.xlsx")
        ) {


            // TODO DB TO XLSX 파일 구현하기.

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
            throw new RuntimeException(e);
        }

        connectionThreadLocal.remove();
        workbookThreadLocal.remove();
    }
}
