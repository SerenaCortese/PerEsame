package simulazione;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import grafiModel.Model;
import idMapBean.Airport;
import idMapBean.Flight;
/**
 * simulare lo spostamento di K passeggeri con comportamento casuale.
Inizialmente, posizionare K passeggeri in modo casuale tra gli aeroporti disponibili. In seguito, ciascun
passeggero prende il primo volo del 2015 in partenza (“SCHEDULED_DEPARTURE_DATE”) da quell’aeroporto.
c. Il passeggero, non appena giunto a destinazione (“ARRIVAL_DATE”), sceglierà il successivo primo volo
disponibile in partenza da tale aeroporto. Ipotizzare che non vi siano problemi di capienza e trascurare i
tempi di imbarco. La simulazione termina dopo che ciascun passeggero ha preso V voli, oppure se non ci
sono più voli in partenza dall’aeroporto considerato.
d. Al termine della simulazione, stampare il ritardo complessivo (“ARRIVAL_DELAY”) accumulato da ciascun
passeggero.
 * @author sere9
 *
 */
public class Simulazione {
	
	private int k; //totPasseggeri
	private int v; //totVoli
	private Model model;

	private PriorityQueue<Event> pq;
	
	private int totRitardo;
	
	public Simulazione(int k, int v, Model model) {
		this.k = k;
		this.v = v;
		this.model = model;
		this.totRitardo = 0 ;
		pq = new PriorityQueue<>();
	}

	public void init() {
		for(int i =0; i<k;i++) {
			int random = (int) Math.random()*model.getAirports().size();
			Airport partenza = model.getAirports().get(random);
			Flight volo = partenza.getVoliPartenza().get(0);
			Event e = new Event(partenza,volo ,volo.getScheduledDepartureDate(), EventType.PARTENZA,1);
			pq.add(e);
		}
				
	}
	
	public class Event implements Comparable<Event>{
		
		private Airport aeroporto;
		private Flight volo;
		private LocalDateTime ora;
		private EventType tipo;
		private int numVoli;
		
		public Event(Airport aeroporto, Flight volo, LocalDateTime ora, EventType tipo, int numVoli) {
			super();
			this.aeroporto = aeroporto;
			this.volo = volo;
			this.ora = ora;
			this.tipo = tipo;
			this.numVoli = numVoli;
		}

		public Airport getAeroporto() {
			return aeroporto;
		}

		public void setAeroporto(Airport aeroporto) {
			this.aeroporto = aeroporto;
		}

		public Flight getVolo() {
			return volo;
		}

		public void setVolo(Flight volo) {
			this.volo = volo;
		}

		public LocalDateTime getOra() {
			return ora;
		}

		public void setOra(LocalDateTime ora) {
			this.ora = ora;
		}

		public EventType getTipo() {
			return tipo;
		}

		public void setTipo(EventType tipo) {
			this.tipo = tipo;
		}

		public int getNumVoli() {
			return numVoli;
		}

		public void setNumVoli(int numVoli) {
			this.numVoli = numVoli;
		}

		@Override
		public int compareTo(Event arg0) {
			return this.ora.compareTo(arg0.ora);
		}

		@Override
		public String toString() {
			return "Event [aeroporto=" + aeroporto + ", volo=" + volo + ", ora=" + ora + ", tipo=" + tipo
					+ "]";
		}
		
	}
	
	public enum EventType{
		PARTENZA,ARRIVO
	}

	public void run() {
		Event e;
		while ((e = pq.poll()) != null) {
			if (e.getNumVoli()>= v || e.getAeroporto()== null)
				break;

			processEvent(e);
		}
		
	}

	private void processEvent(Event e) {
		System.out.println(e+" queue "+pq.size());
		switch (e.getTipo()) {
		case PARTENZA:
			Airport arrivo = model.getAeroportiIdMap().get(e.getVolo().getDestinationAirportId());
			Event e1 = new Event(arrivo,e.getVolo(),e.getVolo().getArrivalDate(),EventType.ARRIVO,e.getNumVoli());
			pq.add(e1);
			break;
		case ARRIVO:
			if(e.getVolo().getArrivalDelay()>0) {
				totRitardo+= e.getVolo().getArrivalDelay();
				Airport partenza = e.getAeroporto();
				for(Flight f : partenza.getVoliPartenza()) {
					if(f.getScheduledDepartureDate().compareTo(e.getOra())>0) {
						int numVoli = e.getNumVoli();
						Event e2 = new Event(partenza, f, f.getScheduledDepartureDate(),EventType.PARTENZA, numVoli++);
						pq.add(e2);
						break;
					}
				}
			}
			
			break;
		}
		
	}
	
	public int getTotRitardo() {
		return this.totRitardo;
	}
	
	
	
	
}