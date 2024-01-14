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

  offset: number = 0;
  limit: number = 0;

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

  onClientChange(client: Client) {
    this.selectedAccount = undefined;
    this.fetchAccounts(client);
  }

  fetchAccounts(client: Client | undefined){
    if(!client) {
      return;
    }
    this.accountService.fetchAccounts(client).subscribe(response => {
      this.accounts = response._embedded.account
      this.accounts.forEach(account => {
        if (account?.accountId === this.selectedAccount?.accountId) {
          this.selectedAccount = account;
        }
      })
    });
  }

  addAccount(form: NgForm) {
    if(!this.selectedClient) {
      return;
    }
    this.accountService.addAccount(form.value.currency, this.selectedClient).subscribe({
      next: (response) => {
        this.fetchAccounts(this.selectedClient);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  addToAccount(form: NgForm, withdraw: boolean = false) {
    let amount = form.value.amount * (withdraw? -1 : 1);

    if (!this.selectedAccount || this.selectedAccount?.amount + amount < 0) {
      return;
    }
    this.accountService.addFunds(this.selectedAccount, amount).subscribe({
      next: (response) => {
        this.fetchAccounts(this.selectedClient);
      },
      error: (error) => {

      }
    });


  }

  getHistory() {
    console.log(this.limit + "  " + this.offset);

    this.accountService.getTransactions(this.limit, this.offset, this.selectedAccount).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {

      }
    });

  }
}
