import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';  // Import CommonModule here
import { ReclamationService } from '../../../services/reclamation.service';  // Adjust path
import { Reclamation } from '../../../models/reclamation.model';  // Adjust path

@Component({
  selector: 'app-statics',
  templateUrl: './statics.component.html',
  styleUrls: ['./statics.component.scss'],
  standalone: true,  // Make this a standalone component
  imports: [CommonModule]  // Import CommonModule here for *ngFor and *ngIf
})
export class StaticsComponent implements OnInit {
  generalStats: any;
  monthlyStats: any;

  constructor(private reclamationService: ReclamationService) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    

    this.reclamationService.getMonthlyStats().subscribe(
      (data) => {
        this.monthlyStats = data;
      },
      (error) => {
        console.error('Error fetching monthly stats:', error);
      }
    );
  }

  downloadPdf(): void {
    this.reclamationService.downloadPdf().subscribe(
      (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'all_reclamations.pdf';
        a.click();
      },
      (error) => {
        console.error('Error downloading PDF:', error);
      }
    );
  }
}
