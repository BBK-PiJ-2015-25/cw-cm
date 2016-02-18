import static org.junit.Assert.*;
import org.junit.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class ContactManagerTest {

	private Set<Contact> contacts;
	private Calendar aFutureDate;
	private Calendar aPastDate;

	@Before
	public void setUp() {
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(2016, 12, 01);
		Calendar pastDate   = Calendar.getInstance();
		pastDate.set(2010, 01, 01);

		this.aFutureDate = futureDate;
		this.aPastDate   = pastDate;
		this.contacts    = new HashSet<Contact>();
		Contact person   = new ContactImpl(1, "David Jones");

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

	// @TODO - need to test and implement the add new past meeting method
	// @Test(expected=IllegalStateException.class)
	// public void testGetPastMeetingWithMeetingThatHasADateInThePast() {
	// 	ContactManager contactManager = new ContactManagerImpl();
	// 	Calendar pastDate 			  = Calendar.getInstance();
	// 	pastDate.set(2010, 01, 01);

	// 	int pastMeetingId = contactManager.addFutureMeeting(this.contacts, this.aPastDate);

	// 	PastMeeting pastMeeting = contactManager.getPastMeeting(pastMeetingId);
	// }

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

	@Test(expected=NullPointerException.class)
	public void testAddNexPastMeetingWithoutEmptyContactSet() {
		ContactManager contactManager = new ContactManagerImpl();
		Set<Contact> contacts = new HashSet<Contact>();

		contactManager.addNewPastMeeting(contacts, this.aPastDate, null);
	}

	@Test
	public void testAddNexPastMeetingWithValidData() {
		ContactManager contactManager = new ContactManagerImpl();
		contactManager.addNewPastMeeting(this.contacts, this.aPastDate, "Hello Dave");
	}
}	