import java.util.*;

public class Clause {
	private Set<Term> terms;

	Clause() {
		this.terms = new HashSet<Term>();
	}

	Clause(Set<Term> terms) {
		this.terms = new HashSet<Term>();
		for (Term term : terms) {
			this.terms.add(new Term(term));
		}
	}

	public void addTerm(Term term) {
		this.terms.add(term);
	}

	public void removeTerm(Term term) {
		this.terms.remove(term);
	}

	public void addClause(Clause clause) {
		this.terms.addAll(clause.getTerms());
	}

	public Set<Term> getTerms() {
		return this.terms;
	}

	@Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof Clause)) {
            return false;
        }

        Clause clause = (Clause) obj;

        Set<Term> setA = new HashSet<>(this.getTerms());
        Set<Term> setB = new HashSet<>(clause.getTerms());

        return setA.removeAll(clause.getTerms()) && setB.removeAll(this.getTerms());
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Term term : this.getTerms()) {
        	result += term.hashCode(); 
        }
        return result;
    }

    @Override
    public String toString() {
    	String str = "";
    	int count = 0;
    	for (Term term : this.getTerms()) {
        	str += count == 0 ? term.toString() : (" | " + term.toString());
        	count++;
        }
        return str;
    }
}
