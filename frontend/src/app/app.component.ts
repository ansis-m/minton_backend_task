import { Component } from '@angular/core';
import {ClientService} from "./services/client.service";
import {NgForm} from "@angular/forms";
import {Client} from "./models/client";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  selectedClient: Client | undefined;

  constructor(private clientService: ClientService) {}

  onRegister(form: NgForm) {
    if (form.invalid) return;
    this.clientService.addClient(form.value).subscribe(response => {
    });
  }

  getClients() {
    this.clientService.getClients();
  }

  get clients(): Client[]{
    let list = this.clientService.getClientList();
    list.forEach(client => {
      if (client.clientId === this.selectedClient?.clientId) {
        this.selectedClient = client;
      }
    })
    return list;
  }

  getAccounts() {

  }
}
