package functional.java.examples;

import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class AddressTransformerTest {
	private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
	private final static String MISSING_CITY = "Testitie 5\n00999OLEMATON";
	private final static String MISSING_SECOND_LINE = "FUBAR";

	@Test
	public void hasAddressFields() {
		final ContactInformation address = new AddressTransformer().apply(ADDRESS);
		assertEquals("Testitie 5", address.streetAddress());
		assertEquals("00999", address.postCode());
		assertEquals("OLEMATON", address.postOffice());
	}

	@Test
	public void hasNoContactInformationWithMissingCity() {
		final ContactInformation address = new AddressTransformer().apply(MISSING_CITY);
		assertEquals(NO_CONTACT_INFORMATION, address);
	}

	@Test
	public void hasNoContactInformationWithMissingSecondLine() {
		final ContactInformation address = new AddressTransformer().apply(MISSING_SECOND_LINE);
		assertEquals(NO_CONTACT_INFORMATION, address);
	}
}
