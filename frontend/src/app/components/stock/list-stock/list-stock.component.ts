import { Component, OnInit } from '@angular/core';
import { StockService } from '../../../services/stock.service';  
import { Stock } from '../../../models/stock.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';  // Import FormsModule

@Component({
  selector: 'app-list-stock',
  standalone: true,
  imports: [CommonModule, FormsModule],  // Add necessary imports
  templateUrl: './list-stock.component.html',
  styleUrls: ['./list-stock.component.scss']
})
export class ListStockComponent implements OnInit {
  stocks: Stock[] = [];  // Array to hold fetched stocks
  filteredStocks: Stock[] = [];  // Filtered stocks based on search query
  searchQuery: string = '';  // Search query for filtering
  loading: boolean = false;  // Loading indicator

  page: number = 1;  // Current page for pagination
  pageSize: number = 6;  // Number of items per page

  constructor(private stockService: StockService,
              private router: Router) { }

  ngOnInit(): void {
    this.loadStocks();
  }

  loadStocks(): void {
    this.loading = true;
    this.stockService.getAll().subscribe({
      next: (data) => {
        this.stocks = data;
        this.filteredStocks = [...this.stocks];  // Initialize filtered stocks
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading stocks', err);
        this.loading = false;
      }
    });
  }

  // Method to filter stocks based on search query
  applySearch(): void {
    this.filteredStocks = this.stocks.filter(stock =>
      stock.productId.toString().includes(this.searchQuery) || // Recherche par productId
      stock.location?.toLowerCase().includes(this.searchQuery.toLowerCase()) || // Recherche par location
      stock.status.toLowerCase().includes(this.searchQuery.toLowerCase()) || // Recherche par status
      stock.notes?.toLowerCase().includes(this.searchQuery.toLowerCase()) || // Recherche par notes
      stock.quantity.toString().includes(this.searchQuery) || // Recherche par quantity
      stock.minQuantity.toString().includes(this.searchQuery) // Recherche par minQuantity
    );
    this.page = 1; // Réinitialisation à la page 1 après une recherche
  }
  

  // Navigate to stock editing page
  editStock(id: number): void {
    this.router.navigate(['/modify-stock', id]); 
  }

  // Delete a stock item
  deleteStock(id: number): void {
    if (confirm('Are you sure you want to delete this stock?')) {
      this.stockService.delete(id).subscribe({
        next: () => {
          // Re-load the list of stocks after successful deletion
          this.loadStocks();
          alert('Stock deleted successfully');
        },
        error: (err) => {
          console.error('Error deleting stock', err);
          alert('Error deleting stock');
        }
      });
    }
  }  

  // Navigate to add new stock page
  addStock(): void {
    this.router.navigate(['/add-stock']);
  }

  // View detailed stock information
  viewStock(id: number): void {
    this.router.navigate(['/view-stock', id]);
  }
}
