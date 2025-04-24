import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StockService } from '../../../services/stock.service';
import { Stock, StockStatus } from '../../../models/stock.model';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-modify-stock',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './modify-stock.component.html',
  styleUrls: ['./modify-stock.component.scss']
})
export class ModifyStockComponent implements OnInit {
  stock: Stock = {
    productId: 0,
    location: '',
    minQuantity: 0,
    quantity: 0,
    status: StockStatus.AVAILABLE,
    notes: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private stockService: StockService
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.stockService.getById(id).subscribe(data => {
      this.stock = data;
    });
  }

  updateStock() {
    this.stockService.update(this.stock.id!, this.stock).subscribe(
      response => {
        console.log('Stock updated successfully', response);
        // Redirect to /stocks after successful update
        this.router.navigate(['/stocks']);
      },
      error => {
        console.error('Error updating stock:', error);
        alert('There was an error updating the stock.');
      }
    );
  }
  goBack() {
    this.router.navigate(['/stocks']);
  }
}