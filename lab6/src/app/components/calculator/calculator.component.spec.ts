import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { CalculatorComponent } from './calculator.component';
import { CalculatorService } from '../../services/calculator.service';

class MockCalculatorService {
  calculate = jasmine
    .createSpy('calculate')
    .and.returnValue(of({ result: '15', calculationId: 123 }));
  history = jasmine.createSpy('history').and.returnValue(of([]));
}

describe('CalculatorComponent', () => {
  let component: CalculatorComponent;
  let fixture: ComponentFixture<CalculatorComponent>;
  let calculatorService: MockCalculatorService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalculatorComponent, ReactiveFormsModule],
      providers: [
        { provide: CalculatorService, useClass: MockCalculatorService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CalculatorComponent);
    component = fixture.componentInstance;
    calculatorService = TestBed.inject(CalculatorService) as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form', () => {
    expect(component.form).toBeTruthy();
    expect(component.form.get('firstNumber')).toBeTruthy();
    expect(component.form.get('secondNumber')).toBeTruthy();
    expect(component.form.get('operationType')).toBeTruthy();
    expect(component.form.get('precision')).toBeTruthy();
    expect(component.form.get('firstNumberSystem')).toBeTruthy();
    expect(component.form.get('secondNumberSystem')).toBeTruthy();
  });
});
