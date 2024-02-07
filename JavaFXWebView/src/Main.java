import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

	private TextField textField = new TextField("www.google.com");

	private WebView webView = new WebView();
	private WebEngine webEngine;
	private WebHistory history;
	private TabPane tabPane;
	private String homePageUrl = "http://www.google.com";

	private TableView<HistoryEntry> historyTable = new TableView<>();
	private ObservableList<HistoryEntry> historyEntries = FXCollections.observableArrayList();
	private boolean isHistoryDisplay = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		// Initialize the WebView and WebEngine
		webEngine = webView.getEngine();
		history = webEngine.getHistory();

		// Create Buttons
		Button reloadButton = new Button("Refresh");
		Button zoomInButton = new Button();
		Button zoomOutButton = new Button();
		Button historyButton = new Button("History");
		Button backButton = new Button();
		Button forwardButton = new Button();
		Button goButton = new Button("Go");
		Button homeButton = new Button("Home");
		Button printButton = new Button("Print");

		// Event Handlers
		reloadButton.setOnAction(e -> webEngine.reload());
		zoomInButton.setOnAction(e -> zoomIn());
		zoomOutButton.setOnAction(e -> zoomOut());
		historyButton.setOnAction(e -> displayHistory());
		backButton.setOnAction(e -> navigateBack());
		forwardButton.setOnAction(e -> navigateForward());
		goButton.setOnAction(e -> loadPage());
		homeButton.setOnAction(e -> webEngine.load(homePageUrl));
		printButton.setOnAction(e -> print());

		// Add image to Buttons

		ImageView zoomInImage = new ImageView(new Image("zoomIn.png"));
		zoomInImage.setFitWidth(16);
		zoomInImage.setFitHeight(16);
		zoomInButton.setGraphic(zoomInImage);

		ImageView zoomOutImage = new ImageView(new Image("zoomOut.png"));
		zoomOutImage.setFitWidth(16);
		zoomOutImage.setFitHeight(16);
		zoomOutButton.setGraphic(zoomOutImage);

		ImageView backButtonImage = new ImageView(new Image("back.png"));
		backButtonImage.setFitWidth(16);
		backButtonImage.setFitHeight(16);
		backButton.setGraphic(backButtonImage);

		ImageView forwardButtonImage = new ImageView(new Image("forward.png"));
		forwardButtonImage.setFitWidth(16);
		forwardButtonImage.setFitHeight(16);
		forwardButton.setGraphic(forwardButtonImage);

		ImageView homeButtonImage = new ImageView(new Image("home.png"));
		homeButtonImage.setFitWidth(16);
		homeButtonImage.setFitHeight(16);
		homeButton.setGraphic(homeButtonImage);

		ImageView reloadButtonImage = new ImageView(new Image("refresh.png"));
		reloadButtonImage.setFitWidth(16);
		reloadButtonImage.setFitHeight(16);
		reloadButton.setGraphic(reloadButtonImage);

		ImageView printButtonImage = new ImageView(new Image("print.png"));
		printButtonImage.setFitWidth(16);
		printButtonImage.setFitHeight(16);
		printButton.setGraphic(printButtonImage);

		// TextField Setting - Enter URL
		textField.setStyle("-fx-text-fill: gray");
		textField.setPrefWidth(250);
		textField.setPrefHeight(20);

		// Toolbar
		ToolBar toolBar = new ToolBar(textField, goButton, reloadButton, zoomInButton, zoomOutButton, historyButton,
				backButton, forwardButton, homeButton, printButton);

		// layout - History
		BorderPane pane = new BorderPane();
		pane.setTop(toolBar);
		pane.setCenter(tabPane);
		pane.setBottom(historyTable);

		// Layout - WebView
		BorderPane root = new BorderPane();
		root.setTop(toolBar);
		root.setCenter(webView);

		// Create a scene
		Scene scene = new Scene(root, 800, 600);

		// Set stage properties
		primaryStage.setTitle("JavaFX WebView");
		primaryStage.getIcons().add(new Image("webIcon.jpg"));
		primaryStage.setScene(scene);
		primaryStage.show();

		// Load the initial URL
		loadPage();

		// Create a TabPane for tabs
		tabPane = new TabPane();

		// Create a WebView Tab
		Tab webViewTab = new Tab("WebView", webView);
		tabPane.getTabs().add(webViewTab);

		// Create a TableView for displaying history
		historyTable = new TableView<>();

		// Create the table columns for History & Add columns to the table
		TableColumn<HistoryEntry, String> urlColumn = new TableColumn<>("URL");
		urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
		TableColumn<HistoryEntry, String> dateColumn = new TableColumn<>("Last Visited Date");
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().lastVisitedDateProperty());

		historyTable.getColumns().addAll(urlColumn, dateColumn);

		root.setCenter(tabPane);

	}

	private void loadPage() {
		String url = textField.getText();
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		webEngine.load(url);
	}

	private void zoomIn() {
		webView.setZoom(webView.getZoom() + 0.25);
	}

	private void zoomOut() {
		webView.setZoom(webView.getZoom() - 0.25);
	}

	private void displayHistory() {

		if (!isHistoryDisplay) {
			isHistoryDisplay = true;

			// Clear existing history entries
			historyEntries.clear();

			// Get history entries from your WebView's WebEngine
			ObservableList<WebHistory.Entry> entries = history.getEntries();

			// Iterate through the entries and add them to historyEntries
			for (WebHistory.Entry entry : entries) {
				historyEntries.add(new HistoryEntry(entry.getUrl(), entry.getLastVisitedDate()));
			}

			// Set the data in the historyTable
			historyTable.setItems(historyEntries);

			// Create a Tab for displaying history
			Tab historyTab = new Tab("History", historyTable);
			historyTab.setContent(historyTable);

			// Add the historyTab to the TabPane
			tabPane.getTabs().add(historyTab);

		}
	}

	private void print() {

		PrinterJob printerJob = PrinterJob.createPrinterJob();
		if (printerJob != null) {
			webEngine.print(printerJob);
			printerJob.endJob();
		}
	}

	private void navigateBack() {
		if (history.getCurrentIndex() > 0) {
			history.go(-1);
		}
	}

	private void navigateForward() {
		if (history.getCurrentIndex() < history.getEntries().size() - 1) {
			history.go(1);
		}
	}
}
