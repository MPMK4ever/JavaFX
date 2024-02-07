import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HistoryEntry {

	private final StringProperty url;
	private final StringProperty lastVisitedDate;

	public HistoryEntry(String url, Date lastVisitedDate) {
		this.url = new SimpleStringProperty(url);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(lastVisitedDate);

		this.lastVisitedDate = new SimpleStringProperty(formattedDate);

	}

	public StringProperty urlProperty() {
		return url;
	}

	public StringProperty lastVisitedDateProperty() {
		return lastVisitedDate;
	}

}
