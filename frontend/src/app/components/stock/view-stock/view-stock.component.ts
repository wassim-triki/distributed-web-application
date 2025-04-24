import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StockService } from '../../../services/stock.service';
import { Stock, StockStatus } from '../../../models/stock.model';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Import Router for navigation
@Component({
  selector: 'app-view-stock',
  standalone: true,
  imports: [FormsModule,CommonModule], // Add FormsModule to imports
  templateUrl: './view-stock.component.html',
  styleUrls: ['./view-stock.component.scss']
})
export class ViewStockComponent implements OnInit {
  stock: Stock | null = null;
  isLoading = true; // Flag to track loading state

  constructor(
    private route: ActivatedRoute,
    private router: Router, // Inject Router for navigation
    private stockService: StockService
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.stockService.getById(id).subscribe(data => {
      this.stock = data;
      this.isLoading = false; // Set loading to false once data is fetched
    });
  }

  // Add a safe check for accessing stock properties
  getStatusClass(status: StockStatus | null): string {
    if (status === null) {
      return '';
    }
    switch (status) {
      case StockStatus.AVAILABLE:
        return 'AVAILABLE';
      case StockStatus.OUT_OF_STOCK:
        return 'OUT_OF_STOCK';
      case StockStatus.RESERVED:
        return 'RESERVED';
      default:
        return '';
    }
  }

  goBack() {
    this.router.navigate(['/stocks']);
  }
}
