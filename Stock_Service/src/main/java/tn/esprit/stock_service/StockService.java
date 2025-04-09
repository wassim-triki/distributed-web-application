package tn.esprit.stock_service;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final JavaMailSender mailSender;
    private final StockStatisticsPdfService stockStatisticsPdfService;

    public StockService(StockRepository stockRepository,JavaMailSender mailSender,StockStatisticsPdfService stockStatisticsPdfService) {
        this.stockRepository = stockRepository;
        this.mailSender = mailSender;
        this.stockStatisticsPdfService = stockStatisticsPdfService;


    }
    public Stock addStock(Stock stock) {
        Stock savedStock = stockRepository.save(stock);
        checkStockLevel(savedStock); // Vérification après ajout
        return savedStock;
    }

    public Stock updateStock(int id, Stock stock) {
        if (stockRepository.existsById(id)) {
            stock.setId(id);
            Stock updatedStock = stockRepository.save(stock);
            checkStockLevel(updatedStock); // Vérification après mise à jour
            return updatedStock;
        }
        return null;
    }

    private void checkStockLevel(Stock stock) {
        if (stock.getQuantity() <= stock.getMinQuantity()) {
            sendRestockNotification(stock);
        }
    }

    private void sendRestockNotification(Stock stock) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo("khouloudbelhadj11@gmail.com");
            helper.setSubject("Stock Low: Product " + stock.getProductId() + " needs restocking");
            helper.setText(
                    "Warning: The stock for product with ID " + stock.getProductId() + " has dropped to or below the minimum quantity ("
                            + stock.getMinQuantity() + "). Currently, there are only " + stock.getQuantity() + " units remaining in stock. "
                            + "Please restock the product as soon as possible to avoid stockouts."
            );

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteStock(int id) {
        stockRepository.deleteById(id);
    }

    public void deleteAllStocks() {
        stockRepository.deleteAll();
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

    public List<Stock> filterStock(StockStatus status, String location, Integer Quantity) {
        return stockRepository.findAll().stream()
                .filter(stock -> status == null || stock.getStatus() == status)
                .filter(stock -> location == null || stock.getLocation().equalsIgnoreCase(location))
                .filter(stock -> Quantity == null || stock.getQuantity() > 10)
                .toList();
    }

    public byte[] generateStatisticsPdf() {
        StockStatisticsDTO stats = getStatistics();
        return stockStatisticsPdfService.generateAdvancedStockStatisticsPdf(stats);
    }

}