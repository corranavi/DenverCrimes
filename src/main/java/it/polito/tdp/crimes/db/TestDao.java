package it.polito.tdp.crimes.db;

import it.polito.tdp.crimes.model.Event;

public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		//for(Event e : dao.listAllEvents())
			//System.out.println(e);
		for(String s: dao.getAllOffenseCategories()) {
			System.out.println(s);
		}
	}

}
