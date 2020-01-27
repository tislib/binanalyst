import {Component} from '@angular/core';
import {LightHttpService} from "./service/light-http.service";
import {Calculator} from "./model/calculator";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private allData: { [key: string]: Calculator };
  private names: string[];
  private selectedCalculator: string;

  get data() {
    if (this.allData && this.selectedCalculator) {
      return this.allData[this.selectedCalculator];
    } else {
      return undefined;
    }
  }

  constructor(private service: LightHttpService) {
    service.getData().subscribe(resp => {
      this.allData = resp;
      this.names = Object.keys(this.allData).sort();
    });
  }
}
