import { Component } from '@angular/core';
import { StockService } from '../../../services/stock.service';
import { Stock, StockStatus } from '../../../models/stock.model';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Import FormsModule
@Component({
  selector: 'app-add-stock',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-stock.component.html',
  styleUrls: ['./add-stock.component.scss']
})
export class AddStockComponent {
  stock: Stock = {
    productId: 0,
    location: '',
    minQuantity: 0,
    quantity: 0,
    status: StockStatus.AVAILABLE, // Ensure this is a correct enum value
    notes: ''
  };

  constructor(private stockService: StockService, private router: Router) {}

  addStock() {
    // Verify the data being passed
    console.log('Adding stock:', this.stock);

    this.stockService.add(this.stock).subscribe(
      response => {
        console.log('Stock added successfully', response);
        this.router.navigate(['/stocks']); // Redirect after adding stock
      },
      error => {
        console.error('There was an error adding the stock:', error);
        alert('Failed to add stock! Please check your inputs.');
      }
    );
  }

  goBack() {
    this.router.navigate(['/stocks']);
  }
}
