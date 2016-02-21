import java.util.*;

/**
 * This is the comparison test for organizing a list by
 * its date value. 
 */
public class MeetingComparator implements Comparator<Meeting> {

	@Override
	public int compare(Meeting meeting1, Meeting meeting2) {
        return meeting1.getDate().compareTo(meeting2.getDate());
    }
}