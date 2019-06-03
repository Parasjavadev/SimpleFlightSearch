import { Component, OnInit } from '@angular/core';
import { FlightSearch } from '../model/flightsearch.model';
import { Flightdetails } from '../model/flightdetails.model';
import { FlightserviceService } from '../flightservice.service';

@Component({
  selector: 'app-flightsearch',
  templateUrl: './flightsearch.component.html',
  styleUrls: ['./flightsearch.component.css']
})
export class FlightsearchComponent implements OnInit {
  flightSearch: FlightSearch = new FlightSearch();
  flightdetailsList: Flightdetails[];
  constructor(private flightservice: FlightserviceService) { }

  ngOnInit() {
  }

  searchFlight(): void {
    this.flightservice.searchFlights(this.flightSearch)
    .subscribe( data => {
      this.flightdetailsList = data;
    });
  }

}
