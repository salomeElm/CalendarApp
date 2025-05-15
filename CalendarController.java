import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Calendar;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * CalendarController is the main controller class for the interactive calendar application.
 * It handles the display of a monthly calendar, allows users to select year and month,
 * and enables adding or editing appointments for specific dates.
 */
public class CalendarController {

	@FXML
	private GridPane calendarGrid; // Grid layout representing the calendar days
	@FXML
	private ComboBox<String> monthComboBox; // ComboBox for selecting the month
	@FXML
	private ComboBox<Integer> yearComboBox; // ComboBox for selecting the year
	@FXML
	private Label monthYearLabel; // Label displaying the selected month and year

	private final String[] months = {
			"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"
	};

	private final int MIN_YEAR = 1990;
	private final int MAX_YEAR = 2100;
	private final int NUM_OF_COLUMNS = 7; // Number of days in a week
	private final int NUM_OF_ROWS = 7; // 1 row for day names + 6 for calendar dates

	// Map to store appointments: key = date formatted as dd/MM/yyyy, value = appointment text
	private HashMap<String, String> appointments = new HashMap<String, String>();

	/**
	 * Called automatically when the interface is loaded.
	 * Fills the year and month ComboBoxes and displays the initial calendar view.
	 */
	@FXML
	public void initialize() {
		for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
			yearComboBox.getItems().add(i);  
		}

		monthComboBox.getItems().addAll(months);

		Calendar now = Calendar.getInstance();
		yearComboBox.setValue(now.get(Calendar.YEAR));
		monthComboBox.setValue(months[now.get(Calendar.MONTH)]);

		updateCalendar(); // Display calendar for the current date

		yearComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Refresh the calendar when the selected year changes
				updateCalendar();
			}
		});

		monthComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Refresh the calendar when the selected month changes
				updateCalendar();
			}
		});
	}

	/**
	 * Updates the calendar display based on the selected year and month.
	 */
	private void updateCalendar() {
		calendarGrid.getChildren().clear();
		calendarGrid.getColumnConstraints().clear();
		calendarGrid.getRowConstraints().clear();

		int year = yearComboBox.getValue();
		int month = monthComboBox.getSelectionModel().getSelectedIndex();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);

		int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); 
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		// Update the label with current month and year
		monthYearLabel.setText(months[month] + " " + year);

		// Create 7 columns for the days of the week
		for (int i = 0; i < NUM_OF_COLUMNS; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(100.0 / NUM_OF_COLUMNS);
			calendarGrid.getColumnConstraints().add(col);
		}

		// Create 7 rows (1 for headers and 6 for the days)
		for (int i = 0; i < NUM_OF_ROWS; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / NUM_OF_ROWS);
			calendarGrid.getRowConstraints().add(row);
		}

		// Day-of-week headers (row 0)
		String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		for (int i = 0; i < NUM_OF_COLUMNS; i++) {
			Label label = new Label(dayNames[i]);
			label.setStyle("-fx-font-weight: bold;");
			GridPane.setHalignment(label, HPos.CENTER); 
			calendarGrid.add(label, i, 0);
		}

		int startCol = firstDayOfWeek - 1; // Adjust so Sunday = 0...
		int dayCounter = 1;
		int row = 1; // Start from row 1 (after headers)

		// Fill the calendar with buttons for each day
		for (int i = 0; i < (NUM_OF_COLUMNS*(NUM_OF_ROWS-1)); i++) { 
			int col = i % NUM_OF_COLUMNS;
			if (i < startCol || dayCounter > daysInMonth) {
				continue; // Skip empty cells before and after the month's days
			}

			Button dayButton = new Button(String.valueOf(dayCounter));
			dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			int currentDay = dayCounter;
			dayButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					openMeetingDialog(currentDay, month+1, year); // Open appointment editor
				}
			});

			calendarGrid.add(dayButton, col, row);
			dayCounter++;

			if (col == 6) {
				row++;
			}
		}
	}

	/**
	 * Opens a dialog for entering or editing appointments for a specific day.
	 * @param day   the day of the appointment
	 * @param month the month of the appointment 
	 * @param year  the year of the appointment
	 */
	private void openMeetingDialog(int day, int month, int year) {
		String key = String.format("%02d/%02d/%04d", day, month , year);
		String content = appointments.getOrDefault(key, ""); // Retrieve existing appointment or empty string

		TextArea textArea = new TextArea(content);
		textArea.setWrapText(true);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Meetings for " + key);
		alert.setHeaderText("Enter or edit appointments for this day:");
		alert.getDialogPane().setContent(textArea); // Inserting the text area inside the dialog
		alert.showAndWait(); // Show the dialog and wait for user action

		String updated = textArea.getText();
		if (!updated.trim().isEmpty()) {
			appointments.put(key, updated); // Save updated appointment
		} else {
			appointments.remove(key); // Remove if input is empty
		}
	}
}
