import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Client} from "../models/client";
import {Observable} from "rxjs";
import {Account} from "../models/account";
import {Transfer} from "../models/transfer";

@Injectable({ providedIn: 'root' })
export class AccountService {
  private apiUrl = 'http://localhost:8089/account';
  private clients: Client[] = [];


  constructor(private http: HttpClient) {}

  fetchAccounts(client: Client): Observable<any> {
    const body = {"clientId": client.clientId, "page": 0, "size": 100};
    return this.http.post(this.apiUrl, body);
  }

  addAccount(currency: string, selectedClient: Client): Observable<any> {
    const body =
      {'clientId': selectedClient.clientId, currency};

    return this.http.post(this.apiUrl + '/create', body);
  }

  addFunds(selectedAccount: Account, amount: number, currency: string): Observable<any> {
    const body = {
      'accountId': selectedAccount.accountId,
      'amount': amount,
      'currency': currency
    };

    return this.http.post(this.apiUrl + '/add', body);
  }

  getTransactions(limit: number, offset: number, selectedAccount: Account | undefined): Observable<any> {
    const params = new HttpParams()
      // @ts-ignore
      .set('accountId', selectedAccount.accountId)
      .set('limit', limit)
      .set('offset', offset)

    return this.http.get(this.apiUrl + '/transactions', { params });
  }

  transfer(transfer: Transfer) {
    return this.http.post(this.apiUrl + '/transfer', transfer);
  }
}
