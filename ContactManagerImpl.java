import java.util.*;
// import java.util.Set;
// import java.util.Iterator;
// import java.util.HashMap;
// import java.util.Arrays;

public class ContactManagerImpl implements ContactManager {

	private HashMap<Integer, Meeting> meetings;

	ContactManagerImpl() {
		this.meetings   = new HashMap<Integer, Meeting>();
	}

	/**
	 * Works out the ID that should be assigned to the meeting that
	 * is being added to the set. Takes the most recent ID (which will be
	 * the key) and increments it by one.
	 *
	 * @return int
	 * @access private
	 */
	private int workoutNextIdIncrement() {
		Set<Integer> keys      = this.meetings.keySet();
		Object[]     keysArray = keys.toArray();

		int key = 1;
		if (!keys.isEmpty()) {
			key = (int) keysArray[keys.size() - 1] + 1;
		}

		return key;
	}

	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		if (contacts == null) {
			throw new NullPointerException("Contacts cannot be null.");
		}

		if (date == null) {
			throw new NullPointerException("Date cannot be null.");
		}

		Calendar currentDateTime = Calendar.getInstance();
		if (!date.after(currentDateTime)) {
			throw new IllegalArgumentException("Date must be a date in the future.");
		}

		if (contacts.isEmpty()) {
			throw new IllegalArgumentException("The set of contacts must have at least one contact.");
		}
		
		int key = this.workoutNextIdIncrement();
		Meeting newMeeting = new FutureMeetingImpl(key, date, contacts);
		this.meetings.put(key, newMeeting);

		return key;
	}

	public PastMeeting getPastMeeting(int id) {

		if (!this.meetings.containsKey(id)) {
			return null;
		}

		Meeting meeting = this.meetings.get(id);
		Calendar now    = Calendar.getInstance();

		if (meeting.getDate().after(now)) {
			throw new IllegalStateException("The selected meeting has a date in the future.");
		}
		
		return (PastMeeting) meeting;
	}

	public FutureMeeting getFutureMeeting(int id) {
		
		if (!this.meetings.containsKey(id)) {
			return null;
		}

		Meeting meeting = this.meetings.get(id);
		Calendar now    = Calendar.getInstance();

		if (meeting.getDate().before(now)) {
			throw new IllegalStateException("The selected meeting has a date in the past.");
		}

		return (FutureMeeting) meeting;
	}

	public Meeting getMeeting(int id) {
		
		if (!this.meetings.containsKey(id)) {
			return null;
		}

		return this.meetings.get(id);
	}

	public List<Meeting> getFutureMeetingList(Contact contact) {

		// @TODO - Implement the contact check

		List<Meeting> futureMeetingList = new ArrayList<Meeting>();

		if (!this.meetings.isEmpty()) {
			Set<Integer> keys = this.meetings.keySet();
			Iterator iterator = keys.iterator();

			while (iterator.hasNext()) {
				Meeting meeting = this.meetings.get(iterator.next());
				Calendar now    = Calendar.getInstance();

				if (!meeting.getDate().before(now)) {
					Set<Contact> contacts = meeting.getContacts();

					Iterator contactIterator = contacts.iterator();
					while (contactIterator.hasNext()) {
						Contact person = (Contact) contactIterator.next();

						if (person.getId() == contact.getId()) {
							futureMeetingList.add(meeting);
						}
					}
				}
			}
		}

		if (!futureMeetingList.isEmpty()) {
			Collections.sort(futureMeetingList, new MeetingComparator());
		}

		return futureMeetingList;
	}

	// @TODO
	// public List<Meeting> getMeetingListOn(Calendar date) {
	// 	return null;
	// }

	// //@TODO
	// public List<Meeting> getPastMeetingListFor(Contact contact) {

	// }

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {

		if (contacts == null) {
			throw new NullPointerException("Contacts cannot be null.");
		}

		if (date == null) {
			throw new NullPointerException("Date cannot be null.");
		}

		if (text == null) {
			throw new NullPointerException("Text cannot be null.");
		}

		if (contacts.isEmpty()) {
			throw new IllegalArgumentException("The set of contacts must have at least one contact.");
		}

		int key = this.workoutNextIdIncrement();
		Meeting newPastMeeting = new PastMeetingImpl(key, date, contacts, text);
		this.meetings.put(key, newPastMeeting);
	}

	// //@TODO
	// public PastMeeting addMeetingNotes(int id, String text) {

	// }

	public int addNewContact(String name, String notes) {

		if (name == "") {
			throw new IllegalArgumentException("Name cannot be an empty string.");
		}

		return 1;
	}

	// //@TODO
	// public Set<Contact> getContacts(String name) {

	// }

	// //@TODO
	// public Set<Contact> getContacts(int... ids) {

	// }

	// //@TODO
	// public void flush() {
		
	// }
}