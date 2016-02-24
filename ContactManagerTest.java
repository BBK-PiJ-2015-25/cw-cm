import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
// import java.util.Calendar;
// import java.util.Set;
// import java.util.HashSet;

public class ContactManagerTest {

	private ContactManager contactManager;
	private Set<Contact>   contacts;
	private Contact 	   contact;
	private Calendar 	   aFutureDate;
	private Calendar 	   aPastDate;

	@Before
	public void setUp() {
		Calendar futureDate = Calendar.getInstance();
		futureDate.add(Calendar.YEAR, 1);
		this.aFutureDate = futureDate;

		Calendar pastDate = Calendar.getInstance();
		pastDate.set(2010, 01, 01);
		this.aPastDate   = pastDate;

		ContactManager contactManager = new ContactManagerImpl();
		int contactId = contactManager.addNewContact("David Jones", "Some notes");

		this.contactManager = contactManager;
		this.contacts = contactManager.getContacts("David Jones");

		Set<Contact> contacts  = contactManager.getContacts(contactId);
		Object[] contactsArray = contacts.toArray();
		this.contact = (Contact) contactsArray[0];
	}

	@Test(expected=NullPointerException.class)
	public void testAddFutureMeetingWithNullContacts() {
		this.contactManager.addFutureMeeting(null, this.aFutureDate);
	}

	@Test(expected=NullPointerException.class)
	public void testAddFutureMeetingWithNullDate() {
		this.contactManager.addFutureMeeting(this.contacts, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingWithPastDate() {
		this.contactManager.addFutureMeeting(this.contacts, this.aPastDate);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingWithAEmptySetOfContacts() {
		Set<Contact> contacts = new HashSet<Contact>();
		this.contactManager.addFutureMeeting(contacts, this.aFutureDate);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingWithNonExistingContact() {
		this.contacts.add(new ContactImpl(999, "Unknown", "This contact doesn't exist."));
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
	}

	@Test
	public void testCorrectReturnValueFromAddFutureMeetingWithValidInput() {
		int newKey = this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		assertEquals("The ID for the new future meeting was expected to be 1.", 1, newKey);
	}

	@Test
	public void testCorrectReturnValueFromAddFutureMeetingMultipleMeetingsAdded() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		int newKey = this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		assertEquals("The ID for the new future meeting was expected to be 6.", 6, newKey);
	}

	@Test
	public void testGetPastMeetingWithAnIdThatDoesntExist() {
		PastMeeting pastMeeting = this.contactManager.getPastMeeting(99);

		assertEquals("The value for past meeting was expected to be null.", null, pastMeeting);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetPastMeetingWithAFutureMeetingId() {
		int futureMeetingId = this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		PastMeeting pastMeeting = this.contactManager.getPastMeeting(futureMeetingId);
	}

	@Test
	public void testGetPastMeetingWithMeetingThatHasADateInThePast() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		PastMeeting pastMeeting = this.contactManager.getPastMeeting(1);

		// The addNewPastMethod does not return an ID so we need to assert by part of the meeting
		assertEquals("The past meeting added did not match the past meeting returned.", "My Notes", pastMeeting.getNotes());
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutContacts() {
		this.contactManager.addNewPastMeeting(null, this.aPastDate, "Hello Dave");
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutDate() {
		this.contactManager.addNewPastMeeting(this.contacts, null, "Hello Dave");
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutText() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNexPastMeetingWithEmptyContactSet() {
		Set<Contact> contacts = new HashSet<Contact>();

		this.contactManager.addNewPastMeeting(contacts, this.aPastDate, "My Notes");
	}

	@Test
	public void testAddNexPastMeetingWithValidData() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Hello Dave");
	}

	@Test
	public void testGetFutureMeetingNonExistantMeeting() {
		FutureMeeting futureMeeting = this.contactManager.getFutureMeeting(1);

		assertEquals("Future meeting return value should be null.", futureMeeting, null);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetFutureMeetingTargetMeetingInPast() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		FutureMeeting futureMeeting = this.contactManager.getFutureMeeting(1);
	}

	@Test
	public void testGetFutureMeetingValidMeeting() {
		int futureMeetingId 		= this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		FutureMeeting futureMeeting = this.contactManager.getFutureMeeting(futureMeetingId);

		assertEquals("Future meeting did not match the meeting that was added.", futureMeetingId, futureMeeting.getId());
	}

	@Test
	public void testGetMeetingWithAMeetingThatDoesntExist() {
		Meeting meeting = this.contactManager.getMeeting(999);

		assertEquals("Meeting should be none existent and null should have been returned.", meeting, null);
	}

	@Test
	public void testGetMeetingWithAValidId() {
		int futureMeetingId = this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		Meeting meeting 	= this.contactManager.getMeeting(futureMeetingId);

		assertEquals("Meeting should have the same ID of the meeting we added.", futureMeetingId, meeting.getId());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetMeetingWithANonExistingContact() {
		Contact contact = new ContactImpl(999, "Unknown", "This contact doesn't exist.");
		List<Meeting> meetings = this.contactManager.getFutureMeetingList(contact);
	}

	@Test
	public void testGetFutureMeetingListWithNoSavedMeetings() {
		List<Meeting> meetings = this.contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the list to be empty.", 0, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithOnlySavedPastMeetings() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		List<Meeting> meetings = this.contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the list to be empty.", 0, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithExistingFutureMeetings() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		List<Meeting> meetings = this.contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the number of meetings to match 3.", 3, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithAMixOfPastAndFutureMeetings() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		List<Meeting> meetings = this.contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the number of meetings to match 4.", 4, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWhereTheContactIsNotPartOfEveryMeeting() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		int id = this.contactManager.addNewContact("Some guy", "Some notes");
		Set<Contact> contacts = this.contactManager.getContacts("");

		this.contactManager.addFutureMeeting(contacts, this.aFutureDate);

		Contact newContact = this.contactManager.getContacts(id).iterator().next();
		List<Meeting> meetings = this.contactManager.getFutureMeetingList(newContact);

		assertEquals("We were expecting the number of meetings to match 1.", 1, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListInCorrectDateOrder() {
		GregorianCalendar first  = new GregorianCalendar(2017, 01, 01);
		GregorianCalendar second = new GregorianCalendar(2017, 01, 02);
		GregorianCalendar third  = new GregorianCalendar(2017, 01, 03);

		this.contactManager.addFutureMeeting(this.contacts, third);
		this.contactManager.addFutureMeeting(this.contacts, first);
		this.contactManager.addFutureMeeting(this.contacts, second);

		List<Meeting> meetings = this.contactManager.getFutureMeetingList(this.contact);

		Meeting firstMeeting = meetings.get(0);
		Meeting secondMeeting = meetings.get(1);
		Meeting thirdMeeting = meetings.get(2);

		boolean success = true;

		if (!firstMeeting.getDate().equals(first)) {
			success = false;
		}

		if (!secondMeeting.getDate().equals(second)) {
			success = false;
		}

		if (!thirdMeeting.getDate().equals(third)) {
			success = false;
		}

		assertEquals("The meetings were not in order", true, success);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewContactEmptyStringName() {
		this.contactManager.addNewContact("", "Some Notes");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewContactEmptyStringNotes() {
		this.contactManager.addNewContact("David Jones", "");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewContactNullName() {
		this.contactManager.addNewContact(null, "Some Notes");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewContactNullNotes() {
		this.contactManager.addNewContact("David Jones", null);
	}

	@Test
	public void testAddNewContact() {
		ContactManager contactManager = new ContactManagerImpl();
		int id = contactManager.addNewContact("David Jones", "Some Notes");

		assertEquals("Adding new contact returns the wrong ID, we were expecting an ID of 1.", 1, id);
	}

	@Test
	public void testAddNewContactCheckIdIncrementsCorrectly() {
		ContactManager contactManager = new ContactManagerImpl();

		int firstId  = contactManager.addNewContact("David Jones", "Some Notes");
		int secondId = contactManager.addNewContact("David Jones", "Some Notes");
		int thirdId  = contactManager.addNewContact("David Jones", "Some Notes");

		assertEquals("Adding new contacts does not increment the ID properly.", 3, thirdId);
	}

	@Test(expected=NullPointerException.class)
	public void testGetContactsNullName() {
		String name = null;
		Set<Contact> contacts = this.contactManager.getContacts(name);
	}

	@Test
	public void testGetContactsEmptyList() {
		ContactManager contactManager = new ContactManagerImpl();
		Set<Contact> contacts = contactManager.getContacts("");

		assertEquals("The returned contacts list should be empty.", true, contacts.isEmpty());
	}

	@Test
	public void testGetContactsReturnAll() {
		ContactManager contactManager = new ContactManagerImpl();

		int firstId  = contactManager.addNewContact("David Jones", "Some Notes");
		int secondId = contactManager.addNewContact("David Jones", "Some Notes");
		int thirdId  = contactManager.addNewContact("David Jones", "Some Notes");

		Set<Contact> contacts = contactManager.getContacts("");

		assertEquals("Total number of contacts returned should be 3.", 3, contacts.size());
	}

	@Test
	public void testGetContactsByName() {
		ContactManager contactManager = new ContactManagerImpl();

		int firstId  = contactManager.addNewContact("David Jones", "Some Notes");
		int secondId = contactManager.addNewContact("David Jones", "Some Notes");
		int thirdId  = contactManager.addNewContact("David", "Some Notes");

		Set<Contact> contacts = contactManager.getContacts("David Jones");

		assertEquals("Total number of contacts returned should be 2.", 2, contacts.size());
	}

	@Test(expected=NullPointerException.class)
	public void testGetContactsWithNoIds() {
		int[] ids = null;
		Set<Contact> contacts = this.contactManager.getContacts(ids);
	}

	@Test
	public void testGetContactsWithIdsButNoContactsExistYet() {
		ContactManager contactManager = new ContactManagerImpl();
		int[] ids = new int[2];
		ids[0]    = 1;
		ids[1]    = 2;

		Set<Contact> contacts = contactManager.getContacts(ids);

		assertEquals("The contacts set should be empty.", true, contacts.isEmpty());
	}

	@Test
	public void testGetContactsWithIdsThatExist() {
		ContactManager contactManager = new ContactManagerImpl();
		int contactIdOne   = contactManager.addNewContact("David Jones", "Some Notes");
		int contactIdTwo   = contactManager.addNewContact("John Smith", "Some Notes");
		int contactIdThree = contactManager.addNewContact("Jane Doe", "Some Notes");

		int[] ids = new int[3];
		ids[0] = contactIdOne;
		ids[1] = contactIdTwo;
		ids[2] = contactIdThree;

		Set<Contact> contacts = contactManager.getContacts(ids);

		assertEquals("The contacts set should have size of 3.", 3, contacts.size());
	}

	@Test(expected=NullPointerException.class)
	public void testGetMeetingListOnWithNullDate() {
		this.contactManager.getMeetingListOn(null);
	}

	@Test
	public void testGetMeetingListOnWhereNoMeetingsHaveBeenSet() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, 1);

		List<Meeting> meetings = this.contactManager.getMeetingListOn(date);

		assertEquals("We were expecting an empty list of meetings as non have been set yet.", true, meetings.isEmpty());
	}

	@Test
	public void testGetMeetingListOnWhereMeetingsExistButDoNotMatchDate() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Some Notes");

		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, 1);

		List<Meeting> meetings = this.contactManager.getMeetingListOn(date);

		assertEquals("We are expecting an empty list of meetings as with a matching date been set yet.", true, meetings.isEmpty());
	}

	@Test
	public void testGetMeetingListOnWhereMeetingsExistAndMatchDate() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, 1);

		this.contactManager.addFutureMeeting(this.contacts, date);
		this.contactManager.addFutureMeeting(this.contacts, date);
		this.contactManager.addFutureMeeting(this.contacts, date);

		List<Meeting> meetings = this.contactManager.getMeetingListOn(date);

		assertEquals("We are expecting 3 meetings to be returned.", 3, meetings.size());
	}

	@Test(expected=NullPointerException.class)
	public void testGetPastMeetingListForNullContact() {
		this.contactManager.getPastMeetingListFor(null);
	}

	@Test
	public void testGetPastMeetingListForNoMeetings() {
		Contact contact = new ContactImpl(1, "David Jones", "Some Notes");
		List<PastMeeting> pastMeetings = this.contactManager.getPastMeetingListFor(contact);

		assertEquals("We are expecting an empty list of past meetings.", true, pastMeetings.isEmpty());
	}

	@Test
	public void testGetPastMeetingListForOnlyFutureMeetings() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		List<PastMeeting> pastMeetings = this.contactManager.getPastMeetingListFor(this.contact);

		assertEquals("The list should be empty as we have no past meetings.", true, pastMeetings.isEmpty());
	}

	@Test
	public void testGetPastMeetingListForMixOfFutureAndPastMeetings() {
		this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Some notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Some notes");
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Some notes");

		List<PastMeeting> pastMeetings = this.contactManager.getPastMeetingListFor(this.contact);

		assertEquals("The list should have three items inside.", 3, pastMeetings.size());
	}

	@Test(expected=NullPointerException.class)
	public void testAddMeetingNotesWithNullNotes() {
		this.contactManager.addMeetingNotes(1, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddMeetingNotesMeetingDoesntExist() {
		this.contactManager.addMeetingNotes(1, "Some notes");
	}

	@Test(expected=IllegalStateException.class)
	public void testAddMeetingNotesMeetingInFuture() {
		int futureMeetingId = this.contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		this.contactManager.addMeetingNotes(futureMeetingId, "Some notes");

	}

	@Test
	public void testAddMeetingNotesMeetingInPast() {
		this.contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Old notes");
		PastMeeting pastMeetingWithNotes = this.contactManager.addMeetingNotes(1, "New notes");

		assertEquals("The notes were not changed to the new version.", "New notes", pastMeetingWithNotes.getNotes());
	}
}	