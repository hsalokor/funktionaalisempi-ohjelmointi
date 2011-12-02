package functional.java.examples;

import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
import static functional.java.examples.StringList.*;

import java.util.List;

import com.google.common.base.Function;

public class AddressTransformer implements Function<String, ContactInformation> {
	@Override
	public ContactInformation apply(String input) {
		try {
			return toAddress(Lines.from(input));
		} catch (ArrayIndexOutOfBoundsException e) {
			return NO_CONTACT_INFORMATION;
		}
	}

	private Address toAddress(final List<String> addressLines) {
		AddressBuilder addressBuilder = new AddressBuilder().withStreetAddress(First.of(addressLines));
		addressBuilder.withPostCode(firstWord(Second.of(addressLines)));
		addressBuilder.withPostOffice(secondWord(Second.of(addressLines)));
		return addressBuilder.build();
	}
}
