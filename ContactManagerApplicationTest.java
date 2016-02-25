public class ContactManagerApplicationTest {

	public static void main(String[] args) {
		System.out.println("Contact manager application has begun");

		ContactManager cm = new ContactManagerImpl();

		cm.addContact("David Jones", "Notes");

		cm.addFutureMeeting();
	}
}