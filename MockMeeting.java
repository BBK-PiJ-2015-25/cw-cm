import java.util.Calendar;
import java.util.Set;

/**
 * A mock class so we can access the methods of the Meeting class
 * when we are unit testing.
 */
public class MockMeeting extends MeetingImpl {

	MockMeeting(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
	}
}