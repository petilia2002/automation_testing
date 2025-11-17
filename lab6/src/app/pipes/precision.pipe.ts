import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'precision',
  standalone: true
})
export class PrecisionPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
