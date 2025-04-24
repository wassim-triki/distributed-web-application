package tn.esprit.microservice.product_service;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM");

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add a new product
    public Product addProduct(Product product) {
        if (product == null) {
            logger.error("Attempted to add a null product.");
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getDateAdded() == null) {
            product.setDateAdded(new Date());
        }

        logger.info("Adding product: {}", product.getName());
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(int id, Product updatedProduct) {
        if (updatedProduct == null) {
            logger.error("Updated product is null for ID: {}", id);
            throw new IllegalArgumentException("Updated product cannot be null");
        }

        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
                    existingProduct.setCategory(updatedProduct.getCategory());
                    existingProduct.setSupplierEmail(updatedProduct.getSupplierEmail());
                    logger.info("Updating product with ID: {}", id);
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> {
                    logger.warn("Product not found with ID: {}", id);
                    return new NoSuchElementException("Product not found with ID: " + id);
                });
    }

    // Delete product by ID
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existing product ID: {}", id);
            throw new NoSuchElementException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        logger.info("Deleted product with ID: {}", id);
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(int id) {
        logger.info("Fetching product by ID: {}", id);
        return productRepository.findById(id);
    }

    // Get product by name
    public Optional<Product> getProductByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Product name is null or empty");
            return Optional.empty();
        }
        logger.info("Fetching product by name: {}", name);
        return Optional.ofNullable(productRepository.findByName(name));
    }

    // Filter products by category
    public List<Product> getProductsByCategory(Category category) {
        if (category == null) {
            logger.warn("Category is null for filter");
            return Collections.emptyList();
        }
        logger.info("Fetching products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    // Get overall product statistics
    public ProductStatsDTO getProductStats() {
        List<Product> allProducts = productRepository.findAll();
        long total = allProducts.size();

        Map<Category, Long> byCategory = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        logger.info("Returning product statistics");
        return new ProductStatsDTO(total, byCategory);
    }

    // Get monthly product addition statistics
    public Map<String, Long> getMonthlyProductStats() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            logger.info("No products available for monthly stats");
            return new HashMap<>();
        }

        Map<String, Long> monthlyStats = allProducts.stream()
                .filter(p -> p.getDateAdded() != null)
                .collect(Collectors.groupingBy(
                        p -> SDF.format(p.getDateAdded()),
                        Collectors.counting()
                ));

        logger.info("Returning monthly product statistics");
        return monthlyStats;
    }

    // Get products by price range (with optional category)
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice, Category category) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            logger.error("Invalid price range: {} - {}", minPrice, maxPrice);
            throw new IllegalArgumentException("Invalid price range");
        }

        logger.info("Fetching products by price range: {} - {} and category: {}", minPrice, maxPrice, category);

        if (category == null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
    }

    // Search products by keyword in description
    public List<Product> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("Keyword is null or empty");
            return Collections.emptyList();
        }

        logger.info("Searching products with keyword: {}", keyword);
        return productRepository.findByDescriptionContainingIgnoreCase(keyword);
    }private static final String QR_CODE_FOLDER = "F:/distributed-web-application/Product_Service/src/resources/QRCODES";

    public String generateQRCodeForProduct(Product product) {
        // Prepare QR code data: All product details
        String productData = "Product ID: " + product.getId() + "\n" +
                "Name: " + product.getName() + "\n" +
                "Description: " + product.getDescription() + "\n" +  // Add other relevant fields
                "Price: " + product.getPrice() + "\n" +
                "Category: " + product.getCategory();

        // Ensure the directory exists
        File directory = new File(QR_CODE_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Define the QR code file path
        String fileName = product.getName() + ".png"; // Use the product name as the file name
        File qrCodeFile = new File(QR_CODE_FOLDER, fileName);

        try {
            // Generate the QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(productData, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Convert BitMatrix to BufferedImage
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Save the image to file
            ImageIO.write(image, "PNG", qrCodeFile);

            // Return the relative URL to the frontend
            return "/qrcodes/" + fileName; // The URL that the frontend will access
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }


}
    // Internal DTO class for product statistics
    public static class ProductStatsDTO {
        private final long total;
        private final Map<Category, Long> byCategory;

        public ProductStatsDTO(long total, Map<Category, Long> byCategory) {
            this.total = total;
            this.byCategory = byCategory != null ? byCategory : new HashMap<>();
        }

        public long getTotal() {
            return total;
        }

        public Map<Category, Long> getByCategory() {
            return byCategory;
        }
    }
}
