import java.util.Set;
import java.util.Calendar;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private String notes;

	PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
		super(id, date, contacts);

		if (notes == null) {
			throw new NullPointerException("Notes cannot be null.");
		}

		this.notes = notes;
	}

	public String getNotes() {
		if (this.notes != null) {
			return this.notes;
		} else {
			return null;
		}
	}
}