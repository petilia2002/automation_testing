import { PrecisionPipe } from './precision.pipe';

describe('PrecisionPipe', () => {
  let pipe: PrecisionPipe;

  beforeEach(() => {
    pipe = new PrecisionPipe();
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return empty string for null value', () => {
    expect(pipe.transform(null)).toBe('');
  });

  it('should return empty string for undefined value', () => {
    expect(pipe.transform(undefined)).toBe('');
  });

  it('should return empty string for empty string', () => {
    expect(pipe.transform('')).toBe('');
  });

  it('should format number with default 2 decimals', () => {
    expect(pipe.transform(123.456)).toBe('123.46');
  });

  it('should format number with specified decimals', () => {
    expect(pipe.transform(123.456, 1)).toBe('123.5');
    expect(pipe.transform(123.456, 3)).toBe('123.456');
    expect(pipe.transform(123.456, 0)).toBe('123');
  });

  it('should handle negative decimals by using 0', () => {
    expect(pipe.transform(123.456, -2)).toBe('123');
  });

  it('should handle string numbers', () => {
    expect(pipe.transform('123.456', 2)).toBe('123.46');
  });

  it('should return original string for non-numeric values', () => {
    expect(pipe.transform('abc')).toBe('abc');
  });

  it('should handle zero value', () => {
    expect(pipe.transform(0)).toBe('0.00');
    expect(pipe.transform(0, 0)).toBe('0');
  });

  it('should handle integer numbers', () => {
    expect(pipe.transform(123)).toBe('123.00');
    expect(pipe.transform(123, 0)).toBe('123');
  });
});
