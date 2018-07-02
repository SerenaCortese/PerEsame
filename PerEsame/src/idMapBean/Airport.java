package idMapBean;

import java.util.ArrayList;
import java.util.List;


public class Airport {

	private int id;
	private String iataCode;
	private String name;
	private String city;
	private String state;
	private String country;
	private double latitude;
	private double longitude;
	private double timezoneOffset;
	
	private List<Flight> voliPartenza;
	
	private int pipol;

	
	
	public Airport(int id, String iataCode, String name, String city, String state, String country, double latitude,
			double longitude, double timezoneOffset) {
		super();
		this.id = id;
		this.iataCode = iataCode;
		this.name = name;
		this.city = city;
		this.state = state;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timezoneOffset = timezoneOffset;
		voliPartenza = new ArrayList<>();
		pipol=0;
	}

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getIataCode() {
		return iataCode;
	}


	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getTimezoneOffset() {
		return timezoneOffset;
	}


	public void setTimezoneOffset(double timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}


	public List<Flight> getVoliPartenza() {
		return voliPartenza;
	}


	public void setVoliPartenza(List<Flight> voliPartenza) {
		this.voliPartenza = voliPartenza;
	}
	
	public void setPipol(int p) {
		this.pipol =p;
	}
	
	public int getPipol() {
		return this.pipol;
	}
	
	public void reSetPipo() {
		this.pipol =0;
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
		Airport other = (Airport) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Airport (");
		builder.append(id);
		builder.append(") ");
		builder.append(name);
		return builder.toString();
	}

}


