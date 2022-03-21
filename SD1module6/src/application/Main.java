

// NEED TO MAKE DOCUMENTATION


package application;
	
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
	
	static Map<String, Integer> wordFrequency;
	static LinkedHashMap<String, Integer> orderedList;
	
	@Override
	public void start(Stage primaryStage) {
		
		VBox pageVert = new VBox();
		HBox buttonHorz = new HBox();
		Scene pageScene = new Scene(pageVert,900,500);
		Label pageLabel = new Label("Edgar Allan Poe's 'The Raven' words sorted");
		Button sortHigh = new Button("Sort high to low");
		Button sortLow = new Button("Sort low to high");
		TableView tableView = new TableView();
		Text wordList = new Text();
		
		TableColumn<Map, String> column1 = new TableColumn<>("Word");
	    column1.setCellValueFactory(new MapValueFactory<>("Key"));
	    TableColumn<Map, Integer> column2 = new TableColumn<>("Count");
	    column2.setCellValueFactory(new MapValueFactory<>("Value"));
	    
	    tableView.getColumns().add(column1);
		tableView.getColumns().add(column2);
		
		pageLabel.setFont(new Font("Arial", 24));
		primaryStage.setScene(pageScene);
		primaryStage.show();
		
		pageVert.getChildren().add(pageLabel);
		pageVert.getChildren().add(buttonHorz);
		buttonHorz.getChildren().add(sortHigh);
		buttonHorz.getChildren().add(sortLow);
		pageVert.getChildren().add(wordList);
		tableView.getItems().addAll(orderedList);
		
		sortHigh.setOnAction(actionEvent ->  {
			orderedList = new LinkedHashMap<>(); // HashMap used to hold ordered words (by count)
			
			// Code used from: https://howtodoinjava.com/java/sort/java-sort-map-by-values/
			wordFrequency.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
						forEachOrdered(x -> orderedList.put(x.getKey(), x.getValue()));
			
			wordList.setText(orderedList.toString());
		});
		
		sortLow.setOnAction(actionEvent ->  {
			orderedList = new LinkedHashMap<>();
			
			// Code used from: https://howtodoinjava.com/java/sort/java-sort-map-by-values/
			wordFrequency.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).
						forEachOrdered(x -> orderedList.put(x.getKey(), x.getValue()));
			
			wordList.setText(orderedList.toString());
		});
		
	}
	
	public static void main(String[] args) throws IOException {
		ArrayList<String> poemWords = new ArrayList<String>();
		File poemFile = new File("ravenPoem.html");
		FileInputStream fis = new FileInputStream(poemFile);
		byte[] data = new byte[(int) poemFile.length()];
		
		try {
			fis.read(data);
			fis.close();
			String entirePoem = new String(data, "UTF-8");
			int firstSpot = entirePoem.indexOf("<h1>"); // Start here
			int lastSpot = entirePoem.indexOf("<!--end chapter-->"); // End here
			entirePoem = entirePoem.substring(firstSpot, lastSpot); // Shortens the page to just the poem
			entirePoem = entirePoem.replaceAll("\\<.*?>",""); // Removes HTML tags
			entirePoem = entirePoem.replaceAll("\\.",""); // Removes period
			entirePoem = entirePoem.replaceAll("!"," "); // Removes !
			entirePoem = entirePoem.replaceAll("\\?"," "); // Removes ?
			entirePoem = entirePoem.replaceAll("[^\\p{ASCII}]", ""); // Removes non-ASII
			
			Scanner scanPoem = new Scanner(entirePoem);
			scanPoem.useDelimiter(" |\\n|-"); // White space
			while(scanPoem.hasNext()) {
				String word = scanPoem.next();
				if(word.length() > 0)
					poemWords.add(word);
			}
			scanPoem.close();
			
			wordFrequency = poemWords.stream().collect(toMap(s -> s, s -> 1, Integer::sum)); // Maps words and counts occurrences
			System.out.println("Sorted words:");
			System.out.println(orderedList);
		   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		launch(args);
	}
}