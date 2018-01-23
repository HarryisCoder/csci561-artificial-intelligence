import java.util.*;

public class Problem {
	List<Clause> queries;
	Set<Clause> kb;

	Problem(List<Clause> queries, Set<Clause> kb) {
		this.queries = queries;
		this.kb = kb;
	}
}