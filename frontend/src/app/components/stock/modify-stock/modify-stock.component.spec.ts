import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyStockComponent } from './modify-stock.component';

describe('ModifyStockComponent', () => {
  let component: ModifyStockComponent;
  let fixture: ComponentFixture<ModifyStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyStockComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
