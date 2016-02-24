import java.util.*;
// import java.util.Set;
// import java.util.Iterator;
// import java.util.LinkedHashMap;
// import java.util.Arrays;

public class ContactManagerImpl implements ContactManager {

	private LinkedHashMap<Integer, Meeting> meetings;
	private LinkedHashMap<Integer, Contact> contacts;

	private final String FILENAME = "contacts.txt"; 

	ContactManagerImpl() {
		this.meetings = new LinkedHashMap<Integer, Meeting>();
		this.contacts = new LinkedHashMap<Integer, Contact>();
	}

	/**
	 * A getter for the filename.
	 * @return String
	 * @author David Jones
	 */
	public String getFilename() {
		return this.FILENAME;
	}

	/**
	 * A getter for the meetings.
	 * @return LinkedHashMap<Integer, Meeting>
	 * @author David Jones
	 */
	public LinkedHashMap<Integer, Meeting> getMeetings() {
		return this.meetings;
	}

	/**
	 * A getter for the contacts.
	 * @return LinkedHashedMap<Integer, Contact>
	 * @author David Jones
	 */
	public LinkedHashMap<Integer, Contact> getContacts() {
		return this.contacts;
	}

	/**
	 * Works out the ID that should be assigned to the object that
	 * is being added to the map. Works out the highest ID and increments it by one.
	 *
	 * @return int
	 * @access private
	 */
	private int workoutNextIdIncrement(Set<Integer> keys) {

		int maxKey = 0;

		if (!keys.isEmpty()) {
			Iterator<Integer> iterator = keys.iterator();
			while (iterator.hasNext()) {
				Integer i = iterator.next();

				if (i > maxKey) {
					maxKey = i;
				}
			}
		}

		return maxKey + 1;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

		if (!this.checkContactsExist(contacts)) {
			throw new IllegalArgumentException("All contacts must exist in the contact manager. Use the method addNewContact to add a new contact.");
		}
		
		int key = this.workoutNextIdIncrement(this.meetings.keySet());
		Meeting newMeeting = new FutureMeetingImpl(key, date, contacts);
		this.meetings.put(key, newMeeting);

		return key;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public Meeting getMeeting(int id) {
		
		if (!this.meetings.containsKey(id)) {
			return null;
		}

		return this.meetings.get(id);
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {

		if (!this.checkContactExists(contact)) {
			throw new IllegalArgumentException("The contact does not exist. You can add a new contact using the addNewContact method.");
		}

		List<Meeting> futureMeetingList = new ArrayList<Meeting>();

		if (!this.meetings.isEmpty()) {
			Set<Integer> keys = this.meetings.keySet();
			Iterator iterator = keys.iterator();

			while (iterator.hasNext()) {
				Meeting meeting = this.meetings.get(iterator.next());
				Calendar now    = Calendar.getInstance();

				if (!meeting.getDate().before(now)) {
					Set<Contact> contacts = meeting.getContacts();

					Iterator<Contact> contactIterator = contacts.iterator();
					while (contactIterator.hasNext()) {
						Contact person = contactIterator.next();

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

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public List<Meeting> getMeetingListOn(Calendar date) {

		if (date == null) {
			throw new NullPointerException("The date cannot be null.");
		}

		List<Meeting> meetings = new ArrayList<Meeting>();

		/**
		 * In order to get matches for the date and not time we will need to set
		 * the time components to zero.
		 */
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);

		Set<Integer> keys = this.meetings.keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			Meeting meeting = this.meetings.get(iterator.next());
			Calendar meetingDate = meeting.getDate();

			meetingDate.set(Calendar.HOUR, 0);
			meetingDate.set(Calendar.MINUTE, 0);
			meetingDate.set(Calendar.SECOND, 0);

			if (meetingDate.equals(date)) {
				meetings.add(meeting);
			}
		}

		return meetings;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public List<PastMeeting> getPastMeetingListFor(Contact contact) {

		if (contact == null) {
			throw new NullPointerException("The contact cannot be null.");
		}

		List<PastMeeting> meetings = new ArrayList<PastMeeting>();

		Set<Integer> keys = this.meetings.keySet();
		Iterator meetingIterator = keys.iterator();

		while (meetingIterator.hasNext()) {
			Meeting meeting = this.meetings.get(meetingIterator.next());

			if (meeting instanceof  PastMeeting) {
				Set<Contact> contacts = meeting.getContacts();
				Iterator<Contact> contactIterator = contacts.iterator();

				while (contactIterator.hasNext()) {
					Contact meetingContact = contactIterator.next();

					if (meetingContact.getId() == contact.getId()) {
						meetings.add((PastMeeting) meeting);
					}
				}
			}
		}

		return meetings;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

		int key = this.workoutNextIdIncrement(this.meetings.keySet());
		Meeting newPastMeeting = new PastMeetingImpl(key, date, contacts, text);
		this.meetings.put(key, newPastMeeting);
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public PastMeeting addMeetingNotes(int id, String text) {
		
		if (text == null) {
			throw new NullPointerException("The notes cannot be null.");
		}

		Meeting meeting = this.getMeeting(id);

		if (meeting == null) {
			throw new IllegalArgumentException("A meeting with the ID of '" + id + "' does not exist.");
		}

		Calendar now = Calendar.getInstance();

		if (meeting.getDate().after(now)) {
			throw new IllegalStateException("The selected meeting exists but is a future meeting. An ID of a past meeting must be supplied.");
		}

		this.meetings.remove(meeting.getId());

		Meeting pastMeeting = new PastMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts(), text);
		this.meetings.put(pastMeeting.getId(), pastMeeting);

		return (PastMeeting) pastMeeting;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public int addNewContact(String name, String notes) {

		if (name == null) {
			throw new NullPointerException("Name cannot be null.");
		}

		if (notes == null) {
			throw new NullPointerException("Notes cannot be null.");
		}

		if (name == "") {
			throw new IllegalArgumentException("Name cannot be an empty string.");
		}

		if (notes == "") {
			throw new IllegalArgumentException("Notes cannot be an empty string.");
		}

		int id = this.workoutNextIdIncrement(this.contacts.keySet());

		Contact contact = new ContactImpl(id, name, notes);
		this.contacts.put(id, contact);

		return id;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
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

	/**
	 * Takes a set of contacts and loops through the saved contacts
	 * and checks whether they exist. If any one of the contacts passed
	 * into this method does not exist then false is returned. Otherwise
	 * true is returned.
	 *
	 * @param  Set<Contact> a set of contacts
	 * @return boolean
	 * @author David Jones
	 */
	private boolean checkContactsExist(Set<Contact> contacts) {

		Iterator<Contact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			Contact contact = iterator.next();

			if (!this.contacts.containsKey(contact.getId())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Takes a single contacts and checks whether the contact exists.
	 * If the contact does exist then true is returned otherwise false
	 * is returned.
	 *
	 * @param  Contact the contact object being tested
	 * @return boolean
	 * @author David Jones
	 */
	private boolean checkContactExists(Contact contact) {
		if (this.contacts.containsKey(contact.getId())) {
			return true;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 * @author David Jones
	 */
	@Override
	public void flush() {
		
	}
}