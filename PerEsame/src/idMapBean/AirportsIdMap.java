package idMapBean;

import java.util.HashMap;
import java.util.Map;


public class AirportsIdMap {
	
private Map<Integer, Airport> map;
	
	public AirportsIdMap() {
		map = new HashMap<>();
	}
	
	public Airport get(int airportId) {
		return map.get(airportId);
	}
	
	public Airport get(Airport airport) {
		Airport old = map.get(airport.getId());
		if (old == null) {
			map.put(airport.getId(), airport);
			return airport;
		}
		return old;
	}
	
	public void put(Airport airport, int airportId) {
		map.put(airportId, airport);
	}


}
