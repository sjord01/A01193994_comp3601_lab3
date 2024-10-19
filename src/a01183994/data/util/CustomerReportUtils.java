package a01183994.data.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting and manipulating customer data for reporting
 * purposes. This class provides static methods to format phone numbers, IDs,
 * join dates, and to truncate strings for display in reports.
 *
 * @author Samson James Ordonez
 * @version 1.0
 */
public class CustomerReportUtils {
	private static final String EMPTY_STRING = "";
	private static final String SPACE = " ";
	private static final String ELLIPSIS = "...";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	/**
	 * Truncates a string to a specified maximum length, adding an ellipsis if
	 * necessary.
	 *
	 * @param s         The string to truncate
	 * @param maxLength The maximum length of the resulting string
	 * @return The truncated string, or an empty string if the input is null
	 */
	public static String truncate(String s, int maxLength) {
		if (s == null)
			return EMPTY_STRING;
		return s.length() <= maxLength ? s : s.substring(0, maxLength - 3) + ELLIPSIS;
	}

	/**
	 * Truncates a string to a specified maximum length, replacing multiple
	 * whitespace characters with a single space and adding an ellipsis if
	 * necessary.
	 *
	 * @param s         The string to truncate and format
	 * @param maxLength The maximum length of the resulting string
	 * @return The truncated and formatted string, or an empty string if the input
	 *         is null
	 */
	public static String truncateAndReplace(String s, int maxLength) {
		if (s == null)
			return EMPTY_STRING;
		return truncate(s.replaceAll("\\s+", SPACE), maxLength);
	}

	/**
	 * Formats a phone number string into a standard format (XXX) XXX-XXXX.
	 *
	 * @param phone The phone number to format
	 * @return The formatted phone number, or the original string if it doesn't
	 *         contain 10 digits
	 */
	public static String formatPhoneNumber(String phone) {
		String digitsOnly = phone.replaceAll("\\D", EMPTY_STRING);
		if (digitsOnly.length() != 10)
			return phone;
		return String.format("(%s) %s-%s", digitsOnly.substring(0, 3), digitsOnly.substring(3, 6),
				digitsOnly.substring(6));
	}

	/**
	 * Formats an ID string into a specific format: [integer]. [6-digit zero-padded
	 * integer]
	 *
	 * @param id The ID to format
	 * @return The formatted ID string
	 */
	public static String formatId(String id) {
		return String.format("%d. %06d", Integer.parseInt(id), Integer.parseInt(id));
	}

	/**
	 * Formats a LocalDate object into a string using the format "yyyyMMdd".
	 *
	 * @param joinDate The LocalDate object to format
	 * @return The formatted date string, or an empty string if the input is null
	 */
	public static String formatJoinDate(LocalDate joinDate) {
		if (joinDate == null) {
			return EMPTY_STRING;
		}
		return joinDate.format(DATE_FORMATTER);
	}
}