import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stock,StockStatus } from '../models/stock.model';
import { StockStatisticsDTO } from '../models/stock-statistics-dto.model';

@Injectable({
  providedIn: 'root'
})
export class StockService {

  private baseUrl = 'http://localhost:8085/stocks';

  constructor(private http: HttpClient) {}

  // CRUD Operations
  getAll(): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.baseUrl}`);
  }

  getById(id: number): Observable<Stock> {
    return this.http.get<Stock>(`${this.baseUrl}/${id}`);
  }

  add(stock: Stock): Observable<Stock> {
    return this.http.post<Stock>(`${this.baseUrl}/add`, stock);
  }

  update(id: number, stock: Stock): Observable<Stock> {
    return this.http.put<Stock>(`${this.baseUrl}/update/${id}`, stock);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  deleteAll(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/all`);
  }

  // Filter Stocks
  filter(status?: StockStatus, location?: string, quantityGreaterThan?: number): Observable<Stock[]> {
    const params: any = {};
    if (status) params.status = status;
    if (location) params.location = location;
    if (quantityGreaterThan !== undefined) params.quantity = quantityGreaterThan;

    return this.http.get<Stock[]>(`${this.baseUrl}/filter`, { params });
  }

  // Statistics
  getStatistics(): Observable<StockStatisticsDTO> {
    return this.http.get<StockStatisticsDTO>(`${this.baseUrl}/statistics`);
  }

  // Export
  downloadStockPdf(): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/pdf`, { responseType: 'blob' });
  }

  downloadStockExcel(): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/excel`, { responseType: 'blob' });
  }

  downloadStatisticsPdf(): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/statistics/pdf`, { responseType: 'blob' });
  }
}
