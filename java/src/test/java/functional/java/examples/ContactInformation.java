package functional.java.examples;

public interface ContactInformation {
	public static final ContactInformation NO_CONTACT_INFORMATION = new NoContactInformation();

	String streetAddress();

	String postCode();

	String postOffice();

	public static final class NoContactInformation implements ContactInformation {
		@Override
		public String streetAddress() {
			return "";
		}

		@Override
		public String postCode() {
			return "";
		}

		@Override
		public String postOffice() {
			return "";
		}
	}
}
