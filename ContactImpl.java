import java.io.Serializable;

class ContactImpl implements Contact, Serializable {

	private int id;
	private String name;
	private String notes;

	ContactImpl(int id, String name, String notes) {

		if (id < 1) {
			throw new IllegalArgumentException("ID must be an integer with a value more than 0" + id + " was passed.");
		}

		if (name == null) {
			throw new NullPointerException("Name must not be null.");
		}

		if (notes == null) {
			throw new NullPointerException("Notes must not be null.");
		}

		this.id    = id;
		this.name  = name;
		this.notes = notes;
	}

	ContactImpl(int id, String name) {

		if (id < 1) {
			throw new IllegalArgumentException("ID must be an integer with a value more than 0");
		}

		if (name == null) {
			throw new NullPointerException("Name must not be null.");
		}

		this.id    = id;
		this.name  = name;
		this.notes = null;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getNotes() {
		if (this.notes != null) {
			return this.notes;
		} else {
			return "";
		}
	}

	public void addNotes(String notes) {

	}
}