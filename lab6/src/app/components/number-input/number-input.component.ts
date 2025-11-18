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
  @Input() disabled = false;

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
    this.disabled = isDisabled;
  }

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    this.value = input.value;
    this.onChange(this.value);
  }

  // Интерфейсный метод Validator - делегирует валидацию по значению внутреннего поля
  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    return this.validateValue();
  }

  // Вся логика валидации на основе this.value (не требует AbstractControl)
  private validateValue(): ValidationErrors | null {
    if (this.value === null || this.value === '') {
      return { required: true };
    }
    const pattern = this.getPatternForSystem(this.numberSystem);
    if (!pattern.test(this.value)) {
      return { invalidNumberSystem: true };
    }
    return null;
  }

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

  // Геттеры для шаблона (используют индексную нотацию)
  get validationErrors(): ValidationErrors | null {
    return this.validateValue();
  }

  get hasInvalidNumberSystem(): boolean {
    const e = this.validationErrors;
    return !!(e && e['invalidNumberSystem']);
  }

  get hasRequiredError(): boolean {
    const e = this.validationErrors;
    return !!(e && e['required']);
  }
}
