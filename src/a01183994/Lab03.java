package a01183994;

import org.apache.logging.log4j.Logger;

import a01183994.data.util.ApplicationException;
import a01183994.data.util.ApplicationLogger;
import a01183994.io.CustomerReport;

public class Lab03 {

    private String customerData;
    private static final Logger LOG = ApplicationLogger.getLogger(CustomerReport.class);

    public static void main(String[] args) {
        if (args.length != 1) {
        	LOG.error("Error: Please provide exactly one argument containing customer data.");
            System.exit(-1);
        }

        new Lab03(args[0]).run();
    }

    public Lab03(String customerData) {
        this.customerData = customerData;
    }

    private void run() {
        try {
            CustomerReport.generateReport(customerData);
        } catch (ApplicationException e) {
        	LOG.error("An error occurred: " + e.getMessage());
        }
    }
}