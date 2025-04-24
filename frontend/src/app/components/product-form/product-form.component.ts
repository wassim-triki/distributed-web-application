import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { KeycloakService } from '../../services/keycloak.service';
import { Product, Category } from '../../models/product.model';
import { catchError, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss'],
})
export class ProductFormComponent implements OnInit {
  product: Product = {
    id: 0,
    name: '',
    description: '',
    price: 0,
    category: Category.CLOTHING,
    stockQuantity: 0,
    supplierEmail: '',
    dateAdded: new Date(),
  };
  categories = Object.values(Category);
  isLoading = false;
  errorMessage: string | null = null;
  isEditMode = false;

  constructor(
    private productService: ProductService,
    private keycloakService: KeycloakService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.errorMessage = null;

    console.log('ProductFormComponent: Entering ngOnInit, Route:', this.route.snapshot.url);

    this.keycloakService
      .isAuthenticated()
      .pipe(
        tap((authenticated) => console.log('User is authenticated:', authenticated)),
        switchMap((authenticated) => {
          if (!authenticated) {
            this.errorMessage = 'Please log in to access this page.';
            this.isLoading = false;
            return of(null);
          }
          const id = this.route.snapshot.paramMap.get('id');
          console.log('Route ID:', id);
          if (id && Number.isInteger(+id) && +id > 0) {
            this.isEditMode = true;
            console.log('Calling getProductById with ID:', +id);
            return this.productService.getProductById(+id);
          }
          this.isEditMode = false;
          console.log('Add mode: No product fetch required');
          return of(null);
        }),
        tap((product) => {
          if (product) {
            console.log('Loaded Product:', product);
            this.product = {
              ...product,
              description: product.description ?? '',
              stockQuantity: product.stockQuantity ?? 0,
              supplierEmail: product.supplierEmail ?? '',
              dateAdded: product.dateAdded ?? new Date(),
            };
          }
        }),
        catchError((error) => {
          console.error('Error in ngOnInit:', error);
          this.errorMessage = error.message || 'Failed to load product.';
          this.isLoading = false;
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  saveProduct(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Create productToSave, omitting id and dateAdded for add mode
    const productToSave: Omit<Product, 'id' | 'dateAdded'> & { id?: number; dateAdded?: Date } = {
      name: this.product.name,
      description: this.product.description || '',
      price: this.product.price,
      category: this.product.category,
      stockQuantity: this.product.stockQuantity,
      supplierEmail: this.product.supplierEmail,
    };

    if (this.isEditMode) {
      productToSave.id = this.product.id;
      productToSave.dateAdded = this.product.dateAdded;
    }

    console.log('Saving product:', productToSave);

    if (this.isEditMode && !productToSave.id) {
      console.error('Edit mode: Product ID is missing');
      this.errorMessage = 'Cannot update product: Missing ID.';
      this.isLoading = false;
      return;
    }

    const operation = this.isEditMode
      ? this.productService.updateProduct(productToSave.id!, productToSave as Product)
      : this.productService.addProduct(productToSave as Product);

    operation
      .pipe(
        tap((savedProduct) => {
          console.log(this.isEditMode ? 'Product updated:' : 'Product added:', savedProduct);
          this.router.navigate(['/products']);
        }),
        catchError((error) => {
          console.error('Error saving product:', error);
          this.errorMessage = error.message || `Failed to ${this.isEditMode ? 'update' : 'add'} product.`;
          this.isLoading = false;
          return of(null);
        })
      )
      .subscribe(() => (this.isLoading = false));
  }

  cancel(): void {
    this.router.navigate(['/products']);
  }
}