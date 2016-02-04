import static org.junit.Assert.*;
import org.junit.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class MeetingTest {

	private Calendar aDate;
	private Set<Contact> contacts;

	@Before
	public void setUp() {
		this.aDate = Calendar.getInstance();
		this.contacts = new HashSet<Contact>();

		Contact person = new ContactImpl(1, "David Jones");
		this.contacts.add(person);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testContructorWithANegativeId() {
		Meeting meeting = new MockMeeting(-1, this.aDate, this.contacts);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testContructorWithAZeroID() {
		Meeting meeting = new MockMeeting(0, this.aDate, this.contacts);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testContructorWithANullDate() {
		Meeting meeting = new MockMeeting(1, null, this.contacts);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testContructorWithANullContact() {
		Meeting meeting = new MockMeeting(1, this.aDate, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testContructorWithAnEmptyContactSet() {
		Set<Contact> contacts = new HashSet<Contact>();
		Meeting meeting 	  = new MockMeeting(1, this.aDate, contacts);
	}

	@Test
	public void testGetIdReturnsTheCorrectValue() {
		Meeting meeting = new MockMeeting(300, this.aDate, this.contacts);
		assertEquals("The ID passed into the constructor does not match", 300, meeting.getId());
	}

	@Test
	public void testGetDateReturnsTheCorrectValue() {
		Meeting meeting = new MockMeeting(1, this.aDate, this.contacts);
		assertEquals("The date passed into the constructor does not match", this.aDate, meeting.getDate());
	}

	@Test
	public void testGetContactsReturnsTheCorrectValue() {
		Meeting meeting = new MockMeeting(1, this.aDate, this.contacts);
		assertEquals("The contacts set passed into the constructor does not match", this.contacts, meeting.getContacts());
	}
}