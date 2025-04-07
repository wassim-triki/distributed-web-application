package tn.esprit.stock_service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(int id, Stock stock) {
        if (stockRepository.existsById(id)) {
            stock.setId(id);
            return stockRepository.save(stock);
        }
        return null;
    }

    public void deleteStock(int id) {
        stockRepository.deleteById(id);
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockById(int id) {
        return stockRepository.findById(id).orElse(null);
    }

    public List<Stock> getStockByStatus(StockStatus status) {
        return stockRepository.findByStatus(status);
    }

    public List<Stock> getStockByProductId(int productId) {
        return stockRepository.findByProductId(productId);
    }


}
