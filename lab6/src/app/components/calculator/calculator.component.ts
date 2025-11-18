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

  OperationType = OperationType;
  NumberSystem = NumberSystem;

  // человекочитаемые метки (можно изменить)
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

  readonly operationOptions = (
    Object.keys(OperationType) as Array<keyof typeof OperationType>
  ).map((k) => {
    const v = OperationType[k] as OperationType;
    return { key: k, value: v, label: this.OPERATION_LABELS[v] ?? String(v) };
  });

  readonly numberSystemOptions = (
    Object.keys(NumberSystem) as Array<keyof typeof NumberSystem>
  ).map((k) => {
    const v = NumberSystem[k] as NumberSystem;
    return {
      key: k,
      value: v,
      label: this.NUMBERSYSTEM_LABELS[v] ?? String(v),
    };
  });

  // флаг, который передаётся в дочерний компонент — запрещает ввод нуля во втором инпуте
  isDivide = false;

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
      this.isDivide = op === OperationType.DIVIDE;

      if (this.isDivide) {
        // применяем валидатор, запрещающий ноль
        this.form
          .get('secondNumber')!
          .setValidators([Validators.required, this.nonZeroValidator]);
        // если текущее значение равно 0 — очищаем
        const cur = this.form.get('secondNumber')!.value;
        if (cur === '0' || cur === '-0') {
          this.form.get('secondNumber')!.setValue('');
        }
      } else {
        // обычный валидатор
        this.form.get('secondNumber')!.setValidators([Validators.required]);
      }
      this.form.get('secondNumber')!.updateValueAndValidity();
    });
  }

  // контроллерная валидация: если numeric === 0, возвращаем ошибку
  nonZeroValidator(control: any) {
    if (
      !control ||
      control.value === null ||
      control.value === undefined ||
      control.value === ''
    ) {
      return { required: true };
    }
    // Преобразуем строку в число (предполагая десятичную систему на уровне валидации)
    // Если нужно учитывать систему — можно прочитать соседний control и парсить с нужным base.
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
