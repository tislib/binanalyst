import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Calculator} from "../model/calculator";
import {Observable} from "rxjs";

@Injectable()
export class LightHttpService {

  constructor(private http: HttpClient) {
  }

  getData(): Observable<{[key: string]: Calculator}> {
    return this.http.get<{[key: string]: Calculator}>('/api/data');
  }
}
