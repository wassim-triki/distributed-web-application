// src/app/components/product-details/product-details.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatSpinner } from '@angular/material/progress-spinner';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { KeycloakService } from '../../services/keycloak.service';
import { Product } from '../../models/product.model';
import { catchError, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatSpinner,
  ],
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'],
})
export class ProductDetailsComponent implements OnInit {
  product: Product | null = null;
  qrCodeUrl: string | null = null;
  isLoading: boolean = false;
  errorMessage: string | null = null;

  constructor(
    private productService: ProductService,
    private keycloakService: KeycloakService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadProduct();
  }

  loadProduct(): void {
    this.isLoading = true;
    this.errorMessage = null;
    this.keycloakService
      .isAuthenticated()
      .pipe(
        switchMap((authenticated) => {
          if (!authenticated) {
            this.errorMessage = 'Please log in to view product details.';
            return of(null);
          }
          const id = this.route.snapshot.paramMap.get('id');
          if (!id) {
            this.errorMessage = 'Invalid product ID.';
            return of(null);
          }
          return this.productService.getProductById(+id);
        }),
        tap((product) => {
          this.product = product;
          if (product) {
            this.loadQRCode(product.id);
          }
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to load product.';
          this.isLoading = false;
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  loadQRCode(id: number): void {
    this.productService
      .generateQRCode(id)
      .pipe(
        tap((qrCode) => {
          this.qrCodeUrl = qrCode; // Assumes QR code is a URL or base64 string
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to generate QR code.';
          return of(null);
        })
      )
      .subscribe();
  }

  retry(): void {
    this.loadProduct();
  }
}