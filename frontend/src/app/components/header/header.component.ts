import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // Needed for routerLink to work in the template
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, NgIf],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {}
