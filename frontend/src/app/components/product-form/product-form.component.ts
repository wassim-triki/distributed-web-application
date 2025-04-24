// src/app/components/product-form/product-form.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSpinner } from '@angular/material/progress-spinner';
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
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatSpinner,
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
    stockQuantity: 0,
    category: Category.ELECTRONICS, // Use enum value
    supplierEmail: '',
    dateAdded: new Date(),
  };
  categories = Object.values(Category);
  isLoading: boolean = false;
  errorMessage: string | null = null;
  isEditMode: boolean = false;

  constructor(
    private productService: ProductService,
    private keycloakService: KeycloakService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.keycloakService
      .isAuthenticated()
      .pipe(
        switchMap((authenticated) => {
          if (!authenticated) {
            this.errorMessage = 'Please log in to edit products.';
            return of(null);
          }
          const id = this.route.snapshot.paramMap.get('id');
          if (id) {
            this.isEditMode = true;
            return this.productService.getProductById(+id);
          }
          return of(null);
        }),
        tap((product) => {
          if (product) {
            this.product = { ...product }; // Create a copy to avoid mutating original
          }
        }),
        catchError((error) => {
          this.errorMessage = error.message || 'Failed to load product.';
          return of(null);
        })
      )
      .subscribe();
  }

  onSubmit(form: NgForm): void {
    if (!form.valid) {
      this.errorMessage = 'Please fill out all required fields correctly.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    const request = this.isEditMode
      ? this.productService.updateProduct(this.product.id, this.product)
      : this.productService.addProduct(this.product);

    request
      .pipe(
        tap(() => this.router.navigate(['/products'])),
        catchError((error) => {
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