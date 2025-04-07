package tn.esprit.stock_service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockRestAPI {

    private final StockService stockService;

    public StockRestAPI(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/add")
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.addStock(stock));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable int id, @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.updateStock(id, stock));
    }

    //
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
}
