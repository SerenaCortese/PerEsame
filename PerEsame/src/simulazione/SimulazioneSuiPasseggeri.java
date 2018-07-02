package simulazione;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import idMapBean.Airport;
import idMapBean.AirportsIdMap;
import idMapBean.Flight;
import querySQL.DAO;

/*NEL CONTROLLER
 * @FXML
    void doSimula(ActionEvent event) {
    	try {
    		int K = Integer.parseInt(this.numeroPasseggeriTxtInput.getText());
    		int V = Integer.parseInt(this.numeroVoliTxtInput.getText());
    		Airline airline = this.cmbBoxLineaAerea.getValue() ;
        	if(airline == null) {
        		this.txtResult.appendText("ERRORE: selezionare una linea aerea.\n");
        		return ;
        	}
    		List<Passeggero> delays = model.simula(K,V,airline) ;
    		if(delays!=null) {
    			this.txtResult.appendText("RITARDI ACCUMULATI DAI PASSEGGERI DURANTE LA SIMULAZIONE:\n");
    			for(Passeggero p : delays)
    				this.txtResult.appendText("Passeggero "+ (p.getIdPasseggero()+1) + " delay = "+p.getRitardo()+" minuti\n");
    		}
    		
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire un numero di passeggero o di voli validi.\n");
    	}
    }
 */
public class SimulazioneSuiPasseggeri {
	
	enum EventType {
		PASSEGGERO_PARTITO,
		PASSEGGERO_ARRIVATO
	}
	
	class Event implements Comparable<Event>{
		private EventType eventtype ;
		private Flight flight ;
		private Passeggero passeggero ;
		public Event(EventType eventtype, Flight flight, Passeggero passeggero) {
			super();
			this.eventtype = eventtype;
			this.flight = flight ;
			this.passeggero = passeggero;
		}
		public EventType getEventtype() {
			return eventtype;
		}
		public void setEventtype(EventType eventtype) {
			this.eventtype = eventtype;
		}
		
		public Passeggero getPasseggero() {
			return passeggero;
		}
		public Flight getFlight() {
			return flight;
		}
		public void setFlight(Flight flight) {
			this.flight = flight;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((flight == null) ? 0 : flight.hashCode());
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
			Event other = (Event) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (flight == null) {
				if (other.flight != null)
					return false;
			} else if (!flight.equals(other.flight))
				return false;
			return true;
		}
		private SimulazioneSuiPasseggeri getOuterType() {
			return SimulazioneSuiPasseggeri.this;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Event [eventtype=");
			builder.append(eventtype);
			builder.append(", flight=");
			builder.append(flight);
			builder.append(", passeggero=");
			builder.append(passeggero);
			builder.append("]");
			return builder.toString();
		}
		@Override
		public int compareTo(Event o) {
			return flight.getScheduledDepartureDate().compareTo(o.getFlight().getScheduledDepartureDate());
		}
		
	}
	
	private int K ; //tot passeggeri
	private int V ; //tot voli che devono prendere
	private PriorityQueue<Event> queue ;
	private List<Airport> airports ;
	private AirportsIdMap airportIdMap ;
	private List<Passeggero> passeggeri ;
	
	public void init(int K, int V, List<Airport> airports, AirportsIdMap airportidMap) {
		this.K = K ;
		this.V = V ;
		this.airports = airports ;
		Collections.shuffle(this.airports);
		queue = new PriorityQueue<>() ;
		DAO dao = new DAO() ;
		passeggeri = new ArrayList<>();
		int i = 0;
		while(i < K) {
			Passeggero p = new Passeggero(i, 0);
			p.setCurrentAirport(this.airports.get(i));
			passeggeri.add(p) ;
			Flight flight = dao.getFirstFlightFrom(this.airports.get(i));
			Event e = new Event(EventType.PASSEGGERO_PARTITO, flight, p) ;
			queue.add(e) ;
			i++ ;
		}
		this.airportIdMap = airportidMap ;
		
	}
	public void run() {
		Event e ;
		try {
			while((e = queue.poll())!=null) {
				processEvent(e) ;
			}
		} catch(NullPointerException npe) {
			
		}
	}
	
	private void processEvent(Event e) {
		DAO dao = new DAO() ;

		switch(e.getEventtype()) {
		case PASSEGGERO_PARTITO:
			e.getPasseggero().setRitardo(e.getPasseggero().getRitardo()+e.getFlight().getArrivalDelay());
			Flight flight = dao.getNextFlight(airportIdMap.get(e.getFlight().getDestinationAirportId()), e.getFlight()) ;
			
			//segnalo che è arrivato
			Event arrivo = new Event(EventType.PASSEGGERO_ARRIVATO, flight, e.getPasseggero()) ;
			queue.add(arrivo) ;
			
			break;
			
		case PASSEGGERO_ARRIVATO:
			
			e.getPasseggero().setCurrentAirport(airportIdMap.get(e.getFlight().getDestinationAirportId())); 
			e.getPasseggero().setRitardo(e.getPasseggero().getRitardo()+e.getFlight().getDepartureDelay());
			Flight flight1 = dao.getNextFlight(airportIdMap.get(e.getFlight().getDestinationAirportId()), e.getFlight()) ;
			if(e.getPasseggero().getFlights().size() < V || flight1 == null) {
				//lo faccio partire
				Event partenza = new Event(EventType.PASSEGGERO_PARTITO, flight1, e.getPasseggero()) ;
				queue.add(partenza) ;
			}
			break;
		}
	}
	
	public List<Passeggero> getPassengerDelays() {
		return this.passeggeri ;
	}
	
}
