package idMapBean;

import java.util.HashMap;
import java.util.Map;


public class AirlinesIdMap {
	
private Map<Integer, Airline> map;
	
	public AirlinesIdMap() {
		map = new HashMap<>();
	}
	
	public Airline get(int airlineId) {
		return map.get(airlineId);
	}
	
	public Airline get(Airline airline) {
		Airline old = map.get(airline.getId());
		if (old == null) {
			map.put(airline.getId(), airline);
			return airline;
		}
		return old;
	}
	
	public void put(Airline airline, int airlineId) {
		map.put(airlineId, airline);
	}


}
