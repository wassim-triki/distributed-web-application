import { TestBed } from '@angular/core/testing';
   import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
   import { ProductService } from './product.service';
   import { Product, Category } from '../models/product.model';

   describe('ProductService', () => {
     let service: ProductService;
     let httpMock: HttpTestingController;

     beforeEach(() => {
       TestBed.configureTestingModule({
         imports: [HttpClientTestingModule],
         providers: [ProductService]
       });
       service = TestBed.inject(ProductService);
       httpMock = TestBed.inject(HttpTestingController);
     });

     afterEach(() => {
       httpMock.verify();
     });

     it('should fetch all products', () => {
       const mockProducts: Product[] = [
         { id: 1, name: 'Test', description: '', price: 10, stockQuantity: 100, category: Category.ELECTRONICS, supplierEmail: 'test@test.com', dateAdded: new Date() }
       ];
       service.getAllProducts().subscribe(products => {
         expect(products).toEqual(mockProducts);
       });
       const req = httpMock.expectOne(`${service['apiUrl']}`);
       expect(req.request.method).toBe('GET');
       req.flush(mockProducts);
     });
   });