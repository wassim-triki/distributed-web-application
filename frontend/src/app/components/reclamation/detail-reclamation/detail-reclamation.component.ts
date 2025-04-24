import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ReclamationService } from '../../../services/reclamation.service';
import { Reclamation } from '../../../models/reclamation.model';

@Component({
  selector: 'app-detail-reclamation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detail-reclamation.component.html',
  styleUrls: ['./detail-reclamation.component.scss']   
})

export class DetailReclamationComponent implements OnInit {
  reclamation?: Reclamation;
  isLoading = true;
  error = '';
  success = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reclamationService: ReclamationService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.reclamationService.getReclamationById(id).subscribe({
        next: (data) => {
          this.reclamation = data;
          this.isLoading = false;
        },
        error: () => {
          this.error = 'Erreur lors du chargement de la réclamation.';
          this.isLoading = false;
        }
      });
    } else {
      this.error = 'ID de réclamation invalide.';
      this.isLoading = false;
    }
  }

  updateReclamation(): void {
    if (this.reclamation && typeof this.reclamation.id === 'number') {
      this.reclamationService.updateReclamation(this.reclamation.id, this.reclamation).subscribe({
        next: () => {
          this.success = '✅ Réclamation mise à jour avec succès.';
        },
        error: () => {
          this.error = '❌ Échec de la mise à jour de la réclamation.';
        }
      });
    } else {
      this.error = 'ID de réclamation invalide pour la mise à jour.';
    }
  }
  
  

  goBack(): void {
    this.router.navigate(['/listreclamation']);
  }
}
