public class ContactManagerImpl implements ContactManager {

	// @TODO
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		return 1;
	}

	// @TODO
	public PastMeeting getPastMeeting(int id) {
		return new PastMeeting();
	}

	//@TODO
	public FutureMeeting getFutureMeeting(int id) {
		return new FutureMeeting();
	}

	public Meeting getMeeting(int id) {
		return new Meeting();
	}
	 //@TODO
	public List<Meeting> getFutureMeetingList(Contact contact) {

	}

	//@TODO
	public List<Meeting> getMeetingListOn(Calendar date) {

	}

	//@TODO
	public List<Meeting> getPastMeetingListFor(Contact contact) {

	}

	//@TODO
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {

	}

	//@TODO
	public PastMeeting addMeetingNotes(int id, String text) {

	}

	//@TODO
	public int addNewContact(String name, String notes) {

	}

	//@TODO
	public Set<Contact> getContacts(String name) {

	}

	//@TODO
	public Set<Contact> getContacts(int... ids) {

	}

	//@TODO
	public void flush() {
		
	}
}