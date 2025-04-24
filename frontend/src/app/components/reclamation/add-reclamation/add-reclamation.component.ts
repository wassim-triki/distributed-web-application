import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { ReclamationService } from '../../../services/reclamation.service';
import { Reclamation } from '../../../models/reclamation.model';

@Component({
  selector: 'app-add-reclamation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-reclamation.component.html',
  styleUrls: ['./add-reclamation.component.scss']
})
export class AddReclamationComponent {
  reclamation: Reclamation = {
    dateReclamation: new Date().toISOString().split('T')[0],
    description: '',
    orderId: 0,
    statut: 'En attente',
    type: 'AUTRE',
    emailClient: ''
  };

  types: string[] = [
    'PRODUIT_ENDOMMAGE',
    'RETARD_LIVRAISON',
    'ERREUR_COMMANDE',
    'SERVICE_CLIENT',
    'AUTRE'
  ];

  constructor(
    private reclamationService: ReclamationService,
    private router: Router
  ) {}

  saveReclamation(): void {
    this.reclamationService.createReclamation(this.reclamation).subscribe({
      next: () => {
        alert('✅ Réclamation ajoutée avec succès');
        this.router.navigate(['/listreclamation']);
      },
      error: (err) => {
        console.error('❌ Erreur lors de l\'ajout de la réclamation:', err);
        alert('Une erreur est survenue. Veuillez réessayer.');
      }
    });
  }




  
}
