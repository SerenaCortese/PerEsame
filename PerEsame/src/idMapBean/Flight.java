package idMapBean;

import java.time.LocalDateTime;


public class Flight {
	
	private int id;
	private int airlineId;
	private int flightNumber;
	private String tailNumber;
	private int originAirportId;
	private int destinationAirportId;
	private LocalDateTime scheduledDepartureDate;
	private LocalDateTime arrivalDate;
	private double departureDelay;
	private double arrivalDelay;
	private double elapsedTime;
	private int distance;
	
	public Flight(int id, int airlineId, int flightNumber, String tailNumber, int originAirportId,
			int destinationAirportId, LocalDateTime scheduledDepartureDate, LocalDateTime arrivalDate,
			double departureDelay, double arrivalDelay, double elapsedTime, int distance) {
		super();
		this.id = id;
		this.airlineId = airlineId;
		this.flightNumber = flightNumber;
		this.tailNumber = tailNumber;
		this.originAirportId = originAirportId;
		this.destinationAirportId = destinationAirportId;
		this.scheduledDepartureDate = scheduledDepartureDate;
		this.arrivalDate = arrivalDate;
		this.departureDelay = departureDelay;
		this.arrivalDelay = arrivalDelay;
		this.elapsedTime = elapsedTime;
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(int airlineId) {
		this.airlineId = airlineId;
	}

	public int getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getTailNumber() {
		return tailNumber;
	}

	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}

	public int getOriginAirportId() {
		return originAirportId;
	}

	public void setOriginAirportId(int originAirportId) {
		this.originAirportId = originAirportId;
	}

	public int getDestinationAirportId() {
		return destinationAirportId;
	}

	public void setDestinationAirportId(int destinationAirportId) {
		this.destinationAirportId = destinationAirportId;
	}

	public LocalDateTime getScheduledDepartureDate() {
		return scheduledDepartureDate;
	}

	public void setScheduledDepartureDate(LocalDateTime scheduledDepartureDate) {
		this.scheduledDepartureDate = scheduledDepartureDate;
	}

	public LocalDateTime getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDateTime arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public double getDepartureDelay() {
		return departureDelay;
	}

	public void setDepartureDelay(double departureDelay) {
		this.departureDelay = departureDelay;
	}

	public double getArrivalDelay() {
		return arrivalDelay;
	}

	public void setArrivalDelay(double arrivalDelay) {
		this.arrivalDelay = arrivalDelay;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Flight (");
		builder.append(id);
		builder.append(") airlineId=");
		builder.append(airlineId);
		builder.append(", flightNumber=");
		builder.append(flightNumber);
		builder.append(", originAirportId=");
		builder.append(originAirportId);
		builder.append(", destinationAirportId=");
		builder.append(destinationAirportId);
		builder.append(", scheduledDepartureDate=");
		builder.append(scheduledDepartureDate);
		builder.append(", arrivalDate=");
		builder.append(arrivalDate);
		builder.append(", departureDelay=");
		builder.append(departureDelay);
		builder.append(", arrivalDelay=");
		builder.append(arrivalDelay);
		builder.append(", airTime=");
		builder.append(elapsedTime);
		builder.append(", distance=");
		builder.append(distance);
		return builder.toString();
	}


}
