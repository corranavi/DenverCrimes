/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Adiacenza arco=boxArco.getValue();
    	if(arco==null) {
    		txtResult.appendText("Please select ad edge from the menu.\n");
    		return;
    	}
    	String source=arco.getE1();
    	String target=arco.getE2();
    	List<String> percorso= this.model.trovaPercorso(source, target);
    	for(String s: percorso) {
    		txtResult.appendText(s+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	boxArco.getItems().clear();
    	String categoria= boxCategoria.getValue();
    	if(categoria==null) {
    		txtResult.appendText("Please select a crime category from the menu.\n");
    		return;
    	}
    	Integer mese=boxMese.getValue();
    	if(mese==null) {
    		txtResult.appendText("Please select a month from the menu.\n");
    		return;
    	}
    	model.creaGrafo(categoria, mese);
    	if(model.numeroVertici()==0) {
    		txtResult.appendText("No solution found with these parameters.\n");
    		return;
    	}
    	txtResult.appendText("A graph has been created.\nThere are "+model.numeroVertici()+" vertices and "+model.numeroArchi()+" edges.\n");
    	List<Adiacenza> archi=this.model.getArchiConPesoMaggiore();
    	boxArco.getItems().addAll(archi);
    	for(Adiacenza a: archi) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(model.getAllCategories());
    	boxMese.getItems().addAll(model.getAllMonths());
    }
}
