package com.example.flightsearch.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.flightsearch.vo.FlightSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FlightSearchController {

	@GetMapping(value = "/checkFlights")
	public List<FlightSchedule> getSystem(@RequestParam(name = "flightNumber", required = false) String flightNumber,
			@RequestParam(name = "origin", required = false) String origin,
			@RequestParam(name = "destination", required = false) String destination,
			@RequestParam(name = "inputDateStr", required = false) String inputDateStr) {

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date inputDate = null;
		try {
			inputDate = simpleDateFormat.parse(inputDateStr);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar inputCalendar = Calendar.getInstance();
		inputCalendar.setTime(inputDate);

		List<FlightSchedule> results = new ArrayList<>();
		for (FlightSchedule flightSchedule : loadFlightSchedules()) {
			Calendar departureCalendar = Calendar.getInstance();
			departureCalendar.setTime(flightSchedule.getDeparture());
			TimeIgnoringComparator timeIgnoringComparator = new TimeIgnoringComparator();
			if (timeIgnoringComparator.compare(inputCalendar, departureCalendar) == 0) {
				if (flightNumber != null) {
					if (flightSchedule.getFlightNumber().equals(flightNumber)) {
						results.add(flightSchedule);
					}
				}
				if (origin != null && destination != null) {
					if (flightSchedule.getOrigin().equalsIgnoreCase(origin)
							&& flightSchedule.getDestination().equals(destination)) {
						results.add(flightSchedule);
					}
				}
			}
		}
		return results;
	}

	private FlightSchedule[] loadFlightSchedules() {
		ObjectMapper mapper = new ObjectMapper();
		FlightSchedule[] flightSchedules = null;
		try {
			flightSchedules = mapper.readValue(ResourceUtils.getFile("classpath:flight-sample.json"),
					FlightSchedule[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flightSchedules;
	}

}

class TimeIgnoringComparator implements Comparator<Calendar> {
	public int compare(Calendar c1, Calendar c2) {
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
			return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
			return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
	}
}
