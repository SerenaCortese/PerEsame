package grafiModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import idMapBean.Airline;
import idMapBean.AirlinesIdMap;
import idMapBean.Airport;
import idMapBean.AirportDistance;
import idMapBean.AirportsIdMap;
import idMapBean.Flight;
import idMapBean.FlightsIdMap;
import querySQL.DAO;

/**
 * 1.Utente seleziona una airline. Costruire un GRAFO, i cui VERTICI rappresentino TUTTI gli AEROPORTI,
 *   e la presenza di un ARCO (ORIENTATO) indica l’esistenza di ALMENO UNA ROTTA DIRETTA tenuta DALLA AIRLINE
 *   selezionata tra i due aeroporti adiacenti all’arco stesso. 
 *   Il PESO dell’arco deve misurare la DISTANZA in linea d’aria tra i due aeroporti, espressa in kilometri.
 *   Stampare l’elenco degli AEROPORTI RAGGIUNTI dalla AIRLINE.
 *  2.Utente SELEZIONA un AEROPORTO tra quelli raggiunti dalla compagnia aerea,
 *   determinare tutti gli AEROPORTI DA ESSO RAGGIUNGIBILI con viaggi di una o più tratte. 
 *   L’elenco deve essere ORDINE per DISTANZA CRESCENTE (in km) rispetto all’aeroporto di partenza.
 * @author sere9
 *
 */
public class GrafoAeroportiRaggiungibili {
	
	private Airline myAirline;
	private List<Airport> reachedAirports;

	private List<Airport> allAirports;
	private List<Airline> allAirlines;
	private List<Flight> allflights;

	private AirportsIdMap airportMap;
	private AirlinesIdMap airlineMap;
	private FlightsIdMap flightIdMap;

	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> graph;

	public GrafoAeroportiRaggiungibili() {
		DAO dao = new DAO();
		
		this.airlineMap = new AirlinesIdMap();
		this.airportMap = new AirportsIdMap();
		this.flightIdMap = new FlightsIdMap();
		this.allAirlines = dao.getAllAirlines(airlineMap);
		this.allAirports = dao.getAllAirports(airportMap);
		this.allflights = dao.getAllFlights(flightIdMap, airportMap, airlineMap);

	}

	public List<Airport> getReachedAirports(Airline airline) {
		if (this.myAirline == null || !this.myAirline.equals(airline)) {

			this.myAirline = airline;
			//System.out.println("Searching " + airline.toString() + "\n");

			DAO dao = new DAO();

			List<Airport> airports = dao.getAllAirportsByAirline(airportMap, myAirline);
			//System.out.println("Found " + airportIds.size() + " airports\n");

			this.reachedAirports = new ArrayList<Airport>();
			for (Airport a : airports)
				this.reachedAirports.add(airportMap.get(a));

			this.reachedAirports.sort(new Comparator<Airport>() {
				@Override
				public int compare(Airport o1, Airport o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

		}

		return this.reachedAirports;
	}

	public List<Airport> getReachedAirports() {
		return reachedAirports;
	}

	public List<Airport> getAllAirports() {
		return allAirports;
	}

	public List<Airline> getAllAirlines() {
		return allAirlines;
	}

	public void buildGraph(Airline airline) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(graph, this.allAirports);

		DAO dao = new DAO();
		List<Flight> routes = dao.getAllFlightsByAirline(airportMap, airline, flightIdMap);

		for (Flight r : routes) {
			if (r.getOriginAirportId() != 0 && r.getDestinationAirportId() != 0) {
				Airport a1 = airportMap.get(r.getOriginAirportId());
				Airport a2 = airportMap.get(r.getDestinationAirportId());

				if (a1 != null && a2 != null) {

					LatLng c1 = new LatLng(a1.getLatitude(), a1.getLongitude());
					LatLng c2 = new LatLng(a2.getLatitude(), a2.getLongitude());
					double distance = LatLngTool.distance(c1, c2, LengthUnit.KILOMETER);

					Graphs.addEdge(graph, a1, a2, distance);
					// System.out.format("%s->%s %.0fkm\n", a1, a2, distance);

				}
			}
		}

	}

	public List<AirportDistance> getDestinations(Airline airline, Airport start) {

		List<AirportDistance> list = new ArrayList<>();

		for (Airport end : reachedAirports) {
			DijkstraShortestPath<Airport, DefaultWeightedEdge> dsp = new DijkstraShortestPath<Airport, DefaultWeightedEdge>(graph);
			
			GraphPath<Airport, DefaultWeightedEdge> p = dsp.getPath(start, end);
					
			if (p != null) {
				list.add(new AirportDistance(end, p.getWeight(), p.getEdgeList().size()));
			}
		}

		list.sort(new Comparator<AirportDistance>() {
			@Override
			public int compare(AirportDistance o1, AirportDistance o2) {
				return Double.compare(o1.getDistance(), o2.getDistance());
			}
		});

		return list;

	}

	

}
