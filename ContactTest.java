import static org.junit.Assert.*;
import org.junit.*;

public class ContactTest {

	@Test(expected=IllegalArgumentException.class)
	public void testConstructContactImplWithAZeroID() {
		Contact contact = new ContactImpl(0, "David Jones", "My notes");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructContactWithANegativeId() {
		Contact contact = new ContactImpl(-1, "David Jones", "My Notes");
	}

	@Test(expected=NullPointerException.class)
	public void testConstructContactWithNullName() {
		Contact contact = new ContactImpl(1, null, "My Notes");
	}

	@Test(expected=NullPointerException.class)
	public void testConstructContactWithNullNotes() {
		Contact contact = new ContactImpl(1, "David Jones", null);
	}

	@Test(expected=NullPointerException.class)
	public void testConstructContactWithNullNameAlternateConstructor() {
		Contact contact = new ContactImpl(1, null);
	}

	@Test
	public void testIdIsSetCorrectly() {
		Contact contact = new ContactImpl(1, "David Jones", "My Notes");

		assertEquals("The ID did not match the once passed into the constructor", 1, contact.getId());
	}

	@Test
	public void testNameIsSetCorrectly() {
		Contact contact = new ContactImpl(1, "David Jones", "My Notes");

		assertEquals("Name did not match the once passed into the constructor", "David Jones", contact.getName());
	}

	@Test
	public void testNotesIsSetCorrectly() {
		Contact contact = new ContactImpl(1, "David Jones", "My Notes");

		assertEquals("Notes did not match the once passed into the constructor", "My Notes", contact.getNotes());
	}

	@Test
	public void testNotesIsEmptyStringIfNotSet() {
		Contact contact = new ContactImpl(1, "David Jones");

		assertEquals("Notes did not return an empty string when no value for notes was passed into the constructor", "", contact.getNotes());
	}
}	