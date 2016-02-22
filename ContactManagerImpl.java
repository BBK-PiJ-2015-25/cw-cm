import java.util.*;
// import java.util.Set;
// import java.util.Iterator;
// import java.util.LinkedHashMap;
// import java.util.Arrays;

public class ContactManagerImpl implements ContactManager {

	private LinkedHashMap<Integer, Meeting> meetings;
	private LinkedHashMap<Integer, Contact> contacts; 

	ContactManagerImpl() {
		this.meetings = new LinkedHashMap<Integer, Meeting>();
		this.contacts = new LinkedHashMap<Integer, Contact>();
	}

	/**
	 * Works out the ID that should be assigned to the meeting that
	 * is being added to the set. Takes the most recent ID (which will be
	 * the key) and increments it by one.
	 *
	 * @return int
	 * @access private
	 */
	private int workoutNextIdIncrementForMeetings() {
		Set<Integer> keys      = this.meetings.keySet();
		Object[]     keysArray = keys.toArray();

		int key = 1;
		if (!keys.isEmpty()) {
			key = (int) keysArray[keys.size() - 1] + 1;
		}

		return key;
	}

	/**
	 * Works out the ID that should be assigned to the contact that
	 * is being added to the Map. Takes the most recent ID (which will be
	 * the key) and increments it by 1.
	 *
	 * @return int
	 * @access private
	 */
	private int workoutNextIdIncrementForContacts() {
		Set<Integer> keys      = this.contacts.keySet();
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
		
		int key = this.workoutNextIdIncrementForMeetings();
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

		int key = this.workoutNextIdIncrementForMeetings();
		Meeting newPastMeeting = new PastMeetingImpl(key, date, contacts, text);
		this.meetings.put(key, newPastMeeting);
	}

	// //@TODO
	// public PastMeeting addMeetingNotes(int id, String text) {

	// }

	public int addNewContact(String name, String notes) {

		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}

		if (notes == null) {
			throw new IllegalArgumentException("Notes cannot be null.");
		}

		if (name == "") {
			throw new IllegalArgumentException("Name cannot be an empty string.");
		}

		if (notes == "") {
			throw new IllegalArgumentException("Notes cannot be an empty string.");
		}

		int id = this.workoutNextIdIncrementForContacts();

		Contact contact = new ContactImpl(id, name, notes);
		this.contacts.put(id, contact);

		return id;
	}

	public Set<Contact> getContacts(String name) {

		if (name == null) {
			throw new NullPointerException("The contact name cannot be null.");
		}

		Set<Contact> contacts = new HashSet<Contact>();
		Set<Integer> keys     = this.contacts.keySet();
		Iterator iterator     = keys.iterator();

		while (iterator.hasNext()) {
			Contact contact    = this.contacts.get(iterator.next());
			String contactName = contact.getName();

			if (name.equals("") || contactName.equals(name)) {
				contacts.add(contact);
			}
		}

		return contacts;
	}

	public Set<Contact> getContacts(int... ids) {

		if (ids == null) {
			throw new NullPointerException("The list of ids cannot be null.");
		}

		Set<Contact> contacts = new HashSet<Contact>();
		int idsLength = ids.length;

		for (int i = 0; i < idsLength; i++) {
			if (this.contacts.containsKey(ids[i])) {
				contacts.add(this.contacts.get(ids[i]));
			}
		}

		return contacts;
	}

	// //@TODO
	// public void flush() {
		
	// }
}