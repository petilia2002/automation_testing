import {
  Directive,
  ElementRef,
  Input,
  OnChanges,
  Renderer2,
  SimpleChanges,
} from '@angular/core';

@Directive({
  selector: '[appResultColor]',
  standalone: true,
})
export class ResultColorDirective implements OnChanges {
  @Input('appResultColor') value: string | number | null = null;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnChanges(changes: SimpleChanges): void {
    this.applyColor();
  }

  private applyColor() {
    if (this.value === null || this.value === undefined || this.value === '') {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
      return;
    }
    const num = Number(this.value);
    if (isNaN(num)) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
      return;
    }
    if (num < 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'red');
    } else if (num === 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
    } else {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'green');
    }
  }
}
