import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticsComponent } from './statics.component';

describe('StaticsComponent', () => {
  let component: StaticsComponent;
  let fixture: ComponentFixture<StaticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaticsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
