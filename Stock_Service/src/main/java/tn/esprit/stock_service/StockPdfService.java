package tn.esprit.stock_service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class StockPdfService {

    public byte[] generateStockPdf(List<Stock> stockList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.DARK_GRAY);
            Paragraph title = new Paragraph("📦 Stock Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1.4f, 1.5f, 1.5f, 1.6f, 2.4f, 3.5f}); // optional: column width

            // Header style
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
            Color headerBgColor = new Color(60, 120, 180); // bleu foncé
            addTableHeader(table, headerFont, headerBgColor);

            // Row style
            Font cellFont = new Font(Font.HELVETICA, 11);

            for (Stock stock : stockList) {
                addStyledCell(table, String.valueOf(stock.getProductId()), cellFont);
                addStyledCell(table, String.valueOf(stock.getQuantity()), cellFont);
                addStyledCell(table, String.valueOf(stock.getMinQuantity()), cellFont);
                addStyledCell(table, stock.getLocation(), cellFont);
                addStyledCell(table, stock.getStatus().toString(), cellFont);
                addStyledCell(table, stock.getNotes() != null ? stock.getNotes() : "-", cellFont);
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table, Font font, Color bgColor) {
        String[] headers = {"Product ID", "Quantity", "Min Quantity", "Location", "Status", "Notes"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8f);
            table.addCell(cell);
        }
    }

    private void addStyledCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6f);  // Reduced padding to fit text on one line
        table.addCell(cell);
    }
}
