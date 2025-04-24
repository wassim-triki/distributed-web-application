import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Product, Category, ProductStatsDTO } from '../models/product.model';

const API_BASE_URL = 'http://localhost:8081'; // Base URL for the backend API

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${API_BASE_URL}/products`; // Endpoint for products

  // Fetch all products
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl).pipe(
      tap((products) => console.log('API Response (getAllProducts):', products)),
      catchError(this.handleError)
    );
  }

  // Fetch a product by its ID
  getProductById(id: number): Observable<Product> {
    if (!Number.isInteger(id) || id <= 0) {
      console.error('Invalid product ID:', id);
      return throwError(() => new Error('Invalid product ID: ID must be a positive integer.'));
    }
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      tap((product) => console.log('API Response (getProductById):', product)),
      catchError(this.handleError)
    );
  }

  // Add a new product
  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product).pipe(
      tap((savedProduct) => console.log('API Response (addProduct):', savedProduct)),
      catchError(this.handleError)
    );
  }

  // Update an existing product
  updateProduct(id: number, product: Product): Observable<Product> {
    if (!Number.isInteger(id) || id <= 0) {
      console.error('Invalid product ID:', id);
      return throwError(() => new Error('Invalid product ID: ID must be a positive integer.'));
    }
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product).pipe(
      tap((updatedProduct) => console.log('API Response (updateProduct):', updatedProduct)),
      catchError(this.handleError)
    );
  }

  // Delete a product by its ID
  deleteProduct(id: number): Observable<void> {
    if (!Number.isInteger(id) || id <= 0) {
      console.error('Invalid product ID:', id);
      return throwError(() => new Error('Invalid product ID: ID must be a positive integer.'));
    }
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('API Response (deleteProduct): Product deleted, ID:', id)),
      catchError(this.handleError)
    );
  }

  // Fetch product statistics
  getProductStats(): Observable<ProductStatsDTO> {
    return this.http.get<ProductStatsDTO>(`${this.apiUrl}/stats`).pipe(
      tap((stats) => console.log('API Response (getProductStats):', stats)),
      catchError(this.handleError)
    );
  }

  // Fetch monthly product statistics
  getMonthlyProductStats(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/stats/monthly`).pipe(
      tap((monthlyStats) => console.log('API Response (getMonthlyProductStats):', monthlyStats)),
      catchError(this.handleError)
    );
  }

  // Fetch products by category
  getProductsByCategory(category: Category): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${category.toString()}`).pipe(
      tap((products) => console.log('API Response (getProductsByCategory):', products)),
      catchError(this.handleError)
    );
  }

  // Fetch products within a price range, optionally filtered by category
  getProductsByPriceRange(minPrice: number, maxPrice: number, category?: Category): Observable<Product[]> {
    let params = new HttpParams()
      .set('minPrice', minPrice.toString())
      .set('maxPrice', maxPrice.toString());

    if (category) {
      params = params.set('category', category.toString());
    }

    return this.http.get<Product[]>(`${this.apiUrl}/price`, { params }).pipe(
      tap((products) => console.log('API Response (getProductsByPriceRange):', products)),
      catchError(this.handleError)
    );
  }

  // Search for products by keyword
  searchProducts(keyword: string): Observable<Product[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params }).pipe(
      tap((products) => console.log('API Response (searchProducts):', products)),
      catchError(this.handleError)
    );
  }

  // Handle HTTP errors
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred.';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Server-side error
      const backendMessage = error.error?.message || error.message;
      switch (error.status) {
        case 400:
          errorMessage = `Bad request: ${backendMessage}`;
          break;
        case 401:
          errorMessage = 'Unauthorized - Please log in again.';
          break;
        case 404:
          errorMessage = `Resource not found: ${backendMessage}`;
          break;
        case 500:
          errorMessage = `Internal server error: ${backendMessage}`;
          break;
        default:
          errorMessage = `Unexpected server error: ${error.status} - ${backendMessage}`;
      }
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}