package tn.esprit.stock_service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

@Service
public class StockStatisticsPdfService {

    public byte[] generateAdvancedStockStatisticsPdf(StockStatisticsDTO stats) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(0, 102, 204));
            Paragraph title = new Paragraph("📊 Stock Statistics Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Affichage des totaux sous forme de tableau
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Paragraph totalsTitle = new Paragraph("📦 Totals Summary", sectionFont);
            totalsTitle.setSpacingAfter(10f);
            document.add(totalsTitle);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            table.setSpacingAfter(20f);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            PdfPCell cell1 = new PdfPCell(new Phrase("Total Stock Items", labelFont));
            cell1.setBackgroundColor(new Color(0, 102, 204));
            cell1.setPadding(8);
            PdfPCell value1 = new PdfPCell(new Phrase(String.valueOf(stats.getTotalStockItems()), valueFont));
            value1.setPadding(8);

            PdfPCell cell2 = new PdfPCell(new Phrase("Total Quantity", labelFont));
            cell2.setBackgroundColor(new Color(0, 102, 204));
            cell2.setPadding(8);
            PdfPCell value2 = new PdfPCell(new Phrase(String.valueOf(stats.getTotalQuantity()), valueFont));
            value2.setPadding(8);

            table.addCell(cell1);
            table.addCell(value1);
            table.addCell(cell2);
            table.addCell(value2);

            document.add(table);

            // Graphique circulaire pour les statuts
            Paragraph chartTitle = new Paragraph("📈 Stock Status Distribution", sectionFont);
            chartTitle.setSpacingBefore(15f);
            chartTitle.setSpacingAfter(10f);
            document.add(chartTitle);

            Image pieChartImage = Image.getInstance(generatePieChartAsBytes(stats));
            pieChartImage.scaleToFit(400, 300);
            pieChartImage.setAlignment(Image.ALIGN_CENTER);
            document.add(pieChartImage);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private byte[] generatePieChartAsBytes(StockStatisticsDTO stats) throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Available", stats.getAvailableCount());
        dataset.setValue("Out of Stock", stats.getOutOfStockCount());
        dataset.setValue("Reserved", stats.getReservedCount());

        JFreeChart pieChart = ChartFactory.createPieChart(
                "", dataset, false, false, false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Available", new Color(0, 204, 102));
        plot.setSectionPaint("Out of Stock", new Color(255, 51, 51));
        plot.setSectionPaint("Reserved", new Color(255, 153, 51));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        // Affiche les valeurs sur chaque section (nombre uniquement)
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}", new DecimalFormat("0"), new DecimalFormat("0%")));
        plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        plot.setLabelPaint(Color.BLACK);

        BufferedImage chartImage = pieChart.createBufferedImage(400, 300);
        return EncoderUtil.encode(chartImage, "png");
    }
}
