import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { NumberInputComponent } from './number-input.component';
import { NumberSystem } from '../../models/enums';
import { By } from '@angular/platform-browser';

describe('NumberInputComponent', () => {
  let component: NumberInputComponent;
  let fixture: ComponentFixture<NumberInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NumberInputComponent, ReactiveFormsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(NumberInputComponent);
    component = fixture.componentInstance;
    component.label = 'Test Input';
    component.numberSystem = NumberSystem.DECIMAL;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display label correctly', () => {
    const labelElement = fixture.debugElement.query(By.css('label'));
    expect(labelElement.nativeElement.textContent).toContain('Test Input');
  });

  it('should implement ControlValueAccessor methods', () => {
    const testValue = '123';

    component.writeValue(testValue);
    expect(component.value).toBe(testValue);

    let onChangeValue: any;
    component.registerOnChange((value: any) => (onChangeValue = value));
    component.onChange(testValue);
    expect(onChangeValue).toBe(testValue);

    let touched = false;
    component.registerOnTouched(() => (touched = true));
    component.onTouched();
    expect(touched).toBeTrue();
  });

  it('should validate decimal numbers correctly', () => {
    component.numberSystem = NumberSystem.DECIMAL;

    component.value = '123';
    expect(component.validationErrors).toBeNull();

    component.value = '-456';
    expect(component.validationErrors).toBeNull();

    component.value = '12a3';
    expect(component.validationErrors).toEqual({ invalidNumberSystem: true });
  });

  it('should validate binary numbers correctly', () => {
    component.numberSystem = NumberSystem.BINARY;

    component.value = '1010';
    expect(component.validationErrors).toBeNull();

    component.value = '1020';
    expect(component.validationErrors).toEqual({ invalidNumberSystem: true });
  });

  it('should validate hexadecimal numbers correctly', () => {
    component.numberSystem = NumberSystem.HEXADECIMAL;

    component.value = '1A3F';
    expect(component.validationErrors).toBeNull();

    component.value = '1G3F';
    expect(component.validationErrors).toEqual({ invalidNumberSystem: true });
  });

  it('should handle zero validation when forbidZero is true', () => {
    component.forbidZero = true;
    component.numberSystem = NumberSystem.DECIMAL;

    component.value = '0';
    expect(component.validationErrors).toEqual({ zeroNotAllowed: true });

    component.value = '123';
    expect(component.validationErrors).toBeNull();
  });

  it('should handle required validation', () => {
    component.value = '';
    expect(component.validationErrors).toEqual({ required: true });
  });

  it('should process input events correctly', () => {
    const inputElement = fixture.debugElement.query(
      By.css('input')
    ).nativeElement;
    expect(inputElement).toBeTruthy();
    const event = { target: { value: '123' } } as unknown as Event;

    component.onInput(event);
    expect(component.value).toBe('123');
  });

  it('should prevent zero input when forbidZero is true', () => {
    component.forbidZero = true;
    const inputElement = fixture.debugElement.query(
      By.css('input')
    ).nativeElement;
    expect(inputElement).toBeTruthy();

    const event = { target: { value: '0' } } as unknown as Event;
    component.onInput(event);
    expect(component.value).toBe('');
  });

  it('should show validation errors in template', () => {
    component.value = 'abc';
    component.numberSystem = NumberSystem.DECIMAL;
    fixture.detectChanges();

    const errorElement = fixture.debugElement.query(By.css('.error'));
    expect(errorElement).toBeTruthy();
  });
});
