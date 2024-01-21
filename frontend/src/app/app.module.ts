import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatButtonModule} from "@angular/material/button";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatInputModule} from "@angular/material/input";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatTableModule} from "@angular/material/table";
import { NameInputComponent } from './name-input/name-input.component';
import { RegisterAccountComponent } from './register-account/register-account.component';

@NgModule({
  declarations: [
    AppComponent,
    NameInputComponent,
    RegisterAccountComponent
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        MatExpansionModule,
        MatButtonModule,
        BrowserAnimationsModule,
        MatInputModule,
        MatOptionModule,
        MatSelectModule,
        MatTableModule,
        ReactiveFormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
