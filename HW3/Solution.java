import java.util.*;

public class Solution {
	private Clause query;
	private Set<Clause> kb;
	private boolean result;
	private String resultStr;
	private long start;
	private long timeLimit;

	Solution(Clause query, Set<Clause> kb) {
		this.query = query;
		this.kb = kb;
		// this.result = true;
		// this.resultStr = "NA";
		this.timeLimit = 5000; // 5 seconds
	}

	private void proveQuery() {
		Iterator<Term> iter = query.getTerms().iterator();
		Term term = iter.next();
		// System.out.println("negateName: " + negate(term.getName()));
		Term nTerm = new Term(negate(term.getName()), term.getArgs());
		// System.out.println("nTerm: " + nTerm);
		Clause nQuery = new Clause();
		nQuery.addTerm(nTerm);
		kb.add(nQuery); // add negation to KB
		// System.out.println("KB add nTerm: ");
		// System.out.println(kb);
		this.result = resolveKB();
	}

	private String negate(String termName) {
		if (termName.charAt(0) == '~') {
			return termName.substring(1);
		} else {
			return "~" + termName;
		}
	}

	private boolean resolveKB() {
		// System.out.println("-------------------------------------");
		// System.out.println("resolving KB ...");
		List<Clause> kbList = new ArrayList<Clause>(this.kb);
		Set<Clause> resolvents = new HashSet<>();
		int n = kbList.size();
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				Clause ci = kbList.get(i);
				Clause cj = kbList.get(j);
				// Result rst = resolveClause(ci, cj);
				Set<Clause> tempResolvents = resolveClause(ci, cj);
				// if (rst.resolvents != null) {
					// System.out.println("Can resolve!");
				if (tempResolvents.size() != 0) {
					// System.out.println("CLAUSE#1: " + ci);
					// System.out.println("CLAUSE#2: " + cj);
					// System.out.println("RESOLVENTS: ");
					// System.out.println(tempResolvents);
					Iterator<Clause> iter = tempResolvents.iterator();
					Clause firstClause = iter.next();
					if (firstClause.getTerms().size() == 0) {
						// System.out.println("Found empty resolvent!");
						// this.resultStr = "TRUE";
						return true;
					}
					resolvents.addAll(tempResolvents);
					// System.out.println();
				}
				// } else {
					// System.out.println("Cannot resolve!");
				// }
			}
		}
		if (resolvents.size() == 0) {
			// System.out.println("No more new clause!");
			// this.resultStr = "FALSE";
			return false;
		} else {
			return keepResolve(resolvents);
		}
	}

	private Set<Clause> resolveClause(Clause ci, Clause cj) {
		// boolean resolvable = false;
		Set<Clause> resolvents = new HashSet<>();
		List<Term> termsi = new ArrayList<>(ci.getTerms());
		List<Term> termsj = new ArrayList<>(cj.getTerms());
		for (int i = 0; i < termsi.size(); i++) {
			for (int j = 0; j < termsj.size(); j++) {
				Term termi = termsi.get(i);
				Term termj = termsj.get(j);
				// System.out.println("[DEBUG]term pair: " + termi + "|" + termj);
				if (negateEachOther(termi, termj)) {
					// System.out.println("Find negaiton: " + termi + "|" + termj);
					Clause resolvent = resolveTerms(termi, termj, ci, cj);
					if (resolvent != null && !this.kb.contains(resolvent)) {
						resolvents.add(resolvent);
					}
				}
			}
		}
		// return new Result(resolvable, resolvents);
		return resolvents;
	}

	private boolean negateEachOther(Term termi, Term termj) {
		return termi.getName().equals("~" + termj.getName()) || termj.getName().equals("~" + termi.getName());
	}

	private Clause resolveTerms(Term termi, Term termj, Clause ci, Clause cj) {
		Clause resolvent = new Clause();
		String[] argA = termi.getArgs();
		String[] argB = termj.getArgs();
		// System.out.println("termi: " + termi);
		// System.out.println("termj: " + termj);

		Map<String, Set<String>> mapA = new HashMap<>();
		Map<String, Set<String>> mapB = new HashMap<>();
		Set<Character> sameVars = new HashSet<>();
		for (int i = 0; i < argA.length; i++) {
			if (isVariable(argA[i]) && isVariable(argB[i])) {
				if ( !argA[i].equals(argB[i])) {
					return null;
				} else {
					sameVars.add(argA[i].charAt(0));
				}
			} else if (!isVariable(argA[i]) && !isVariable(argB[i]) && !argA[i].equals(argB[i])) {
				return null;
			} else if (isVariable(argA[i]) && !isVariable(argB[i])) {
				if (mapA.containsKey(argA[i])) {
					mapA.get(argA[i]).add(argB[i]);
				} else {
					Set<String> set = new HashSet<>();
					set.add(argB[i]);
					mapA.put(argA[i], set);
				}
			} else if (isVariable(argB[i]) && !isVariable(argA[i])){
				if (mapA.containsKey(argB[i])) {
					mapA.get(argB[i]).add(argA[i]);
				} else {
					Set<String> set = new HashSet<>();
					set.add(argA[i]);
					mapB.put(argB[i], set);
				}
			}
		}
		// System.out.println("MapA: " + mapA);
		// System.out.println("MapB: " + mapB);
		// check if one variable assigned to more than one constants
		Clause newCi = new Clause(ci.getTerms());
		Clause newCj = new Clause(cj.getTerms());
		newCi.removeTerm(termi);
		newCj.removeTerm(termj);
		for (Map.Entry<String, Set<String>> entry : mapA.entrySet()) {
			if (entry.getValue().size() > 1) {
				// System.out.println("One variable cannot be assigned to more than one constants!");
				return null;
			}
			Iterator<String> iter = entry.getValue().iterator();
			String constant = iter.next();
			unifyClause(newCi, entry.getKey(), constant);
			// System.out.println("Ci after copy: " + ci);
			// System.out.println("newCi after unification: " + newCi);
		}
		for (Map.Entry<String, Set<String>> entry : mapB.entrySet()) {
			if (entry.getValue().size() > 1) {
				// System.out.println("One variable cannot be assigned to more than one constants!");
				return null;
			}
			Iterator<String> iter = entry.getValue().iterator();
			String constant = iter.next();
			unifyClause(newCj, entry.getKey(), constant);
			// System.out.println("Cj after copy: " + cj);
			// System.out.println("newCj after unification: " + newCj);
		}
		//
		standardize(newCi, newCj, sameVars);
		resolvent.addClause(newCi);
		resolvent.addClause(newCj);
		// System.out.println("RESOLVENT: " + resolvent);

		return resolvent;
	}

	private void standardize(Clause newCi, Clause newCj, Set<Character> sameVars) {
		boolean[] letters = new boolean[26];
		Set<Term> termsi = newCi.getTerms();
		for (Term term : termsi) {
			String[] args = term.getArgs();
			for (int i = 0; i < args.length; i++) {
				if (isVariable(args[i])) {
					char var = args[i].charAt(0);
					letters[(int)(var - 'a')] = true;
				}
			}
		}
		Set<Term> termsj = newCj.getTerms();
		Set<Character> replacedChars = new HashSet<>();
		for (Term term : termsj) {
			String[] args = term.getArgs();
			for (int i = 0; i < args.length; i++) {
				if (isVariable(args[i])) {
					char var = args[i].charAt(0);
					if (letters[(int)(var - 'a')] == true && !sameVars.contains(var) && !replacedChars.contains(var)) {
						int j = 0;
						while (letters[j] == true) {
							j++;
						}
						letters[j] = true;
						char newVar = (char)('a' + j);
						replacedChars.add(newVar);
						unifyClause(newCj, Character.toString(var), Character.toString(newVar));
						// System.out.println("[DEBUG]newCj after standardization: " + newCj);
					}
				}
			}
		}
	}

	private void unifyClause(Clause clause, String var, String con) {
		Set<Term> terms = clause.getTerms();
		for (Term term : terms) {
			String[] args = term.getArgs();
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals(var)) {
					args[i] = con;
				}
			}
		}
	};

	private boolean isVariable(String argument) {
		char c = argument.charAt(0);
		return (int)c >= (int)'a' && (int)c <= (int)'z';
	}

	private boolean keepResolve(Set<Clause> resolvents) {
		// while (resolvents.size() != 0 && !containEmpty(resolvents)) {
		while (resolvents.size() != 0) {
			// System.out.println("-------------------------------------");
			// System.out.println("new KB: ");
			// System.out.println(this.kb);
			// System.out.println("Total resolvents: ");
			// System.out.println(resolvents);
			// System.out.println("-------------------------------------");
			// System.out.println("keep resolving KB ...");
			List<Clause> resolventList = new ArrayList<Clause>(resolvents);
			int m = resolventList.size();
			Set<Clause> newResolvents = new HashSet<>();
			for (int i = 0; i < m; i++) {
				Clause ci = resolventList.get(i);
				List<Clause> kbList = new ArrayList<Clause>(this.kb);
				int n = kbList.size();
				for (int j = 0; j < n; j++) {
					Clause cj = kbList.get(j);
					// Result rst = resolveClause(ci, cj);
					Set<Clause> tempResolvents = resolveClause(ci, cj);
					if (System.currentTimeMillis() - this.start > this.timeLimit) {
						// System.out.println("Time over!");
						return false;
					}
					// if (rst.resolvents != null) {
						// System.out.println("Can resolve!");
					if (tempResolvents.size() != 0) {
						// System.out.println("CLAUSE#1: " + ci);
						// System.out.println("CLAUSE#2: " + cj);
						// System.out.println("RESOLVENTS: ");
						// System.out.println(tempResolvents);
						Iterator<Clause> iter = tempResolvents.iterator();
						Clause firstClause = iter.next();
						if (firstClause.getTerms().size() == 0) {
							// System.out.println("Found empty resolvent!");
							// this.resultStr = "TRUE";
							return true;
						}
						newResolvents.addAll(tempResolvents);
						// System.out.println();
					}
					
					// } else {
						// System.out.println("Cannot resolve!");
					// }
				}
				this.kb.add(ci);
			}
			resolvents = newResolvents;
		}
		if (resolvents.size() == 0) {
			// System.out.println("No more new clause!");
			// this.resultStr = "FALSE";
			return false;
		} else {
			// System.out.println("Contains empty!");
			// this.resultStr = "TRUE";
			return true;
		}
	}

	private boolean containEmpty(Set<Clause> resolvents) {
		for (Clause c : resolvents) {
			if (c.getTerms().size() == 0) {
				return true;
			}
		}
		return false;
	}

	public String getResult() {
		this.start = System.currentTimeMillis();
		proveQuery();
		return this.result ? "TRUE" : "FALSE";
		// return this.resultStr;
	}

	private class Result {
		private boolean resolvable;
		private Set<Clause> resolvents;

		Result(boolean resolvable, Set<Clause> resolvents) {
			this.resolvable = resolvable;
			this.resolvents = resolvents;
		}
	}

}