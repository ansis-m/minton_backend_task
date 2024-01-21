import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-register-account',
  templateUrl: './register-account.component.html',
  styleUrls: ['./register-account.component.css']
})
export class RegisterAccountComponent {

  @Input()
  currencyEntries: any;


  @Output()
  addAccount = new EventEmitter();

  formGroup = new FormGroup({
    selectedCurrency: new FormControl('')
  });


  add(value: any) {
    console.log(JSON.stringify(value));
    this.addAccount.emit(value);
  }
}
