import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {

	private int id;
	private Calendar date;
	private Set<Contact> contacts;
	
	MeetingImpl(int id, Calendar date, Set<Contact> contacts) {

		if (id < 1) {
			throw new IllegalArgumentException("ID must be equal to or greater than one.");
		}

		if (date == null) {
			throw new NullPointerException("Date can't be null.");
		}

		if (contacts == null) {
			throw new NullPointerException("Contacts can't be null.");
		}

		if (contacts.size() == 0) {
			throw new IllegalArgumentException("The set on contacts must have at least one contact inside it.");
		}

		this.id 	  = id;
		this.date 	  = date;
		this.contacts = contacts;
	}

	public int getId() {
		return this.id;
	}

	public Calendar getDate() {
		return this.date;
	}

	public Set<Contact> getContacts() {
		return this.contacts;
	}
}