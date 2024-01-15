import { Component } from '@angular/core';
import {ClientService} from "./services/client.service";
import {NgForm} from "@angular/forms";
import {Client} from "./models/client";
import {AccountService} from "./services/account.service";
import {Account} from "./models/account";
import {Transaction} from "./models/transaction";
import {Transfer} from "./models/transfer";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  selectedClient: Client | undefined;
  accounts: Account[] = [];
  selectedAccount: Account | undefined;
  transactions: Transaction[] = [];

  columns: string[] = ['id', 'amount', 'transaction type', 'id of the target account', 'amount for the target account', 'conversion rate', 'date'];



  offset: number = 0;
  limit: number = 10;
  targetClient: Client | undefined;
  targetAccounts: Account[] = [];
  targetAccount: Account | undefined;

  constructor(private clientService: ClientService,
              private accountService: AccountService) {}

  onRegister(form: NgForm) {
    if (form.invalid) return;
    this.clientService.addClient(form.value).subscribe({
      next: (response) => {
        form.resetForm();
      },
      error: (error) => {

      }
    });
  }

  get targetClients() {
    return this.clients.filter(client => client.clientId !== this.selectedClient?.clientId);
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
        this.transactions = response;
      },
      error: (error) => {

      }
    });

  }

  getAmount(element: Transaction) {

    if (element?.accountTo?.accountId === this.selectedAccount?.accountId) {
      return element.amountTo;
    }
    return element.amountFrom;
  }

  getType(element: Transaction) {
    if (element.amountFrom == null || element.accountTo == null) {
      if (this.selectedAccount?.accountId == element.accountTo?.accountId){
        return "addition of funds";
      }
      return "withdrawal";
    }

    return 'transfer';

  }

  getTargetAccount(element: Transaction) {
    return element.accountTo?.accountId;

  }

  onTargetClientChange(client: Client) {
    if(!client) {
      return;
    }
    this.accountService.fetchAccounts(client).subscribe(response => {
      this.targetAccounts = response._embedded.account
      this.targetAccounts.forEach(account => {
        if (account?.accountId === this.targetAccount?.accountId) {
          this.targetAccount = account;
        }
      })
    });

  }

  transfer(form: NgForm) {
    if (!form.value.amount || !this.selectedAccount?.accountId || !this.targetAccount?.accountId) {
      return;
    }
    let transfer = new Transfer(this.selectedAccount?.accountId, this.targetAccount?.accountId, form.value.amount)

    this.accountService.transfer(transfer).subscribe({
      next: (result: any) => {
        console.log(result)
      },
      error: (error: any) => {
        console.log(error);
      }
    })

  }
}
