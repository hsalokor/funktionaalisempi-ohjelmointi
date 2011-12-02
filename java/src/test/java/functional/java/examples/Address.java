package functional.java.examples;

public class Address implements ContactInformation {
	private final String streetAddress;
	private final String postCode;
	private final String postOffice;

	public Address(String streetAddress, String postCode, String postOffice) {
		this.streetAddress = streetAddress;
		this.postCode = postCode;
		this.postOffice = postOffice;
	}

	@Override
	public String streetAddress() {
		return streetAddress;
	}

	@Override
	public String postCode() {
		return postCode;
	}

	@Override
	public String postOffice() {
		return postOffice;
	}
}
