import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CalculateRequest {
  firstNumber: string;
  firstNumberSystem: string;
  secondNumber: string;
  secondNumberSystem: string;
  operationType: string;
}

export interface CalculateResponse {
  result: string;
  calculationId: number;
}

@Injectable({
  providedIn: 'root',
})
export class CalculatorService {
  private baseUrl = 'http://localhost:8080/api/calculator';

  constructor(private http: HttpClient) {}

  calculate(payload: CalculateRequest): Observable<CalculateResponse> {
    return this.http.post<CalculateResponse>(
      `${this.baseUrl}/calculate`,
      payload
    );
  }

  history(params: any): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/history`, { params });
  }
}
