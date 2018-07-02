package grafiModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import idMapBean.Airline;
import idMapBean.AirlinesIdMap;
import idMapBean.Airport;
import idMapBean.AirportsIdMap;
import idMapBean.Flight;
import idMapBean.FlightsIdMap;
import querySQL.DAO;

/** 
 * a.Costruire un grafo SEMPLICE,ORIENTATO E PESATO i cui NODI siano gli AEROPORTI.
 * 	 Un ARCO collega due aeroporti solo SE la loro DISTANZA è INFERIORE ai chilometri selezionati
 *   ed ALMENO una COMPAGNIA AEREA COMPIE TALE ROTTA.
 *   Il PESO dell’arco è pari alla DURATA del VOLO, ipotizzando una velocità costante di crociera a 800 km/h.
 * b.ESCLUDENDO gli AEROPORTI con ZERO ROTTE, se GRAFO FORTEMENTE CONNESSO.
 *   Inoltre, stampare l’AEROPORTO RAGGIUNGIBILE da “Fiumicino” PIU LONTANO da “Fiumicino” stesso.
 * @author sere9
 *
 */
public class GrafoAeroportoPiuLontano {
	
	private DAO fdao = null;
	
	private List<Airport> airports;
	private List<Airline> airlines;
	private List<Flight> routes;
	
	private AirlinesIdMap airlineIdMap;
	private AirportsIdMap airportIdmap;
	private FlightsIdMap routeIdMap;
	
	SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	public GrafoAeroportoPiuLontano() {
		fdao = new DAO();
		
		this.airlineIdMap = new AirlinesIdMap();
		this.airportIdmap = new AirportsIdMap();
		this.routeIdMap = new FlightsIdMap();
		
		airlines = fdao.getAllAirlines(airlineIdMap);
		System.out.println(airlines.size());

		airports = fdao.getAllAirports(airportIdmap);
		System.out.println(airports.size());

		routes = fdao.getAllFlights(routeIdMap,airportIdmap,airlineIdMap);
		System.out.println(routes.size());
		
	}
	
	public List<Airport> getAirports(){
		if(this.airports == null)
			return new ArrayList<Airport>(); 
		return this.airports;
	}
	
	public void createGraph() {
		//creo grafo
		grafo = new SimpleDirectedWeightedGraph<Airport,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.airports);
		
		//creo archi(=>itero sulle rotte)
		for(Flight r: this.routes) {
			Airport sourceAirport = airportIdmap.get(r.getOriginAirportId());
			Airport destinationAirport = airportIdmap.get(r.getDestinationAirportId());
			
			if(!sourceAirport.equals(destinationAirport)) {//non devo fare questo controllo se creo uno pseudografo perché lì loop ammessi
				
				double weight = LatLngTool.distance(new LatLng(sourceAirport.getLatitude(), sourceAirport.getLongitude()),
						new LatLng(destinationAirport.getLatitude(), destinationAirport.getLongitude()), LengthUnit.KILOMETER);
				Graphs.addEdge(this.grafo, sourceAirport, destinationAirport, weight);
			}
		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
		
	}
	
	public void printStats() {
		if(this.grafo == null) {
			this.createGraph();
		}
		
		ConnectivityInspector<Airport,DefaultWeightedEdge> ci = new ConnectivityInspector<Airport,DefaultWeightedEdge>(grafo);
		System.out.println(ci.connectedSets().size());
		
	}
	
	public Set<Airport> getBiggestSCC(){//SCC perché componente fortemente connesa
		Set<Airport> bestSet = null;
		int bestSize = 0;
		
		ConnectivityInspector<Airport,DefaultWeightedEdge> ci = new ConnectivityInspector<Airport,DefaultWeightedEdge>(grafo);
		for(Set<Airport> s : ci.connectedSets()) {
			if(s.size() > bestSize) {
				bestSet = new HashSet(s); //così son sicura che faccia un'altra copia
				bestSize = s.size();
			}
		}
		return bestSet;
	}

	public List<Airport> getShortestPath(int id1, int id2) {
		Airport a1 = airportIdmap.get(id1);
		Airport a2 = airportIdmap.get(id2);
		
		if(a1 == null || a2 == null) {
			throw new RuntimeException("Gli aeroporti selezionati non sono presenti in memoria");
		}
		System.out.println(a1.toString());
		System.out.println(a2.toString());
		
		//uso algoritmo di Dijkstra
		ShortestPathAlgorithm<Airport, DefaultWeightedEdge> spa = new DijkstraShortestPath<Airport, DefaultWeightedEdge>(this.grafo);
		
		double weight = spa.getPathWeight(a1, a2);
		System.out.println("peso: "+ weight);
		GraphPath<Airport, DefaultWeightedEdge> gp = spa.getPath(a1, a2);
	
		return gp.getVertexList();
		
	}

}
