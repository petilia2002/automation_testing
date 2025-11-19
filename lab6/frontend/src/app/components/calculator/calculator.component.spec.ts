import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { CalculatorComponent } from './calculator.component';
import {
  CalculatorService,
  CalculateResponse,
} from '../../services/calculator.service';
import { NumberSystem, OperationType } from '../../models/enums';
import { NumberInputComponent } from '../number-input/number-input.component';
import { ResultColorDirective } from '../../directives/result-color.directive';
import { PrecisionPipe } from '../../pipes/precision.pipe';

class MockCalculatorService {
  calculate = jasmine
    .createSpy('calculate')
    .and.returnValue(
      of({ result: '15', calculationId: 123 } as CalculateResponse)
    );
}

describe('CalculatorComponent', () => {
  let component: CalculatorComponent;
  let fixture: ComponentFixture<CalculatorComponent>;
  let calculatorService: MockCalculatorService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CalculatorComponent,
        ReactiveFormsModule,
        NumberInputComponent,
        ResultColorDirective,
        PrecisionPipe,
      ],
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

  it('should initialize form with default values', () => {
    expect(component.form.get('firstNumber')?.value).toBe('');
    expect(component.form.get('secondNumber')?.value).toBe('');
    expect(component.form.get('operationType')?.value).toBe(OperationType.ADD);
    expect(component.form.get('precision')?.value).toBe(2);
    expect(component.form.get('firstNumberSystem')?.value).toBe(
      NumberSystem.DECIMAL
    );
    expect(component.form.get('secondNumberSystem')?.value).toBe(
      NumberSystem.DECIMAL
    );
  });

  it('should validate form as invalid when empty', () => {
    expect(component.form.valid).toBeFalse();
  });

  it('should validate form as valid when all fields are filled', () => {
    component.form.patchValue({
      firstNumber: '10',
      secondNumber: '5',
      operationType: OperationType.ADD,
      firstNumberSystem: NumberSystem.DECIMAL,
      secondNumberSystem: NumberSystem.DECIMAL,
      precision: 2,
    });

    expect(component.form.valid).toBeTrue();
  });

  it('should set isDivide to true when division operation is selected', () => {
    component.form.get('operationType')?.setValue(OperationType.DIVIDE);
    expect(component.isDivide).toBeTrue();
  });

  it('should call calculator service on form submission', () => {
    component.form.patchValue({
      firstNumber: '10',
      secondNumber: '5',
      operationType: OperationType.ADD,
      firstNumberSystem: NumberSystem.DECIMAL,
      secondNumberSystem: NumberSystem.DECIMAL,
      precision: 2,
    });

    component.onCalculate();

    expect(calculatorService.calculate).toHaveBeenCalled();
    expect(component.result).toBe('15');
    expect(component.calculationId).toBe(123);
    expect(component.busy).toBeFalse();
  });

  it('should handle calculation error', () => {
    calculatorService.calculate.and.returnValue(
      throwError(() => new Error('Server error'))
    );

    component.form.patchValue({
      firstNumber: '10',
      secondNumber: '5',
      operationType: OperationType.ADD,
      firstNumberSystem: NumberSystem.DECIMAL,
      secondNumberSystem: NumberSystem.DECIMAL,
      precision: 2,
    });

    component.onCalculate();

    expect(component.error).toBe('Ошибка на сервере');
    expect(component.busy).toBeFalse();
  });

  it('should mark form as touched when invalid form is submitted', () => {
    spyOn(component.form, 'markAllAsTouched');

    component.onCalculate();

    expect(component.form.markAllAsTouched).toHaveBeenCalled();
  });

  it('should have operation options', () => {
    expect(component.operationOptions.length).toBeGreaterThan(0);
    expect(component.operationOptions[0].label).toBeDefined();
  });

  it('should have number system options', () => {
    expect(component.numberSystemOptions.length).toBeGreaterThan(0);
    expect(component.numberSystemOptions[0].label).toBeDefined();
  });

  it('should clear second number when switching to division and value is zero', () => {
    component.form.patchValue({
      secondNumber: '0',
      operationType: OperationType.ADD,
    });

    component.form.get('operationType')?.setValue(OperationType.DIVIDE);

    expect(component.form.get('secondNumber')?.value).toBe('');
  });

  it('should apply non-zero validator for division operation', () => {
    const secondNumberControl = component.form.get('secondNumber');
    const initialValidator = secondNumberControl?.validator;

    component.form.get('operationType')?.setValue(OperationType.DIVIDE);

    expect(secondNumberControl?.validator).toBeDefined();
    expect(secondNumberControl?.validator).not.toBe(initialValidator);

    secondNumberControl?.setValue('0');
    expect(secondNumberControl?.hasError('zeroNotAllowed')).toBeTrue();

    secondNumberControl?.setValue('5');
    expect(secondNumberControl?.valid).toBeTrue();
  });
});
