package tn.esprit.stock_service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockRestAPI {

    private final StockService stockService;
    private final StockPdfService pdfService;

    public StockRestAPI(StockService stockService,StockPdfService pdfService) {
        this.stockService = stockService;
        this.pdfService = pdfService;

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

    //http://localhost:8085/stocks/filter?status=AVAILABLE
    @GetMapping("/filter")
    public ResponseEntity<List<Stock>> getStockByStatus(@RequestParam StockStatus status) {
        return ResponseEntity.ok(stockService.getStockByStatus(status));
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


}
