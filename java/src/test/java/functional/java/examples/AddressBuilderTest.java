package functional.java.examples;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class AddressBuilderTest {
	private final static String STREET = "Testitie 5";
	private final static String POST_CODE = "00999";
	private final static String POST_OFFICE = "OLEMATON";

	@Test
	public void buildAddressWithTestData() {
		ContactInformation address = new AddressBuilder().withStreetAddress(STREET)
							.withPostCode(POST_CODE)
							.withPostOffice(POST_OFFICE)
							.build();
		assertEquals("Testitie 5", address.streetAddress());
		assertEquals("00999", address.postCode());
		assertEquals("OLEMATON", address.postOffice());
	}
}
