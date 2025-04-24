export enum Category {
    ELECTRONICS = "ELECTRONICS",
    CLOTHING = "CLOTHING",
    HOME_APPLIANCES = "HOME_APPLIANCES",
    BEAUTY = "BEAUTY",
    TOYS = "TOYS",
    FOOD = "FOOD",
    FURNITURE = "FURNITURE"
  }
  
  export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    stockQuantity: number;
    category: Category;
    supplierEmail: string;
    dateAdded: Date;
  }
  
  export interface ProductStatsDTO {
    total: number;
    byCategory: { [key in Category]: number };
  }