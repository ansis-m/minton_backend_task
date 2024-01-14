import { Component } from '@angular/core';
import {ClientService} from "./services/client.service";
import {NgForm} from "@angular/forms";
import {Client} from "./models/client";
import {AccountService} from "./services/account.service";
import {Account} from "./models/account";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  selectedClient: Client | undefined;
  accounts: Account[] = [];
  selectedAccount: Account | undefined;

  constructor(private clientService: ClientService,
              private accountService: AccountService) {}

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

  onClientChange(client: Client | undefined) {
    if(!client) {
      return;
    }
    this.selectedAccount = undefined;
    this.accountService.fetchAccounts(client).subscribe(response => this.accounts = response._embedded.account);
  }

  addAccount(form: NgForm) {
    if(!this.selectedClient) {
      return;
    }
    this.accountService.addAccount(form.value.currency, this.selectedClient).subscribe({
      next: (response) => {
        this.onClientChange(this.selectedClient);
      },
      error: error => {
        console.error(error);
      }
    });
  }
}
