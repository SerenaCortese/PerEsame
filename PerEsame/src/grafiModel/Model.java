package grafiModel;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import idMapBean.Airline;
import idMapBean.AirlinesIdMap;
import idMapBean.Airport;
import idMapBean.AirportsIdMap;
import idMapBean.Flight;
import idMapBean.FlightsIdMap;
import querySQL.DAO;

public class Model {
	
	private DAO dao;
	
	private List<Airport> aeroporti;
	private List<Airline> airlines;
	private List<Flight> flights;
	
	private AirportsIdMap aeroportiIdMap;
	private AirlinesIdMap airlineIdMap;
	private FlightsIdMap flightIdMap;
	
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao = new DAO();
		aeroportiIdMap = new AirportsIdMap();
		airlineIdMap = new AirlinesIdMap();
		flightIdMap =new FlightsIdMap();
		
		aeroporti = dao.getAllAirports(aeroportiIdMap);
		airlines = dao.getAllAirlines(airlineIdMap);
		
	}
	
	public void grafo() {
		//Inizializzare il grafo e la struttura dati, così ogni volta che lo crea riparte da zero
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungere VERTICI da collezione:  mettere HASHCODE e EQUALS per la classe vertici
		//Graphs.addAllVertices(grafo, list);
		//System.out.println("Aggiunti "+grafo.vertexSet().size()+" vertici\n");
		
		//Aggiungere ARCHI da collezione
		//Graphs.addAllEdges(grafo, list);
		//Altrimenti bisogna fare 
		//Graphs.addEdge(grafo, sourceVertex, targetVertex, weight);
		//System.out.println("Aggiunti "+grafo.edgeSet().size()+" archi\n");
		
		//PESO DATO ARCO
		//DefaultWeightedEdge e = grafo.getEdge(sourceVertex, targetVertex);
		//int peso = grafo.getEdgeWeight(e);
		
		//VICINI
		//Graphs.neighborListOf(grafo, vertice);
		
		//COMPONENTE CONNESSA
//		KosarajuStrongConnectivityInspector<Exhibition, DefaultEdge> ksci = new KosarajuStrongConnectivityInspector<Exhibition, DefaultEdge>(sdgraph);
//		return ksci.isStronglyConnected();
		
		
		//NUMERO COMPONENTI CONNESSE
		//ConnectivityInspector<Airport, DefaultWeightedEdge> ci = new ConnectivityInspector(grafo);
		//System.out.println(ci.connectedSets().size());
		
		//CAMMINO MINIMO
//		if (source == null || destination == null) {
//			throw new RuntimeException("Gli areoporti selezionati non sono presenti in memoria");
//		}
//		
//		ShortestPathAlgorithm<Airport,DefaultWeightedEdge> spa = new DijkstraShortestPath<Airport, DefaultWeightedEdge>(grafo);
//		double weight = spa.getPathWeight(source, destination);
//		System.out.println(weight);
//		
//		GraphPath<Airport,DefaultWeightedEdge> gp = spa.getPath(source, destination);
//		
//		return gp.getVertexList();
		
		//RAGGIUNGIBILI ITERATORE
//		private List<Country> displayAllNeighboursJGraphT(Country selectedCountry) {
//
//			List<Country> visited = new LinkedList<Country>();
		//se non mi interessa ordine in cui son trovati meglio usare Set<>=HashSet<>();
//
//			// Versione 1 : utilizzo un BreadthFirstIterator
////			GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<Country, DefaultEdge>(graph,
////					selectedCountry); //gli dico vertice da cui partire
////			while (bfv.hasNext()) { //ogni volta che va avanti di un passo mi salvo il vertice successivo
////				visited.add(bfv.next());
////			}
//
//			// Versione 2 : utilizzo un DepthFirstIterator
//			GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<Country, DefaultEdge>(graph, selectedCountry);
//			while (dfv.hasNext()) {
//				visited.add(dfv.next());
//			}
//
//			return visited;
//		}
		
		
		
	}
	
	public void utilities() {
		//DATE
//		LocalDate primoGiorno = LocalDate.of(YEAR, m, 1) ; 
//		LocalDate ultimoGiorno = primoGiorno.with(TemporalAdjusters.lastDayOfMonth());
//		ultimoGiorno.getDayOfMonth();
		
		//mettere nella box il numero intero dei mesi 
//		choiceBox<Month> boxMese;
//		boxMese.getItems().addAll(Month.values());
		
		//da intero del mese a mese
//		Month m = Month.of(Integer.valueOf(mese));
		//giorni totali in un mese
//		int giorniTotali = m.length(false);
		
        //Nel DAO 
//		rs.getDate("data").toLocalDate oppure Year.valueof(rs.getInt("anno");
		
		
		//DISTANZE
//		double dist = LatLngTool.distance(new LatLng(sourceAirport.getLatitude(), 
//				sourceAirport.getLongitude()), new LatLng(destinationAirport.getLatitude(), 
//						destinationAirport.getLongitude()), LengthUnit.KILOMETER);
	}

	public AirportsIdMap getAeroportiIdMap() {
		return this.aeroportiIdMap;
	}

	public List<Airport> getAirports() {
		return this.aeroporti;
	}
	


}
