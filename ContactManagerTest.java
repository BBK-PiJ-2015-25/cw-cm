import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
// import java.util.Calendar;
// import java.util.Set;
// import java.util.HashSet;

public class ContactManagerTest {

	private Contact contact;
	private Set<Contact> contacts;
	private Calendar aFutureDate;
	private Calendar aPastDate;

	@Before
	public void setUp() {
		Calendar futureDate = Calendar.getInstance();
		futureDate.add(Calendar.YEAR, 1);
		Calendar pastDate   = Calendar.getInstance();
		pastDate.set(2010, 01, 01);

		this.aFutureDate = futureDate;
		this.aPastDate   = pastDate;
		this.contacts    = new HashSet<Contact>();
		Contact person   = new ContactImpl(1, "David Jones");

		this.contact = person;
		this.contacts.add(person);
	}

	@Test(expected=NullPointerException.class)
	public void testAddFutureMeetingWithNullContacts() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(null, this.aFutureDate);
	}

	@Test(expected=NullPointerException.class)
	public void testAddFutureMeetingWithNullDate() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(this.contacts, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingWithPastDate() {
		Calendar pastDate = Calendar.getInstance();
		pastDate.set(2014, 01, 01);

		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(this.contacts, pastDate);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingWithAEmptySetOfContacts() {
		Set<Contact> contacts = new HashSet<Contact>();
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(contacts, this.aFutureDate);
	}

	@Test
	public void testCorrectReturnValueFromAddFutureMeetingWithValidInput() {
		ContactManager contactManager = new ContactManagerImpl();
		int newKey = contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		assertEquals("The ID for the new future meeting was expected to be 1.", 1, newKey);
	}

	@Test
	public void testCorrectReturnValueFromAddFutureMeetingMultipleMeetingsAdded() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		int newKey = contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		assertEquals("The ID for the new future meeting was expected to be 6.", 6, newKey);
	}

	@Test
	public void testGetPastMeetingWithAnIdThatDoesntExist() {
		ContactManager contactManager = new ContactManagerImpl();
		PastMeeting pastMeeting 	  = contactManager.getPastMeeting(99);

		assertEquals("The value for past meeting was expected to be null.", null, pastMeeting);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetPastMeetingWithAFutureMeetingId() {
		ContactManager contactManager = new ContactManagerImpl();
		int futureMeetingId = contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		PastMeeting pastMeeting = contactManager.getPastMeeting(futureMeetingId);
	}

	@Test
	public void testGetPastMeetingWithMeetingThatHasADateInThePast() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		PastMeeting pastMeeting = contactManager.getPastMeeting(1);

		// The addNewPastMethod does not return an ID so we need to assert by part of the meeting
		assertEquals("The past meeting added did not match the past meeting returned.", "My Notes", pastMeeting.getNotes());
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutContacts() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(null, this.aPastDate, "Hello Dave");
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutDate() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, null, "Hello Dave");
	}

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutText() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNexPastMeetingWithEmptyContactSet() {
		ContactManager contactManager = new ContactManagerImpl();
		Set<Contact> contacts = new HashSet<Contact>();

		contactManager.addNewPastMeeting(contacts, this.aPastDate, "My Notes");
	}

	@Test
	public void testAddNexPastMeetingWithValidData() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Hello Dave");
	}

	@Test
	public void testGetFutureMeetingNonExistantMeeting() {
		ContactManager contactManager = new ContactManagerImpl();
		FutureMeeting futureMeeting = contactManager.getFutureMeeting(1);

		assertEquals("Future meeting return value should be null.", futureMeeting, null);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetFutureMeetingTargetMeetingInPast() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		FutureMeeting futureMeeting = contactManager.getFutureMeeting(1);
	}

	@Test
	public void testGetFutureMeetingValidMeeting() {
		ContactManager contactManager = new ContactManagerImpl();
		int futureMeetingId = contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		FutureMeeting futureMeeting = contactManager.getFutureMeeting(futureMeetingId);

		assertEquals("Future meeting did not match the meeting that was added.", futureMeetingId, futureMeeting.getId());
	}

	@Test
	public void testGetMeetingWithAMeetingThatDoesntExist() {
		ContactManager contactManager = new ContactManagerImpl();
		Meeting meeting = contactManager.getMeeting(999);

		assertEquals("Meeting should be none existent and null should have been returned.", meeting, null);
	}

	@Test
	public void testGetMeetingWithAValidId() {
		ContactManager contactManager = new ContactManagerImpl();
		int futureMeetingId = contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		Meeting meeting = contactManager.getMeeting(futureMeetingId);

		assertEquals("Meeting should have the same ID of the meeting we added.", futureMeetingId, meeting.getId());
	}

	@Test
	public void testGetFutureMeetingListWithNoSavedMeetings() {
		ContactManager contactManager = new ContactManagerImpl();
		List<Meeting> meetings = contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the list to be empty.", 0, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithOnlySavedPastMeetings() {
		ContactManager contactManager = new ContactManagerImpl();

		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		List<Meeting> meetings = contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the list to be empty.", 0, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithExistingFutureMeetings() {
		ContactManager contactManager = new ContactManagerImpl();

		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		List<Meeting> meetings = contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the number of meetings to match 3.", 3, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWithAMixOfPastAndFutureMeetings() {
		ContactManager contactManager = new ContactManagerImpl();

		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "My Notes");

		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		List<Meeting> meetings = contactManager.getFutureMeetingList(this.contact);

		assertEquals("We were expecting the number of meetings to match 4.", 4, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListWhereTheContactIsNotPartOfEveryMeeting() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addFutureMeeting(this.contacts, this.aFutureDate);

		HashSet<Contact> contacts = new HashSet<Contact>();
		Contact newContact = new ContactImpl(2, "Some guy");
		contacts.add(newContact);

		contactManager.addFutureMeeting(contacts, this.aFutureDate);

		List<Meeting> meetings = contactManager.getFutureMeetingList(newContact);

		assertEquals("We were expecting the number of meetings to match 1.", 1, meetings.size());
	}

	@Test
	public void testGetFutureMeetingListInCorrectDateOrder() {
		ContactManager contactManager = new ContactManagerImpl();

		GregorianCalendar first  = new GregorianCalendar(2017, 01, 01);
		GregorianCalendar second = new GregorianCalendar(2017, 01, 02);
		GregorianCalendar third  = new GregorianCalendar(2017, 01, 03);

		contactManager.addFutureMeeting(this.contacts, third);
		contactManager.addFutureMeeting(this.contacts, first);
		contactManager.addFutureMeeting(this.contacts, second);

		List<Meeting> meetings = contactManager.getFutureMeetingList(this.contact);

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
	public void testAddNewContactNullName() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewContact("", "Some Notes");
	}
}	