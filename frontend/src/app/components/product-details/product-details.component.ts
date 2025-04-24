import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { KeycloakService } from '../../services/keycloak.service';
import { Product } from '../../models/product.model';
import { QRCodeComponent } from 'angularx-qrcode';
import { catchError, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    QRCodeComponent,
  ],
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'],
})
export class ProductDetailsComponent implements OnInit {
  product: Product | null = null;
  qrCodeData: string | null = null;
  isLoading: boolean = false;
  errorMessage: string | null = null;

  constructor(
    private productService: ProductService,
    private keycloakService: KeycloakService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProduct();
  }

  loadProduct(): void {
    this.isLoading = true;
    this.errorMessage = null;

    console.log('ProductDetailsComponent: Entering loadProduct, Route:', this.route.snapshot.url);

    this.keycloakService
      .isAuthenticated()
      .pipe(
        tap((authenticated) => console.log('User is authenticated:', authenticated)),
        switchMap((authenticated) => {
          if (!authenticated) {
            this.errorMessage = 'Please log in to view product details.';
            this.isLoading = false;
            return of(null);
          }
          const id = this.route.snapshot.paramMap.get('id');
          console.log('Route ID:', id);
          if (id && Number.isInteger(+id) && +id > 0) {
            console.log('Calling getProductById with ID:', +id);
            return this.productService.getProductById(+id);
          }
          console.log('Invalid or missing product ID, skipping fetch');
          this.errorMessage = 'Invalid product ID.';
          this.isLoading = false;
          this.router.navigate(['/products']);
          return of(null);
        }),
        tap((product) => {
          if (product) {
            console.log('Loaded Product:', product);
            this.product = product;
            this.loadQRCode(product);
          }
        }),
        catchError((error) => {
          console.error('Error loading product:', error);
          this.errorMessage = error.message || 'Failed to load product.';
          this.isLoading = false;
          this.router.navigate(['/products']);
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  loadQRCode(product: Product): void {
    console.log('Generating QR Code for Product:', product);
    if (!product || !product.id) {
      console.error('Product is invalid:', product);
      this.qrCodeData = null;
      return;
    }
    this.qrCodeData = JSON.stringify({
      id: product.id,
      name: product.name,
      description: product.description,
      price: product.price,
      category: product.category,
    });
    console.log('QR Code Data:', this.qrCodeData);
  }

  retry(): void {
    this.loadProduct();
  }
}