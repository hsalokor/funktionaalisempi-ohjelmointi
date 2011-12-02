package functional.java.examples;

public class AddressBuilder {
	private String buildStreetAddress;
	private String buildPostCode;
	private String buildPostOffice;

	public AddressBuilder withStreetAddress(String streetAddress) {
		buildStreetAddress = streetAddress;
		return this;
	}

	public AddressBuilder withPostCode(String postCode) {
		buildPostCode = postCode;
		return this;
	}

	public AddressBuilder withPostOffice(String postOffice) {
		buildPostOffice = postOffice;
		return this;
	}

	public Address build() {
		return new Address(buildStreetAddress, buildPostCode, buildPostOffice);
	}
}
