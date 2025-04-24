import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ReclamationService } from '../../../services/reclamation.service';
import { Reclamation } from '../../../models/reclamation.model';

@Component({
  selector: 'app-list-reclamation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list-reclamation.component.html',
  styleUrls: ['./list-reclamation.component.scss']
})
export class ListReclamationComponent implements OnInit {
  reclamations: Reclamation[] = [];
  isLoading = true;
  error = '';

  constructor(
    private reclamationService: ReclamationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.reclamationService.getAllReclamations().subscribe({
      next: (data) => {
        this.reclamations = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement des réclamations.';
        this.isLoading = false;
      }
    });
  }


  addReclamation(): void {
    this.router.navigate(['/addreclamation']);
  }

  goToDetail(id: number): void {
    this.router.navigate(['/reclamation-detail', id]);
  }

  deleteReclamation(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer cette réclamation ?')) {
      this.reclamationService.deleteReclamation(id).subscribe({
        next: () => {
          this.reclamations = this.reclamations.filter(r => r.id !== id);
        },
        error: () => {
          this.error = 'Erreur lors de la suppression de la réclamation.';
        }
      });
    }
  }

  selectedQrCodeUrl: string | null = null;
  selectedReclamationId: number | null = null;
  
  showQrCode(id: number): void {
    this.selectedReclamationId = id;
    this.selectedQrCodeUrl = this.reclamationService.getQrCodeUrl(id);
  }
  
   

  
}
