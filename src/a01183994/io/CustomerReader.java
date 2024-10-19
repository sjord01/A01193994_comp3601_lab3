package a01183994.io;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import a01183994.data.Customer;
import a01183994.data.util.ApplicationException;
import a01183994.data.util.Validator;

import a01183994.data.util.ApplicationLogger;
import org.apache.logging.log4j.Logger;

/**
 * CustomerReader is responsible for parsing input strings and creating Customer
 * objects. It uses specific delimiters to separate records and fields within
 * the input string.
 * 
 * @author Samson James Ordonez
 * @version 1.3
 */
public class CustomerReader {
	private static final String FIELD_DELIMITER = "\\|";
	private static final String RECORD_DELIMITER = ":";
	private static final int EXPECTED_FIELD_COUNT = 9;
	private static boolean hasErrors = false; // Flag to track if any errors occur

	private static final Logger LOG = ApplicationLogger.getLogger(CustomerReader.class);

	/**
	 * Enum representing the positions of fields in the input data.
	 */
	private enum FieldPosition {
		ID, FIRSTNAME, LASTNAME, STREETNAME, CITY, POSTALCODE, PHONE, EMAIL, JOINDATE
	}

	/**
	 * Reads customer data from the input string and returns a list of Customer
	 * objects.
	 * 
	 * @param input A string containing customer data, each record separated by
	 *              RECORD_DELIMITER
	 * @return A list of Customer objects, or null if errors occurred during parsing
	 * @throws ApplicationException if there's an error in processing the input
	 */
	public static List<Customer> readCustomers(String input) throws ApplicationException {
		List<Customer> customers = new ArrayList<>();
		String[] customerStrings = input.split(RECORD_DELIMITER);

		for (String customerString : customerStrings) {
			String[] fields = customerString.split(FIELD_DELIMITER);
			if (fields.length >= EXPECTED_FIELD_COUNT) {
				Customer customer = createCustomer(fields);
				if (customer != null) {
					customers.add(customer);
				}
			} else {
				LOG.error("Insufficient data for customer: {}", customerString);
				hasErrors = true;
			}
		}

		return hasErrors ? null : customers;
	}

	/**
	 * Creates a Customer object from the given fields.
	 * 
	 * @param fields An array of field values
	 * @return A Customer object or null if creation fails due to invalid data
	 * @throws ApplicationException if there's an error in creating the customer
	 */
	private static Customer createCustomer(String[] fields) throws ApplicationException {
		final String id = getField(fields, FieldPosition.ID);
		final String phone = getField(fields, FieldPosition.PHONE);
		final String firstName = getField(fields, FieldPosition.FIRSTNAME);
		final String lastName = getField(fields, FieldPosition.LASTNAME);
		final String streetName = getField(fields, FieldPosition.STREETNAME);
		final String city = getField(fields, FieldPosition.CITY);
		final String postalCode = getField(fields, FieldPosition.POSTALCODE);
		final String email = getField(fields, FieldPosition.EMAIL);
		final String joinDate = getField(fields, FieldPosition.JOINDATE);

		// Skip validation for empty email fields
		if (email != null && !email.trim().isEmpty()) {
			try {
				// Validate the email
				Validator.validateEmail(email);
			} catch (ApplicationException e) {
				LOG.error("Invalid email for customer: {}", email);
				hasErrors = true; // Mark error flag
				return null; // Skip customer creation if email is invalid
			}
		}

		try {
			return new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName)
					.setStreetName(streetName).setCity(city).setPostalCode(postalCode).setEmail(email)
					.setJoinDate(joinDate).build();
		} catch (DateTimeParseException e) {
			LOG.error("Invalid join date format for customer: {}. Expected format: yyyyMMdd", id);
			hasErrors = true; // Mark error flag
		}
		return null; // Return null if there was an error creating the customer
	}

	/**
	 * Retrieves the field value based on the given position.
	 * 
	 * @param fields   An array of field values
	 * @param position The position of the field
	 * @return The field value as a String
	 */
	private static String getField(String[] fields, FieldPosition position) {
		return fields[position.ordinal()];
	}
}