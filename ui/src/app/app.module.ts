import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {LightHttpService} from "./service/light-http.service";
import {NgbAlertModule, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {VisualGraphCalculatorComponent} from './visual-graph-calculator/visual-graph-calculator.component';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    VisualGraphCalculatorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule,
    NgbAlertModule,
    FormsModule
  ],
  providers: [LightHttpService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
