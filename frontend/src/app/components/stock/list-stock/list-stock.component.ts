import { Component, OnInit } from '@angular/core';
import { StockService } from '../../../services/stock.service';  
import { Stock } from '../../../models/stock.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';  // Import FormsModule
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';
@Component({
  selector: 'app-list-stock',
  standalone: true,
  imports: [CommonModule, FormsModule,NgChartsModule],  // Add necessary imports
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
        this.filteredStocks = [...this.stocks];
        this.generateStatistics();
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

  downloadPdf() {
    this.stockService.downloadStockPdf().subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'stock.pdf';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
  
  downloadExcel() {
    this.stockService.downloadStockExcel().subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'stock.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
  downloadStatisticsPdf() {
    this.stockService.downloadStatisticsPdf().subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'stock-statistics.pdf';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
  
  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'top' },
      title: { display: true, text: '' }  // Titre sera dynamique
    }
  };
  
  barChartType: 'bar' = 'bar';
  
  chartConfigs: {
    title: string;
    data: ChartConfiguration<'bar'>['data'];
  }[] = [];
  
  totalStockQuantity: number = 0;
  
  generateStatistics(): void {
    const attributes: (keyof Stock)[] = ['status', 'location'];
    this.totalStockQuantity = this.stocks.reduce((sum, s) => sum + s.quantity, 0);
  
    this.chartConfigs = attributes.map(attr => {
      const statMap: { [key: string]: number } = {};
  
      this.stocks.forEach(stock => {
        const key = String(stock[attr] ?? 'Undefined');
        statMap[key] = (statMap[key] || 0) + stock.quantity;
      });
  
      return {
        title: `📊 Stock by ${attr.charAt(0).toUpperCase() + attr.slice(1)}`,
        data: {
          labels: Object.keys(statMap),
          datasets: [{
            data: Object.values(statMap),
            label: 'Quantity',
            backgroundColor: this.generateColors(Object.keys(statMap).length),
          }]
        }
      };
    });
  }
  
  generateColors(count: number): string[] {
    const baseColors = ['#4CAF50', '#FFC107', '#F44336', '#2196F3', '#9C27B0', '#00BCD4'];
    return Array.from({ length: count }, (_, i) => baseColors[i % baseColors.length]);
  }
  
}
