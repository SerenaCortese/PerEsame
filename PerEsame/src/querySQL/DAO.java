package querySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idMapBean.Airline;
import idMapBean.AirlinesIdMap;
import idMapBean.Airport;
import idMapBean.AirportsIdMap;
import idMapBean.Flight;
import idMapBean.FlightsIdMap;


public class DAO {
	
	public List<Airport> getAllAirports(AirportsIdMap airportsIdMap) {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Airport airport = new Airport(res.getInt("ID"), res.getString("IATA_CODE"),res.getString("AIRPORT"),
						res.getString("CITY") , res.getString("STATE"),res.getString("COUNTRY"),
						res.getDouble("LATITUDE"), res.getDouble("LONGITUDE"), res.getDouble("TIMEZONE_OFFSET"));
						 
				result.add(airportsIdMap.get(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	
	public List<Airport> getAllAirportsByAirline(AirportsIdMap airportIdMap, Airline airline) {
		
		String sql = "SELECT DISTINCT a.* " + 
				"FROM airports AS a , flights AS f " + 
				"WHERE (a.ID = f.ORIGIN_AIRPORT_ID OR a.ID = f.DESTINATION_AIRPORT_ID) " + 
				"AND f.Airline_ID = ? " + 
				"ORDER BY a.ID ASC " ;
		
		List<Airport> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, airline.getId());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Airport a = new Airport(res.getInt("ID"), res.getString("IATA_CODE"),res.getString("AIRPORT"),
						res.getString("CITY") , res.getString("STATE"),res.getString("COUNTRY"),
						res.getDouble("LATITUDE"), res.getDouble("LONGITUDE"), res.getDouble("TIMEZONE_OFFSET"));
						
				list.add(airportIdMap.get(a));
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}
	}
	

	public List<Airline> getAllAirlines(AirlinesIdMap airlineIdMap) {
		String sql = "SELECT * FROM airlines ORDER BY ID ASC" ;
		
		List<Airline> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Airline airline = new Airline(res.getInt("ID"), res.getString("IATA_CODE"), res.getString("AIRLINE"));
						
				list.add(airlineIdMap.get(airline));
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Flight> getAllFlights(FlightsIdMap flightIdMap, AirportsIdMap airportIdMap,AirlinesIdMap airlineIdMap){
		String sql = "SELECT DISTINCT *  FROM flights ";
	    List<Flight> list = new ArrayList<>() ;
	      
	      try {
	        Connection conn = DBConnect.getConnection() ;

	        PreparedStatement st = conn.prepareStatement(sql) ;
	        ResultSet res = st.executeQuery() ;
	        
	        while(res.next()) {
	          Airport source = airportIdMap.get(res.getInt("ORIGIN_AIRPORT_ID"));
	          Airport destination = airportIdMap.get(res.getInt("DESTINATION_AIRPORT_ID"));
	          Airline airline = airlineIdMap.get(res.getInt("AIRLINE_ID"));
	          Flight f = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
	        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
	        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
	        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
	          
	          list.add(flightIdMap.get(f));
	    
	        }
	        
	        conn.close();
	        
	        return list ;
	      } catch (SQLException e) {

	        e.printStackTrace();
	        return null ;
	      }

	}
	
	
	public List<Flight> getAllFlightsByAirline(AirportsIdMap airportIdMap,Airline airline,FlightsIdMap flightIdMap) {
		
		String sql = "SELECT DISTINCT *  FROM flights AS f WHERE f.Airline_ID = ?";
	    List<Flight> list = new ArrayList<>() ;
	      
	      try {
	        Connection conn = DBConnect.getConnection() ;

	        PreparedStatement st = conn.prepareStatement(sql) ;
	        st.setInt(1, airline.getId());
	        ResultSet res = st.executeQuery() ;
	        
	        while(res.next()) {
	          Airport source = airportIdMap.get(res.getInt("Source_airport_ID"));
	          Airport destination = airportIdMap.get(res.getInt("Destination_airport_ID")); 
	          Flight f = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
	        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
	        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
	        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
	          
	          list.add(flightIdMap.get(f));
	    
	        }
	        
	        conn.close();
	        
	        return list ;
	      } catch (SQLException e) {

	        e.printStackTrace();
	        return null ;
	      }
	      
	}
	/**
	 * Un arco collega due aeroporti solo esiste almeno un volo della compagnia aerea selezionata
	 * tra gli aeroporti considerati.
	 * Il peso dell’arco è dato dal rapporto tra la media dei ritardi su quella tratta (“ARRIVAL_DELAY”)
	 * e la distanza geografica tra gli aeroporti 
	 * (utilizzare la libreria SimpleLatLng per calcolare la distanza tra le coordinate degli aeroporti).
	 * @param airline
	 * @return
	 */
	public Map<Flight,Double> getArchi(Airline airline){
		String sql = "SELECT *, avg(f.ARRIVAL_DELAY) as mediaRitardi "
				+ "FROM flights as f "
				+ "WHERE f.AIRLINE_ID = ? "
				+ "GROUP BY f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID ";
		Map<Flight,Double> result = new HashMap<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, airline.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
			
				Flight flight = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
		        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
		        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
		        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
		          
				double peso = res.getDouble("mediaRitardi");
				result.put(flight, peso);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

	}

	public List<Flight> getVoliPartenzaDaAeroporto(Airport a) {
		String sql = "SELECT * FROM flights as f WHERE f.origin_airport_id = ? "
				+ "ORDER BY scheduled_dep_date ASC";
		List<Flight> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
			
				Flight flight = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
		        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
		        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
		        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
		          
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	
	}
	
	public Flight getFirstFlightFrom(Airport airport) {
		final String sql = "select * " + 
				"from flights f " + 
				"where f.ORIGIN_AIRPORT_ID=? "
				+ "and year(SCHEDULED_DEP_DATE) = 2015 " + 
				"LIMIT 1";
		Flight result = null ;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, airport.getId());
			ResultSet res = st.executeQuery();

			if (res.next()) {
				result = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
		        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
		        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
		        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
		          
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public Flight getNextFlight(Airport airport, Flight flight) {
		final String sql = "select * " + 
				"from flights f " + 
				"where f.ORIGIN_AIRPORT_ID=? " + 
				"and year(SCHEDULED_DEP_DATE) = 2015 " + 
				"and ARRIVAL_DATE > ? " + 
				"LIMIT 1";
		Flight result = null ;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, airport.getId());
			st.setTimestamp(2, Timestamp.valueOf(flight.getArrivalDate()));//forse funziona, se no usa quello commentato
			
			//st.setDate(2, java.sql.Date.valueOf(flight.getArrivalDate().toLocalDate()));
			ResultSet res = st.executeQuery();

			if (res.next()) {
				result = new Flight(res.getInt("ID"), res.getInt("AIRLINE_ID"), res.getInt("FLIGHT_NUMBER"),
		        		  res.getString("TAIL_NUMBER"), res.getInt("ORIGIN_AIRPORT_ID"), res.getInt("DESTINATION_AIRPORT_ID"),
		        		  res.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), res.getTimestamp("ARRIVAL_DATE").toLocalDateTime(),
		        		  res.getDouble("DEPARTURE_DELAY"), res.getDouble("ARRIVAL_DELAY"), res.getDouble("ELAPSED_TIME"), res.getInt("DISTANCE"));
		          
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
}
