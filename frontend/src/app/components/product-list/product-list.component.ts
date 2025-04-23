// src/app/components/product-list/product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSpinner } from '@angular/material/progress-spinner';
import { ProductService } from '../../services/product.service';
import { KeycloakService } from '../../services/keycloak.service';
import { Product, Category, ProductStatsDTO } from '../../models/product.model';
import { Observable, of } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';

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
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatSpinner,
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
  categories = Object.values(Category); // Assumes Category is an enum
  isLoading: boolean = false;
  errorMessage: string | null = null;
  qrCodeUrl: string | null = null;

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
        tap((stats) => (this.stats = stats)),
        catchError((error) => {
          console.error('Failed to load stats', error);
          return of(null);
        })
      )
      .subscribe();
  }

  searchProducts(): void {
    if (!this.searchKeyword) {
      this.loadProductsAndStats();
      return;
    }
    this.isLoading = true;
    this.errorMessage = null;
    this.productService
      .searchProducts(this.searchKeyword)
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

  generateQRCode(id: number): void {
    this.isLoading = true;
    this.errorMessage = null;
    this.productService
      .generateQRCode(id)
      .pipe(
        tap((qrCode) => {
          this.qrCodeUrl = qrCode; // Assumes QR code is a URL or base64 string
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to generate QR code.';
          this.isLoading = false;
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  retry(): void {
    this.loadProductsAndStats();
  }
}