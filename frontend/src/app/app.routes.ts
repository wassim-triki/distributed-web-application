import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ListReclamationComponent } from './components/reclamation/list-reclamation/list-reclamation.component';
import { AddReclamationComponent } from './components/reclamation/add-reclamation/add-reclamation.component';
import { DetailReclamationComponent } from './components/reclamation/detail-reclamation/detail-reclamation.component';
import { HomeComponent } from './components/home/home.component';



export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },

  { path: 'products', component: ProductListComponent },
  { path: 'product/add', component: ProductFormComponent },
  { path: 'product/edit/:id', component: ProductFormComponent },
  { path: 'product/:id', component: ProductDetailsComponent },
  
  { path: 'listreclamation', component: ListReclamationComponent },
  { path: 'reclamation-detail/:id', component: DetailReclamationComponent },
  { path: 'addreclamation', component: AddReclamationComponent },




];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}