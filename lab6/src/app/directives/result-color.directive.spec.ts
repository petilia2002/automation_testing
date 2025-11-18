// src/app/directives/result-color.directive.spec.ts
import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ResultColorDirective } from './result-color.directive';

@Component({
  template: `<div [appResultColor]="testValue">Test Element</div>`,
  standalone: true,
  imports: [ResultColorDirective],
})
class TestComponent {
  testValue: string | number | null = null;
}

describe('ResultColorDirective', () => {
  let fixture: ComponentFixture<TestComponent>;
  let component: TestComponent;
  let divElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    divElement = fixture.debugElement.query(By.css('div'))
      .nativeElement as HTMLElement;
  });

  /* проверь, что Angular привязал директиву к элементу в шаблоне и
  создал рабочий экземпляр этой директивы в DI-контексте этого элемента */
  it('should create directive (via injector)', () => {
    const debugDir = fixture.debugElement.query(
      By.directive(ResultColorDirective)
    );
    expect(debugDir).toBeTruthy();
    const directiveInstance = debugDir.injector.get(ResultColorDirective);
    expect(directiveInstance).toBeTruthy();
  });

  function computedColor(): string {
    return getComputedStyle(divElement).color;
  }

  it('should set black color for null value', () => {
    component.testValue = null;
    fixture.detectChanges();
    const color = computedColor();
    // black -> rgb(0, 0, 0) or rgba(0, 0, 0, 1)
    expect(color).toMatch(/0,\s*0,\s*0/);
  });

  it('should set black color for empty string', () => {
    component.testValue = '';
    fixture.detectChanges();
    const color = computedColor();
    expect(color).toMatch(/0,\s*0,\s*0/);
  });

  it('should set red color for negative numbers', () => {
    component.testValue = -5;
    fixture.detectChanges();
    const color = computedColor();
    // red -> rgb(255, 0, 0)
    expect(color).toMatch(/255,\s*0,\s*0/);
  });

  it('should set black color for zero', () => {
    component.testValue = 0;
    fixture.detectChanges();
    const color = computedColor();
    expect(color).toMatch(/0,\s*0,\s*0/);
  });

  it('should set green color for positive numbers', () => {
    component.testValue = 5;
    fixture.detectChanges();
    const color = computedColor();
    // green обычно "rgb(0, 128, 0)" — проверяем наличие "0, 128, 0"
    expect(color).toMatch(/0,\s*128,\s*0/);
  });

  it('should set green color for positive numbers', () => {
    component.testValue = 5;
    fixture.detectChanges();
    expect(divElement.style.color).toBe('green');
  });

  it('should set black color for empty string', () => {
    component.testValue = '';
    fixture.detectChanges();
    expect(divElement.style.color).toBe('');
  });
});
