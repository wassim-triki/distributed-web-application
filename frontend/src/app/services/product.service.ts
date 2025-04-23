// src/app/services/product.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Product, Category, ProductStatsDTO } from '../models/product.model';

const API_BASE_URL = 'http://localhost:8093/products'; // Correct base URL

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${API_BASE_URL}/products`; // Correct: http://localhost:8093/products

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl).pipe(catchError(this.handleError));
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product).pipe(catchError(this.handleError));
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product).pipe(catchError(this.handleError));
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  getProductStats(): Observable<ProductStatsDTO> {
    return this.http.get<ProductStatsDTO>(`${this.apiUrl}/stats`).pipe(catchError(this.handleError));
  }

  getMonthlyProductStats(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/stats/monthly`).pipe(catchError(this.handleError));
  }

  getProductsByCategory(category: Category): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${category.toString()}`).pipe(catchError(this.handleError));
  }

  getProductsByPriceRange(minPrice: number, maxPrice: number, category?: Category): Observable<Product[]> {
    let params = new HttpParams()
      .set('minPrice', minPrice.toString())
      .set('maxPrice', maxPrice.toString());

    if (category) {
      params = params.set('category', category.toString());
    }

    return this.http.get<Product[]>(`${this.apiUrl}/price`, { params }).pipe(catchError(this.handleError));
  }

  searchProducts(keyword: string): Observable<Product[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params }).pipe(catchError(this.handleError));
  }

  generateQRCode(id: number): Observable<string> {
    return this.http
      .get(`${this.apiUrl}/${id}/generate-qr`, { responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
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