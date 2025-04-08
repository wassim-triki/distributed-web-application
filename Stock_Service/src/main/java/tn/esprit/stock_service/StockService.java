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

    ///Fonc Avancées
    public StockStatisticsDTO getStatistics() {
        List<Stock> allStocks = stockRepository.findAll();

        long totalStockItems = allStocks.size();
        long totalQuantity = allStocks.stream().mapToLong(Stock::getQuantity).sum();

        long availableCount = allStocks.stream().filter(s -> s.getStatus() == StockStatus.AVAILABLE).count();
        long outOfStockCount = allStocks.stream().filter(s -> s.getStatus() == StockStatus.OUT_OF_STOCK).count();
        long reservedCount = allStocks.stream().filter(s -> s.getStatus() == StockStatus.RESERVED).count();

        return new StockStatisticsDTO(totalStockItems, totalQuantity, availableCount, outOfStockCount, reservedCount);
    }

}