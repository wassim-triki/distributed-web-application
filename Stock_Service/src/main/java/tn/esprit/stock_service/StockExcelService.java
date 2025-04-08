package tn.esprit.stock_service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class StockExcelService {

    public byte[] generateStockExcel(List<Stock> stockList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Report");

            // Styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Product ID", "Quantity", "Min Quantity", "Location", "Status", "Notes"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowIdx = 1;
            for (Stock stock : stockList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stock.getProductId());
                row.createCell(1).setCellValue(stock.getQuantity());
                row.createCell(2).setCellValue(stock.getMinQuantity());
                row.createCell(3).setCellValue(stock.getLocation());
                row.createCell(4).setCellValue(stock.getStatus().toString());
                row.createCell(5).setCellValue(stock.getNotes() != null ? stock.getNotes() : "-");

                for (int i = 0; i < 6; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
