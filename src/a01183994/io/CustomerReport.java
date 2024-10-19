package a01183994.io;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

import a01183994.data.Customer;
import a01183994.data.util.ApplicationException;
import a01183994.data.util.ApplicationLogger;
import a01183994.data.util.CustomerReportUtils;

/**
 * The CustomerReport class is responsible for generating a formatted report of
 * customer data. It uses a list of ReportColumn objects to define the structure
 * and content of the report.
 * 
 * @author Samson James Ordonez
 * @version 1.1
 */
public class CustomerReport {
	private static final String REPORT_TITLE = "Customer Report";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String UTC_ZONE = "UTC";
	private static final String DURATION_FORMAT = "Duration: %d milliseconds";
	private static final String DASH = "-";
	private static final String EMPTY_SPACE = " ";

	private static final Logger LOG = ApplicationLogger.getLogger(CustomerReport.class);

	/**
	 * Inner class representing a column in the customer report.
	 */
	private static class ReportColumn {
		final String header;
		final int width;
		final Function<Customer, String> valueExtractor;

		private static final int ID_WIDTH = 12;
		private static final int NAME_WIDTH = 13;
		private static final int STREET_WIDTH = 26;
		private static final int CITY_WIDTH = 13;
		private static final int POSTAL_CODE_WIDTH = 15;
		private static final int PHONE_WIDTH = 16;
		private static final int EMAIL_WIDTH = 26;
		private static final int JOIN_DATE_WIDTH = 11;

		/**
		 * Constructs a ReportColumn with the specified header, width, and value
		 * extractor.
		 *
		 * @param header         The header text for the column
		 * @param width          The width of the column
		 * @param valueExtractor A function to extract the value from a Customer object
		 */
		ReportColumn(String header, int width, Function<Customer, String> valueExtractor) {
			this.header = header;
			this.width = width;
			this.valueExtractor = valueExtractor;
		}

		// List of columns defined for the report
		static final List<ReportColumn> COLUMNS = List.of(
				new ReportColumn("#. ID", ID_WIDTH, customer -> CustomerReportUtils.formatId(customer.getId())),
				new ReportColumn("First name", NAME_WIDTH, Customer::getFirstName),
				new ReportColumn("Last name", NAME_WIDTH, Customer::getLastName),
				new ReportColumn("Street", STREET_WIDTH, customer -> CustomerReportUtils.truncateAndReplace(customer.getStreetName(), STREET_WIDTH)),
				new ReportColumn("City", CITY_WIDTH, Customer::getCity),
				new ReportColumn("Postal Code", POSTAL_CODE_WIDTH, Customer::getPostalCode),
				new ReportColumn("Phone", PHONE_WIDTH, customer -> CustomerReportUtils.formatPhoneNumber(customer.getPhone())),
				new ReportColumn("Email", EMAIL_WIDTH, Customer::getEmail),
				new ReportColumn("Join Date",JOIN_DATE_WIDTH, c -> CustomerReportUtils.formatJoinDate(c.getJoinDate())));
	}

	/**
	 * Generates a customer report based on the provided input string.
	 * 
	 * @param input A string containing customer data
	 * @throws ApplicationException If an error occurs during report generation
	 */
	public static void generateReport(String input) throws ApplicationException {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of(UTC_ZONE));
		Instant startTime = Instant.now();

		System.out.println(startTime.atZone(ZoneId.systemDefault()).format(formatter));

		List<Customer> customers = CustomerReader.readCustomers(input);

		if (customers != null && !customers.isEmpty()) {
			customers.sort(new CompareByJoinedDate());
			System.out.println(generateReportContent(customers));
		}

		Instant endTime = Instant.now();
		System.out.println(endTime.atZone(ZoneId.systemDefault()).format(formatter));
		System.out.println(String.format(DURATION_FORMAT, Duration.between(startTime, endTime).toMillis()));
		LOG.info("Report generation completed");
	}

	/**
	 * Generates the content of the report for the given list of customers.
	 * 
	 * @param customers The list of customers to include in the report
	 * @return A formatted string containing the report content
	 */
	private static String generateReportContent(List<Customer> customers) {
		return Stream
				.concat(Stream.of(REPORT_TITLE, createSeparator(), formatHeaders(), createSeparator()),
						customers.stream().map(CustomerReport::formatCustomer))
				.collect(Collectors.joining(LINE_SEPARATOR));
	}

	/**
	 * Creates a separator line for the report.
	 * 
	 * @return A string of dashes with length equal to the total width of all
	 *         columns
	 */
	private static String createSeparator() {
		return DASH.repeat(ReportColumn.COLUMNS.stream().mapToInt(col -> col.width).sum());
	}

	/**
	 * Formats the headers for all columns in the report.
	 * 
	 * @return A formatted string containing all column headers
	 */
	private static String formatHeaders() {
		return ReportColumn.COLUMNS.stream().map(col -> String.format("%-" + col.width + "s", col.header))
				.collect(Collectors.joining(EMPTY_SPACE));
	}

	/**
	 * Formats a single customer's data for the report.
	 * 
	 * @param customer The customer to format
	 * @return A formatted string containing the customer's data
	 */
	private static String formatCustomer(Customer customer) {
		return ReportColumn.COLUMNS.stream()
				.map(col -> String.format("%-" + col.width + "s",
						CustomerReportUtils.truncate(col.valueExtractor.apply(customer), col.width)))
				.collect(Collectors.joining(EMPTY_SPACE));
	}
}