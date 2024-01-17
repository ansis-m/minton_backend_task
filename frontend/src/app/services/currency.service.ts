import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({ providedIn: 'root' })
export class CurrencyService {
  private apiUrl = 'http://localhost:8089/currencies';

  constructor(private http: HttpClient) {}

  fetchCurrencies(): Observable<Map<string, string>> {
    // @ts-ignore
    return this.http.get(this.apiUrl);
  }
}
