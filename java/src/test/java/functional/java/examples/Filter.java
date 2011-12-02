package functional.java.examples;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	public List<String> apply(List<String> values, Condition<String> predicate) {
		ArrayList<String> output = new ArrayList<String>();
		for (String string : values) {
			if (predicate.apply(string)) {
				output.add(string);
			}
		}
		return output;
	}

	public interface Condition<T> {
		public boolean apply(T input);
	}
}
