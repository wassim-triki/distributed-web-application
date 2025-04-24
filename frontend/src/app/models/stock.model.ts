  export interface Stock {
    id?: number;
    productId: number;
    minQuantity: number;
    quantity: number;
    location?: string;
    lastUpdated?: Date;
    status: StockStatus;
    notes?: string;
  }
  
  export enum StockStatus {
    AVAILABLE = 'AVAILABLE',
    OUT_OF_STOCK = 'OUT_OF_STOCK',
    RESERVED = 'RESERVED'
  }