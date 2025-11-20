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

  it('should create an instance', () => {
    const debugDir = fixture.debugElement.query(
      By.directive(ResultColorDirective)
    );
    expect(debugDir).toBeTruthy();
    const directiveInstance = debugDir.injector.get(ResultColorDirective);
    expect(directiveInstance).toBeTruthy();
  });

  it('should set black color for null value', () => {
    component.testValue = null;
    fixture.detectChanges();
    expect(divElement.style.color).toBe('');
  });

  it('should set black color for empty string', () => {
    component.testValue = '';
    fixture.detectChanges();
    expect(divElement.style.color).toBe('');
  });

  it('should set red color for negative numbers', () => {
    component.testValue = -5;
    fixture.detectChanges();
    expect(divElement.style.color).toBe('red');
  });

  it('should set black color for zero', () => {
    component.testValue = 0;
    fixture.detectChanges();
    expect(divElement.style.color).toBe('black');
  });

  it('should set green color for positive numbers', () => {
    component.testValue = 5;
    fixture.detectChanges();
    expect(divElement.style.color).toBe('green');
  });
});
