import { Component, OnInit } from '@angular/core';
import { StockService } from '../../../services/stock.service';  
import { Stock } from '../../../models/stock.model';

@Component({
  selector: 'app-list-stock',
  templateUrl: './list-stock.component.html',
  styleUrls: ['./list-stock.component.scss']
})
export class ListStockComponent implements OnInit {
  stocks: Stock[] = [];  // Tableau pour stocker les stocks récupérés
  loading: boolean = false;  // Indicateur de chargement pour l'interface

  constructor(private stockService: StockService) { }

  ngOnInit(): void {
    this.loadStocks();
  }

  loadStocks(): void {
    this.loading = true;
    this.stockService.getAll().subscribe({
      next: (data) => {
        this.stocks = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des stocks', err);
        console.error('Message d\'erreur:', err.message);
        console.error('Statut HTTP:', err.status);
        this.loading = false;
      }
    });
  }
  
  
  editStock(id: number): void {
    console.log('Édition du stock avec ID:', id);
    // Ajouter la logique pour rediriger vers le formulaire d'édition
  }
  
  deleteStock(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce stock ?')) {
      this.stockService.delete(id).subscribe({
        next: () => {
          this.stocks = this.stocks.filter(stock => stock.id !== id);
          alert('Stock supprimé avec succès');
        },
        error: (err) => {
          console.error('Erreur lors de la suppression du stock', err);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }
  
}
