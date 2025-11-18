import { TestBed } from '@angular/core/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { CalculatorService, CalculateRequest } from './calculator.service';
import { NumberSystem, OperationType } from '../models/enums';

describe('CalculatorService', () => {
  let service: CalculatorService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CalculatorService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(CalculatorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send calculation request and return response', () => {
    const mockRequest: CalculateRequest = {
      firstNumber: '10',
      firstNumberSystem: NumberSystem.DECIMAL,
      secondNumber: '5',
      secondNumberSystem: NumberSystem.DECIMAL,
      operationType: OperationType.ADD,
    };

    service.calculate(mockRequest).subscribe((response) => {
      expect(response.result).toBe('15');
      expect(typeof response.calculationId).toBe('number');
      expect(response.calculationId).toBeGreaterThan(0);
    });

    const req = httpMock.expectOne(
      'http://localhost:8080/api/calculator/calculate'
    );
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);

    req.flush({
      result: '15',
      calculationId: 12345,
    });
  });

  it('should send GET request for history', () => {
    const params = {
      startDate: '2025-10-10T10:00:00',
      endDate: '2025-10-10T10:20:00',
      operationType: 'MULTIPLY',
    };

    service.history(params).subscribe();

    // Просто проверяем что есть любой запрос к history endpoint
    const req = httpMock.expectOne((request) =>
      request.url.includes('/api/calculator/history')
    );
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should complete successfully', () => {
    const params = {
      startDate: '2025-10-10T10:00:00',
      endDate: '2025-10-10T10:20:00',
    };

    service.history(params).subscribe({
      next: (response) => {
        expect(response).toBeDefined();
      },
      error: (error) => {
        fail('Should not throw error');
      },
    });

    const req = httpMock.expectOne((request) =>
      request.url.includes('/api/calculator/history')
    );
    req.flush([]);
  });
});
