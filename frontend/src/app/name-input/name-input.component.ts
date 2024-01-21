import {Component, EventEmitter, Output} from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-name-input',
  templateUrl: './name-input.component.html',
  styleUrls: ['./name-input.component.css']
})
export class NameInputComponent {


  @Output()
  onRegister: EventEmitter<NgForm> = new EventEmitter<any>();

  onSubmit(form: NgForm) {
    this.onRegister.emit(form);
  }
}
