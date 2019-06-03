import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Flightdetails } from './model/flightdetails.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class FlightserviceService {

  constructor(private http: HttpClient) {}

  private userUrl = 'http://localhost:8080/checkFlights';

  public searchFlights(flightSearch) {
    // tslint:disable-next-line:max-line-length
    let params = new HttpParams();
    params = params.append('inputDateStr', flightSearch.inputDateStr);
    params = params.append('flightNumber', flightSearch.flightNumber);
    params = params.append('origin', flightSearch.origin);
    params = params.append('destination', flightSearch.destination);
    return this.http.get<Flightdetails[]>(this.userUrl, {params: params});
  }


}




