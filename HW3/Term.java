import java.util.*;

public class Term {
	private String name;
	private String[] arguments;

	Term(String name, String[] arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	Term(Term term) {
		this.name = term.name;
		this.arguments = term.arguments.clone();
	}

	public String getName() {
		return this.name;
	}

	public String[] getArgs() {
		return this.arguments;
	}

	@Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof Term)) {
            return false;
        }

        Term term = (Term) obj;

        return term.name.equals(this.name) && Arrays.equals(term.arguments, this.arguments);
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        for (int i = 0; i < this.arguments.length; i++) {
        	result += this.arguments[i].hashCode() * (i + 1); 
        }
        return result;
    }

    @Override
    public String toString() {
    	String str = this.name + "(";
    	for (int i = 0; i < this.arguments.length; i++) {
        	str += this.arguments[i];
        	if (i != this.arguments.length - 1) {
        		str += ",";
        	}
        }
        str += ")";
        return str;
    }
}