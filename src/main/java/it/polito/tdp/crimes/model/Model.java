package it.polito.tdp.crimes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	private List<String> best;
	
	public Model() {
		dao=new EventsDao();
	}
	
	public List<String> getAllCategories(){
		return dao.getAllOffenseCategories();
	}
	public List<Integer> getAllMonths(){
		return dao.getAllMonths();
	}
	
	public void creaGrafo(String eventType, int month) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Adiacenza> adiacenze=dao.getAdiacenze(eventType, month);
		
		//Aggiungo i vertici, prendendoli dalle adiacenze
		for(Adiacenza a: adiacenze) {
			if(!grafo.vertexSet().contains(a.getE1()))
				grafo.addVertex(a.getE1());
			if(!grafo.vertexSet().contains(a.getE2()))
				grafo.addVertex(a.getE2());
			
		//Adesso aggiungo gli edge
			if(grafo.getEdge(a.getE1(), a.getE2())==null) //vuol dire che tale arco non c'è
				Graphs.addEdge(grafo, a.getE1(), a.getE2(), a.getPeso());
		}
		System.out.println("Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.");
	}
	
	public List<Adiacenza> getArchiConPesoMaggiore(){
		double pesoMedio=0.0;
		List<Adiacenza> result=new LinkedList<>();
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			pesoMedio+=grafo.getEdgeWeight(e);
		}
		pesoMedio=pesoMedio/grafo.edgeSet().size();
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>pesoMedio)
				result.add(new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
		}
		Collections.sort(result);
		return result;
	}
	
	public List<String> trovaPercorso(String source, String target){
		best =new LinkedList<>();
		List<String> parziale=new LinkedList<>();
		parziale.add(source);
		ricorsiva(target, parziale, 0);
		return best;
	}
	
	public void ricorsiva(String target, List<String> parziale, int L) {
		
		//TERMINAZIONE
		if(parziale.get(parziale.size()-1).equals(target)) {
			if(parziale.size()>best.size())
				best=new LinkedList<>(parziale);
			return;
		}
		
		//CASO GENERALE
		for(String vicino : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			//CERCO CAMMINO ACICLICO, QUINDI NON DEVE TORNARE SU VERTICI GIA VISITATI
			if(!parziale.contains(vicino)) {
				
				parziale.add(vicino);
				ricorsiva(target, parziale, L+1);
				parziale.remove(parziale.size()-1);
			}
			
		}
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
}
