package tn.esprit.stock_service;

public class StockStatisticsDTO {

    private long totalStockItems;
    private long totalQuantity;
    private long availableCount;
    private long outOfStockCount;
    private long reservedCount;

    public StockStatisticsDTO() {}

    public StockStatisticsDTO(long totalStockItems, long totalQuantity, long availableCount, long outOfStockCount, long reservedCount) {
        this.totalStockItems = totalStockItems;
        this.totalQuantity = totalQuantity;
        this.availableCount = availableCount;
        this.outOfStockCount = outOfStockCount;
        this.reservedCount = reservedCount;
    }

    // Getters & Setters
    public long getTotalStockItems() {
        return totalStockItems;
    }

    public void setTotalStockItems(long totalStockItems) {
        this.totalStockItems = totalStockItems;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(long availableCount) {
        this.availableCount = availableCount;
    }

    public long getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(long outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }

    public long getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(long reservedCount) {
        this.reservedCount = reservedCount;
    }
}
