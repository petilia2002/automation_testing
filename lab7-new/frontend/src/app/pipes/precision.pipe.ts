import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'precision',
  standalone: true,
})
export class PrecisionPipe implements PipeTransform {
  transform(
    value: string | number | null | undefined,
    decimals: number = 2
  ): string {
    if (value === null || value === undefined || value === '') return '';
    const num = Number(value);
    if (isNaN(num)) return String(value);
    // Use toFixed for rounding. If decimals < 0, fallback to 0
    const d = Math.max(0, Math.floor(decimals));
    return num.toFixed(d);
  }
}
