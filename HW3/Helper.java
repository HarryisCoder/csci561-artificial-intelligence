import java.util.*;
import java.util.regex.*;

public class Helper {
	
	public static Clause string2clause (String string) {
		// System.out.println("clause string: " + string);
		String[] terms;
		if (string.contains("|")) {
			// System.out.println("contains bar? " + string.contains("|"));
			terms = string.split("\\s\\|\\s");
		} else {
			terms = new String[]{string};
		}
		// System.out.println("terms.length: " + terms.length);
		Clause clause = new Clause();
		for (int i = 0; i < terms.length; i++) {
			// System.out.println("term string: " + terms[i]);
			Term term = string2term(terms[i]);
			// System.out.println("term: " + term);
			clause.addTerm(term);
		}
		return clause;
	}

	public static Term string2term (String string) {
		Pattern p = Pattern.compile("^(.*?)\\(");
 		Matcher m = p.matcher(string);
 		// System.out.println("name found: " + m.find());
 	// 	System.out.println("name: " + m.group(1));
 		String name = "";
 		if (m.find()) {
			name = m.group(1);
			// System.out.println("name: " + m.group(1));
		}
		// String name = "";
		p = Pattern.compile("\\((.*?)\\)");
 		m = p.matcher(string);
 		// System.out.println("args found: " + m.find());
 		// System.out.println("args: " + m.group(1));
 		String argstr = "";
 		if (m.find()) {
			argstr = m.group(1);
		}
 		String[] arguments;
 		if (argstr.contains(",")) {
			arguments = argstr.split(",");
		} else {
			arguments = new String[]{argstr};
		}
		return new Term(name, arguments);
	}
}