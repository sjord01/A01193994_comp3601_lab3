package a01183994.io;

import java.util.Comparator;
import a01183994.data.Customer;

/**
 * CompareByJoinedDate provides a comparator to sort Customer objects by their joinDate in ascending order.
 * 
 * @author Samson James Ordonez
 * @version 1.0
 */
public class CompareByJoinedDate implements Comparator<Customer> {

    /**
     * Compares two Customer objects based on their joinDate.
     *
     * @param customer1 The first Customer to compare.
     * @param customer2 The second Customer to compare.
     * @return A negative integer, zero, or a positive integer as the first argument is less than,
     *         equal to, or greater than the second.
     */
    @Override
    public int compare(Customer customer1, Customer customer2) {
        if (customer1.getJoinDate() == null && customer2.getJoinDate() == null) {
            return 0;
        }
        if (customer1.getJoinDate() == null) {
            return -1; // Assuming null joinDate comes before non-null
        }
        if (customer2.getJoinDate() == null) {
            return 1; // Assuming null joinDate comes before non-null
        }
        return customer1.getJoinDate().compareTo(customer2.getJoinDate());
    }
}
