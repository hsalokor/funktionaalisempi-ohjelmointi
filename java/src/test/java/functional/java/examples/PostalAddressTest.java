package functional.java.examples;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class PostalAddressTest {
	private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
	private final static ContactInformation postalAddress = new PostalAddress(ADDRESS);
	
	@Test
	public void firstLineIsStreetAddress() {
		assertEquals("Testitie 5", postalAddress.streetAddress());
	}

	@Test
	public void firstWordOfSecondLineIsPostCode() {
		assertEquals("00999", postalAddress.postCode());
	}
	
	@Test
	public void restOfWordsOfSecondLineIsPostOffice() {
		assertEquals("OLEMATON", postalAddress.postOffice());
	}
}
