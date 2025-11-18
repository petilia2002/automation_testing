import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CalculatorService } from '../../services/calculator.service';
import { NumberSystem, OperationType } from '../../models/enums';

import { NumberInputComponent } from '../number-input/number-input.component';
import { ResultColorDirective } from '../../directives/result-color.directive';
import { PrecisionPipe } from '../../pipes/precision.pipe';

@Component({
  selector: 'app-calculator',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NumberInputComponent,
    ResultColorDirective,
    PrecisionPipe,
  ],
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css'],
})
export class CalculatorComponent implements OnInit {
  form: FormGroup;

  // Экспорт enums в шаблон (на случай сравнения)
  OperationType = OperationType;
  NumberSystem = NumberSystem;

  // Карты "enum -> читабельная метка (русская)"
  private readonly OPERATION_LABELS: Record<OperationType, string> = {
    [OperationType.ADD]: 'Сложить',
    [OperationType.SUBTRACT]: 'Вычесть',
    [OperationType.MULTIPLY]: 'Умножить',
    [OperationType.DIVIDE]: 'Разделить',
  };

  private readonly NUMBERSYSTEM_LABELS: Record<NumberSystem, string> = {
    [NumberSystem.BINARY]: 'Двоичная',
    [NumberSystem.OCTAL]: 'Восьмеричная',
    [NumberSystem.DECIMAL]: 'Десятичная',
    [NumberSystem.HEXADECIMAL]: 'Шестнадцатеричная',
  };

  // Подготовленные опции с label
  readonly operationOptions: Array<{
    key: keyof typeof OperationType;
    value: OperationType;
    label: string;
  }> = (Object.keys(OperationType) as Array<keyof typeof OperationType>).map(
    (k) => {
      const val = OperationType[k] as OperationType;
      return {
        key: k,
        value: val,
        label: this.OPERATION_LABELS[val] ?? String(val),
      };
    }
  );

  readonly numberSystemOptions: Array<{
    key: keyof typeof NumberSystem;
    value: NumberSystem;
    label: string;
  }> = (Object.keys(NumberSystem) as Array<keyof typeof NumberSystem>).map(
    (k) => {
      const val = NumberSystem[k] as NumberSystem;
      return {
        key: k,
        value: val,
        label: this.NUMBERSYSTEM_LABELS[val] ?? String(val),
      };
    }
  );

  result: string | null = null;
  calculationId: number | null = null;
  busy = false;
  error: string | null = null;

  constructor(private fb: FormBuilder, private calcSvc: CalculatorService) {
    this.form = this.fb.group({
      firstNumber: ['', Validators.required],
      firstNumberSystem: [NumberSystem.DECIMAL, Validators.required],
      secondNumber: ['', Validators.required],
      secondNumberSystem: [NumberSystem.DECIMAL, Validators.required],
      operationType: [OperationType.ADD, Validators.required],
      precision: [
        2,
        [Validators.required, Validators.min(0), Validators.max(20)],
      ],
    });
  }

  ngOnInit(): void {
    this.form.get('operationType')!.valueChanges.subscribe((op) => {
      if (op === OperationType.DIVIDE) {
        this.form
          .get('secondNumber')!
          .setValidators([Validators.required, this.nonZeroValidator]);
      } else {
        this.form.get('secondNumber')!.setValidators([Validators.required]);
      }
      this.form.get('secondNumber')!.updateValueAndValidity();
    });
  }

  nonZeroValidator(control: any) {
    if (
      !control ||
      control.value === null ||
      control.value === undefined ||
      control.value === ''
    ) {
      return { required: true };
    }
    const v = Number(control.value);
    if (!isNaN(v) && v === 0) {
      return { zeroNotAllowed: true };
    }
    return null;
  }

  onCalculate() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.busy = true;
    this.error = null;

    const payload = {
      firstNumber: this.form.value.firstNumber,
      firstNumberSystem: this.form.value.firstNumberSystem,
      secondNumber: this.form.value.secondNumber,
      secondNumberSystem: this.form.value.secondNumberSystem,
      operationType: this.form.value.operationType,
    };

    this.calcSvc.calculate(payload).subscribe({
      next: (res) => {
        this.result = res.result;
        this.calculationId = res.calculationId;
        this.busy = false;
      },
      error: () => {
        this.error = 'Ошибка на сервере';
        this.busy = false;
      },
    });
  }
}
