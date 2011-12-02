package functional.java.examples;

import static functional.java.examples.StringList.*;

public class PostalAddress implements ContactInformation {
	private final String address;

	public PostalAddress(String address) {
		this.address = address;
	}

	@Override
	public String streetAddress() {
		return First.of(Lines.from(address));
	}

	@Override
	public String postCode() {
		return firstWord(Second.of(Lines.from(address)));
	}

	@Override
	public String postOffice() {
		return secondWord(Second.of(Lines.from(address)));
	}
}
