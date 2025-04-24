import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductService } from '../../services/product.service';
import { KeycloakService } from '../../services/keycloak.service';
import { Product, Category, ProductStatsDTO } from '../../models/product.model';
import { Observable, of } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { QRCodeComponent } from 'angularx-qrcode';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js'; // Import Chart.js types

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    NgChartsModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    QRCodeComponent,
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  products = new MatTableDataSource<Product>([]);
  displayedColumns: string[] = ['name', 'price', 'category', 'actions'];
  stats: ProductStatsDTO | null = null;
  searchKeyword: string = '';
  selectedCategory: Category | null = null;
  categories = Object.values(Category);
  isLoading: boolean = false;
  errorMessage: string | null = null;
  qrCodeData: string | null = null;

  // Chart data and options with proper typing
  public pieChartData: ChartData<'pie', number[], string> = {
    labels: [],
    datasets: [
      {
        data: [],
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'],
        hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'],
      },
    ],
  };

  public pieChartOptions: ChartConfiguration<'pie'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top' as const, // Explicitly set to one of the allowed values
      },
      tooltip: {
        enabled: true,
      },
    },
  };

  constructor(
    private productService: ProductService,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.loadProductsAndStats();
  }

  loadProductsAndStats(): void {
    this.isLoading = true;
    this.errorMessage = null;
    this.keycloakService
      .isAuthenticated()
      .pipe(
        switchMap((authenticated) => {
          if (!authenticated) {
            this.errorMessage = 'Please log in to view products.';
            return of([]);
          }
          return this.productService.getAllProducts();
        }),
        tap((products) => {
          this.products.data = products;
          this.loadStats();
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to load products.';
          this.isLoading = false;
          return of([]);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  loadStats(): void {
    this.productService
      .getProductStats()
      .pipe(
        tap((stats) => {
          this.stats = stats;
          // Update chart data based on stats
          if (stats && stats.byCategory) {
            this.pieChartData = {
              labels: Object.keys(stats.byCategory),
              datasets: [
                {
                  data: Object.values(stats.byCategory),
                  backgroundColor: [
                    '#FF6384',
                    '#36A2EB',
                    '#FFCE56',
                    '#4BC0C0',
                    '#9966FF',
                    '#FF9F40',
                  ],
                  hoverBackgroundColor: [
                    '#FF6384',
                    '#36A2EB',
                    '#FFCE56',
                    '#4BC0C0',
                    '#9966FF',
                    '#FF9F40',
                  ],
                },
              ],
            };
          }
        }),
        catchError((error) => {
          console.error('Failed to load stats', error);
          return of(null);
        })
      )
      .subscribe();
  }

  searchProducts(): void {
    if (!this.searchKeyword.trim()) {
      this.loadProductsAndStats();
      return;
    }
    this.isLoading = true;
    this.errorMessage = null;
    this.productService
      .searchProducts(this.searchKeyword.trim())
      .pipe(
        tap((products) => {
          this.products.data = products;
          this.loadStats();
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to search products.';
          this.isLoading = false;
          return of([]);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  filterByCategory(): void {
    this.isLoading = true;
    this.errorMessage = null;
    const observable: Observable<Product[]> = this.selectedCategory
      ? this.productService.getProductsByCategory(this.selectedCategory)
      : this.productService.getAllProducts();

    observable
      .pipe(
        tap((products) => {
          this.products.data = products;
          this.loadStats();
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to filter products.';
          this.isLoading = false;
          return of([]);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.isLoading = true;
      this.errorMessage = null;
      this.productService
        .deleteProduct(id)
        .pipe(
          tap(() => this.loadProductsAndStats()),
          catchError((error) => {
            this.errorMessage = error.message || 'Failed to delete product.';
            this.isLoading = false;
            return of(null);
          })
        )
        .subscribe();
    }
  }

  generateQRCode(productId: number): void {
    if (!productId) {
      this.qrCodeData = null;
      return;
    }
    this.isLoading = true;
    this.productService
      .getProductById(productId)
      .pipe(
        tap((product) => {
          this.qrCodeData = JSON.stringify({
            id: product.id ?? 'N/A',
            name: product.name ?? 'N/A',
            description: product.description ?? 'N/A',
            price: product.price ?? 0,
            category: product.category ?? 'N/A',
          });
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to generate QR code.';
          this.qrCodeData = null;
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  clearQRCode(): void {
    this.qrCodeData = null;
  }

  retry(): void {
    this.loadProductsAndStats();
  }
}