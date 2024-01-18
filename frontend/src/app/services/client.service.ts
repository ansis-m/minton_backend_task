import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Client} from "../models/client";

@Injectable({ providedIn: 'root' })
export class ClientService {
  private apiUrl = 'http://localhost:8089/client';
  private clients: Client[] = [];


  constructor(private http: HttpClient) {}

  addClient(client: Client) {
    return this.http.post(this.apiUrl, client);
  }

  getClients() {
    return this.http.get(this.apiUrl).subscribe(response => {
      // @ts-ignore
      this.clients = response.content;
    });
  }

  getClientList() {
    return this.clients;
  }
}
