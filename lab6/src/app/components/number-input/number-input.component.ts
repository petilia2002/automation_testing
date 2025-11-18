import { Component, forwardRef, Input, OnInit } from '@angular/core';
import {
  ControlValueAccessor,
  NG_VALUE_ACCESSOR,
  NG_VALIDATORS,
  Validator,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NumberSystem } from '../../models/enums';

@Component({
  selector: 'app-number-input',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './number-input.component.html',
  styleUrls: ['./number-input.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => NumberInputComponent),
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => NumberInputComponent),
      multi: true,
    },
  ],
})
export class NumberInputComponent
  implements ControlValueAccessor, Validator, OnInit
{
  @Input() label: string = '';
  @Input() numberSystem: NumberSystem = NumberSystem.DECIMAL;

  /** Если true — запрещаем ввод/вставку нулевого значения (0 or -0) */
  @Input() forbidZero = false;

  value: string = '';
  onChange = (_: any) => {};
  onTouched = () => {};

  ngOnInit(): void {}

  writeValue(obj: any): void {
    this.value = obj ?? '';
  }
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState?(isDisabled: boolean): void {
    // не используем disabled напрямую (управление через FormControl)
  }

  // --- UI events handlers ---

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const raw = input.value;
    if (this.forbidZero) {
      const numeric = this.parseToNumber(raw);
      if (!isNaN(numeric) && numeric === 0) {
        // очищаем поле и не регистрируем ноль
        this.value = '';
        input.value = '';
        this.onChange(this.value);
        return;
      }
    }

    const pattern = this.getPatternForSystem(this.numberSystem);
    if (raw === '' || pattern.test(raw)) {
      this.value = raw;
      this.onChange(this.value);
    } else {
      // недопустимый символ — откатываем видимое значение к последнему валидному
      input.value = this.value;
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (!this.forbidZero) return;
    const k = event.key;
    if (k === '0' || k === 'Numpad0') {
      const el = event.target as HTMLInputElement;
      const cur = el.value;
      if (cur === '' || cur === '-') {
        event.preventDefault();
      }
    }
  }

  onPaste(event: ClipboardEvent) {
    if (!this.forbidZero) return;
    const text = event.clipboardData?.getData('text') ?? '';
    const numeric = this.parseToNumber(text.trim());
    if (!isNaN(numeric) && numeric === 0) {
      event.preventDefault();
    }
  }

  // --- Validator interface ---

  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    return this.validateValue();
  }

  // публичный геттер для шаблона
  public get validationErrors(): ValidationErrors | null {
    return this.validateValue();
  }

  // приватная логика валидации (используется внутри класса)
  private validateValue(): ValidationErrors | null {
    if (this.value === null || this.value === '') {
      return { required: true };
    }
    const pattern = this.getPatternForSystem(this.numberSystem);
    if (!pattern.test(this.value)) {
      return { invalidNumberSystem: true };
    }
    if (this.forbidZero) {
      const num = this.parseToNumber(this.value);
      if (!isNaN(num) && num === 0) {
        return { zeroNotAllowed: true };
      }
    }
    return null;
  }

  // --- Helpers ---

  private getPatternForSystem(sys: NumberSystem): RegExp {
    switch (sys) {
      case NumberSystem.BINARY:
        return /^-?[01]+$/;
      case NumberSystem.OCTAL:
        return /^-?[0-7]+$/;
      case NumberSystem.DECIMAL:
        return /^-?\d+$/;
      case NumberSystem.HEXADECIMAL:
        return /^-?[0-9a-fA-F]+$/;
      default:
        return /^-?\d+$/;
    }
  }

  private baseForSystem(sys: NumberSystem): number {
    switch (sys) {
      case NumberSystem.BINARY:
        return 2;
      case NumberSystem.OCTAL:
        return 8;
      case NumberSystem.DECIMAL:
        return 10;
      case NumberSystem.HEXADECIMAL:
        return 16;
      default:
        return 10;
    }
  }

  private parseToNumber(val: string): number {
    if (!val || val.trim() === '') return NaN;
    const s = val.trim();
    const negative = s.startsWith('-');
    const core = negative ? s.slice(1) : s;
    const base = this.baseForSystem(this.numberSystem);
    const parsed = parseInt(core, base);
    if (isNaN(parsed)) return NaN;
    return negative ? -parsed : parsed;
  }
}
