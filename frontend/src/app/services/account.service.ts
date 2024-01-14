import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Client} from "../models/client";
import {Observable} from "rxjs";

@Injectable({ providedIn: 'root' })
export class AccountService {
  private apiUrl = 'http://localhost:8089/account';
  private clients: Client[] = [];


  constructor(private http: HttpClient) {}

  addClient(client: Client) {
    return this.http.post(this.apiUrl, client).subscribe();
  }

  getClients() {
    return this.http.get(this.apiUrl).subscribe(response => {
      // @ts-ignore
      this.clients = response._embedded.client;
    });
  }

  fetchAccounts(client: Client): Observable<any> {
    return this.http.get(client._links.accounts.href);
  }

  addAccount(currency: string, selectedClient: Client): Observable<any> {
    const params = new HttpParams()
      .set('clientId', selectedClient.clientId)
      .set('currency', currency);

    return this.http.post(this.apiUrl + '/add', null, { params });
  }
}
