package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Event e= new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic"));
					list.add(e);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getAllOffenseCategories(){
		List<String> categories=new ArrayList<>();
		String sql="SELECT DISTINCT offense_category_id from events";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Event> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				categories.add(new String(res.getString("offense_category_id")));
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Errore nella connessione.");
		}
		Collections.sort(categories);
		return categories;
	}
	
	public List<Integer> getAllMonths(){
		List<Integer> months=new ArrayList<>();
		String sql="SELECT DISTINCT MONTH(EVENTS.reported_date) as mese " + 
				"FROM events";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Event> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				months.add(res.getInt("mese"));
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Errore nella connessione.");
		}
		Collections.sort(months);
		return months;
	}

	public List<Adiacenza> getAdiacenze(String eventType, int month) {
		String sql="SELECT e1.offense_type_id, e2.offense_type_id, count(DISTINCT(e1.neighborhood_id)) AS peso " + 
				"from EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_category_id=? " + 
				"	AND e2.offense_category_id=? " + 
				"	AND e1.offense_type_id!=e2.offense_type_id " + 
				"	AND e1.neighborhood_id=e2.neighborhood_id " + 
				"	AND month(e1.reported_date)=? " + 
				"	AND MONTH(e2.reported_date)=? " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id";
		List<Adiacenza> adiacenze=new LinkedList<>();
		
		try {
			Connection conn= DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, eventType);
			st.setString(2, eventType);
			st.setInt(3, month);
			st.setInt(4, month);
			ResultSet rs=st.executeQuery();
			while(rs.next()) {
				Adiacenza a=new Adiacenza(rs.getString("e1.offense_type_id"),
						rs.getString("e2.offense_type_id"),
						rs.getDouble("peso"));
				adiacenze.add(a);
			}
			conn.close();
		}catch(SQLException e) {
			System.out.println("Errore nella QUERY delle adiacenze");
			e.printStackTrace();
		}
		return adiacenze;
	}

}
