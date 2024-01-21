import {Component, OnInit} from '@angular/core';
import {ClientService} from "./services/client.service";
import {FormControl, FormGroup, NgForm, ɵFormGroupValue, ɵTypedOrUntyped} from "@angular/forms";
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

  formGroup = new FormGroup({
    selectedCurrency: new FormControl('')
  });

  columns: string[] = ['id', 'amount', 'transaction type', 'id of the target account', 'amount for the target account', 'conversion rate', 'currency', 'date'];

  offset: number = 0;
  limit: number = 10;
  targetClient: Client | undefined;

  currencies: Map<string, string> = new Map();
  currencyEntries: [string, string][] = [];
  transferCurrency: string | undefined;

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
    let targetClients =  this.clients.filter(client => client.id !== this.selectedClient?.id);
    if (this.targetClient && !targetClients.includes(this.targetClient)) {
      this.targetClient = undefined;
    }
    return targetClients;
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
      if (client.id === this.selectedClient?.id) {
         list[index] = this.selectedClient;
      }
    })
    return list;
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
        client.accounts = response.content
        client.accounts.forEach(account => {
          if (account?.id === client.selectedAccount?.id) {
            client.selectedAccount = account;
          }
        })
      },
      error: (error) => {
      }
    });
  }

  addAccount(form: any) {

    if(!this.selectedClient) {
      return;
    }
    this.accountService.addAccount(form.selectedCurrency, this.selectedClient).subscribe({
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
    this.accountService.addFunds(this.selectedAccount, amount, form.value.selectedCurrency).subscribe({
      next: (response) => {
        this.fetchAccounts(this.selectedClient);
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  get accounts(){
    return this.selectedClient?.accounts;
  }

  get selectedAccount() {
    return this.selectedClient?.selectedAccount;
  }

  getHistory() {
    this.accountService.getTransactions(this.limit, this.offset, this.selectedAccount).subscribe({
      next: (response) => {
        this.transactions = response;
      },
      error: (error) => {
        console.error(error);
      }
    });

  }

  getAmount(element: Transaction) {

    if (element?.accountTo?.id === this.selectedAccount?.id) {
      return element.amountTo;
    }
    return element.amountFrom;
  }

  getType(element: Transaction) {
    if (element.amountFrom == null || element.accountTo == null) {
      if (this.selectedAccount?.id == element.accountTo?.id){
        return "addition of funds";
      }
      return "withdrawal";
    }

    if (this.selectedClient?.selectedAccount?.id === element.accountFrom?.id) {
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
        return element.accountFrom?.id;
      default:
        return element.accountTo?.id;
    }
  }

  get targetAccount(){
    return this.targetClient?.selectedAccount;
  }

  transfer(form: NgForm) {

    if (!form.value.amount || !this.selectedAccount?.id || !this.targetAccount?.id || !this.transferCurrency) {
      return;
    }
    let transfer = new Transfer(this.selectedAccount?.id, this.targetAccount?.id, form.value.amount, this.transferCurrency)
    this.accountService.transfer(transfer).subscribe({
      next: (result: any) => {
        this.fetchAccounts(this.selectedClient);
        this.fetchAccounts(this.targetClient);
      },
      error: (error: any) => {
        console.error(error);
      }
    })

  }

  getTargetAmount(element: Transaction) {

    if(!this.getType(element).includes("transfer")){
      return 'N/A';
    }
    if (element?.accountTo?.id === this.selectedAccount?.id) {
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
            this.currencyEntries = Array.from(this.currencies.entries());
          }
        }
      },
      error: (error: any) => {
        console.error(error);
      }
    });
  }
}
