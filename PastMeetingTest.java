import static org.junit.Assert.*;
import org.junit.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class PastMeetingTest {

	private Calendar aDate;
	private Set<Contact> contacts;

	@Before
	public void setUp() {
		this.aDate = Calendar.getInstance();
		this.contacts = new HashSet<Contact>();

		Contact person = new ContactImpl(1, "David Jones");
		this.contacts.add(person);
	}

	@Test(expected=NullPointerException.class)
	public void testNullNotesPassedIntoConstructor() {
		Meeting meeting = new PastMeetingImpl(1, this.aDate, this.contacts, null);
	}

	@Test
	public void testGetNotesReturnsTheCorrectValue() {
		PastMeeting meeting = new PastMeetingImpl(1, this.aDate, this.contacts, "My Notes");
		assertEquals("The notes passed into the constructor does not match", meeting.getNotes(), "My Notes");
	}
}