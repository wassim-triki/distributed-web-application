package tn.esprit.stock_service;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockRestAPI {

    private final StockService stockService;
    private final StockPdfService pdfService;
    private final StockExcelService excelService;

    public StockRestAPI(StockService stockService,
                        StockPdfService pdfService,
                        StockExcelService excelService) {
        this.stockService = stockService;
        this.pdfService = pdfService;
        this.excelService = excelService;

    }

    @PostMapping("/add")
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.addStock(stock);
        return ResponseEntity.ok(savedStock);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable int id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable int id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/all")
    public void deleteAllStocks() {
        stockService.deleteAllStocks();
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable int id) {
        Stock stock = stockService.getStockById(id);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //////FA

//http://localhost:8093/stocks/filter?status=RESERVED&location=Bizerte&quantityGreaterThan=15
    @GetMapping("/filter")
    public ResponseEntity<List<Stock>> filterStocks(
            @RequestParam(required = false) StockStatus status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer quantityGreaterThan
    ) {
        List<Stock> filtered = stockService.filterStock(status, location, quantityGreaterThan != null ? quantityGreaterThan : 10);
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/statistics")
    public ResponseEntity<StockStatisticsDTO> getStatistics() {
        return ResponseEntity.ok(stockService.getStatistics());
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadStockPdf() {
        List<Stock> stocks = stockService.getAllStock();
        byte[] pdfBytes = pdfService.generateStockPdf(stocks);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=stock-report.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportExcel() {
        List<Stock> stockList = stockService.getAllStock();
        byte[] excelData = excelService.generateStockExcel(stockList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "stock_report.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }

    @GetMapping("/statistics/pdf")
    public ResponseEntity<byte[]> downloadStatisticsPdf() {
        byte[] pdfBytes = stockService.generateStatisticsPdf();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=advanced-stock-statistics.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
