import {Component, OnInit} from '@angular/core';
import {ClientService} from "./services/client.service";
import {NgForm} from "@angular/forms";
import {Client} from "./models/client";
import {AccountService} from "./services/account.service";
import {Transaction} from "./models/transaction";
import {Transfer} from "./models/transfer";
import {CurrencyService} from "./services/currency.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  selectedClient: Client | undefined;
  transactions: Transaction[] = [];

  columns: string[] = ['id', 'amount', 'transaction type', 'id of the target account', 'amount for the target account', 'conversion rate', 'date'];

  offset: number = 0;
  limit: number = 10;
  targetClient: Client | undefined;

  currencies: Map<string, string> = new Map();

  constructor(private clientService: ClientService,
              private accountService: AccountService,
              private currencyService: CurrencyService) {}


  ngOnInit(): void {
    this.fetchCurrencies();
  }


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

  get targetAccounts() {
    return this.targetClient?.accounts;
  }

  getClients() {
    this.clientService.getClients();
  }

  get clients(): Client[]{
    let list = this.clientService.getClientList();
    list.forEach((client, index) => {
      if (client.clientId === this.selectedClient?.clientId) {
         list[index] = this.selectedClient;
      }
    })
    return list;
  }

  getAccounts() {

  }

  onClientChange(client: Client) {
    this.transactions = [];
    this.fetchAccounts(client);
  }

  fetchAccounts(client: Client | undefined) {
    if (!client) {
      return;
    }
    return this.accountService.fetchAccounts(client).subscribe({
      next: (response) => {
        client.accounts = response._embedded.account
        client.accounts.forEach(account => {
          if (account?.accountId === client.selectedAccount?.accountId) {
            client.selectedAccount = account;
          }
        })
      },
      error: (error) => {
      }
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

    if (!this.selectedAccount || this.selectedAccount.amount + amount < 0) {
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

  get   accounts(){
    return this.selectedClient?.accounts;
  }

  get selectedAccount() {
    return this.selectedClient?.selectedAccount;
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

    if (this.selectedClient?.selectedAccount?.accountId === element.accountFrom?.accountId) {
      return 'outgoing transfer';
    }

    return 'incoming transfer';
  }

  getOtherAccount(element: Transaction) {
    switch (this.getType(element)) {
      case "addition of funds":
      case "withdrawal":
        return 'N/A';
      case "incoming transfer":
        return element.accountFrom?.accountId;
      default:
        return element.accountTo?.accountId;
    }
  }

  get targetAccount(){
    return this.targetClient?.selectedAccount;
  }

  transfer(form: NgForm) {
    if (!form.value.amount || !this.selectedAccount?.accountId || !this.targetAccount?.accountId) {
      return;
    }
    let transfer = new Transfer(this.selectedAccount?.accountId, this.targetAccount?.accountId, form.value.amount)

    this.accountService.transfer(transfer).subscribe({
      next: (result: any) => {
        console.log(result);
        this.fetchAccounts(this.selectedClient);
        this.fetchAccounts(this.targetClient);
      },
      error: (error: any) => {
        console.log(error);
      }
    })

  }

  getTargetAmount(element: Transaction) {

    if(!this.getType(element).includes("transfer")){
      return 'N/A';
    }
    if (element?.accountTo?.accountId === this.selectedAccount?.accountId) {
      return element.amountFrom;
    }
    return element.amountTo;
  }

  private fetchCurrencies() {
    this.currencyService.fetchCurrencies().subscribe({
      next: (result) => {

        for (let key in result) {
          if (result.hasOwnProperty(key)) {
            // @ts-ignore
            this.currencies.set(key, result[key]);
          }
        }
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
