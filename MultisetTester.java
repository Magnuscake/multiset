import java.io.*;

// needed for interface
import java.util.List;
// needed for multiset container
import java.util.HashMap;
import java.util.Map;
// needed for printing purposes
import java.util.StringJoiner;

// DO NOT USE any OR ADD other java.util.* packages and classes
// UNLESS it is specified in the official assignment 1 FAQ

import implementation.*;


/**
 * Framework to test the multiset implementations.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class MultisetTester
{
	/** Name of class, used in error messages. */
	protected static final String progName = "MultisetTester";


	/**
	 * Print help/usage message.
	 */
	public static void usage(String progName) {
		printErrorMsg(progName + ": <implementation> [(optional) fileName to output results to]");
		printErrorMsg("<implementation> = <array | orderedlinkedlist | bst | duallinkedlist>");
		System.exit(1);
	} // end of usage


	/**
	 * Print error message to System.err.
	 */
	public static void printErrorMsg(String msg) {
		System.err.println("> " + msg);
	} // end of printErrorMsg()


	/**
	 * Print error message to specified outWriter.
	 *
	 * @param outWriter PrintWriter to write error messages to.
	 * @param msg Error message.
	 */
	public static void printErrorMsg(PrintWriter outWriter, String msg) {
		outWriter.println("> " + msg);
	} // end of printErrorMsg()


	/**
	 * Process the operation commands coming from inReader, and updates the multiset according to the operations.
	 *
	 * @param inReader Input reader where the operation commands are coming from.
	 * @param outWriter Where to output the results of search and print related operations.
	 * @param creator Factory class to construct appropriate multiset instance.
	 * @param multisetd Map of id and multiset, used to store the multisets created and operated upon.
	 *
	 * @throws IOException If there is an exception to do with I/O.
	 */
	public static void processOperations(BufferedReader inReader, PrintWriter outWriter, MultisetImplCreator creator, Map<String, RmitMultiset> multisets)
		throws IOException
	{
		String line;
		boolean bQuit = false;

		// continue reading in commands until we either receive the quit signal or there are no more input commands
		while (!bQuit && (line = inReader.readLine()) != null) {
			// tokens can be separted by one or more whitespaces
			String[] tokens = line.split("\\s+");

			// check if there is at least an operation command
			if (tokens.length < 1) {
				printErrorMsg("not enough tokens.");
				continue;
			}

			String command = tokens[0];
			// determine which operation to execute
			switch (command) {
				// list the multisets
				case "list":
					outWriter.println("# " + line);
					for (String multisetId : multisets.keySet()) {
						outWriter.println(multisetId);
					}
					break;
				// create a new multiset
				case "create":
					if (tokens.length == 2) {
						String id = tokens[1];
						// check if multiset id used already
						if (multisets.containsKey(id)) {
							printErrorMsg("multiset identifier " + id + " is used currently.");
						}
						else {
							RmitMultiset newMultiset = creator.createImplementation();
							multisets.put(id, newMultiset);
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// add an element to a multiset
				case "add":
					if (tokens.length == 3) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							multiset.add(tokens[2]);
						}
						else {
							printErrorMsg("operation failed, id not found.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// search for an element in a multiset
				case "search":
					outWriter.println("# " + line);
					if (tokens.length == 3) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							int foundNumber = multiset.search(tokens[2]);
							if (foundNumber == RmitMultiset.searchFailed) {
								outWriter.println(tokens[2] + " not found");
							}
							else {
								outWriter.println(tokens[2] + " " + foundNumber);
							}
						}
						else {
							// we print -1 to indicate error for automated testing
							outWriter.println(RmitMultiset.searchFailed);
							printErrorMsg(outWriter, "operation failed, id not found.");
						}
					}
					else {
						// we print -1 to indicate error for automated testing
						outWriter.println(RmitMultiset.searchFailed);
						printErrorMsg(outWriter, "not enough tokens.");
					}
					break;
				// search a multset for all elements that have a specified number of instances
				case "searchByInstance":
					outWriter.println("# " + line);
					if (tokens.length == 3) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							int instanceToSearch = Integer.valueOf(tokens[2]);
							if (instanceToSearch > 0) {
								List<String> lItems = multiset.searchByInstance(instanceToSearch);

								if (lItems != null) {
									// output
									StringJoiner joiner = new StringJoiner(",");
									for (String item : lItems) {
										joiner.add(item);
									}
									outWriter.println(joiner.toString());
								}
								else {
									outWriter.println(RmitMultiset.searchFailed);
									printErrorMsg(outWriter, "operation failed, null returned.");
								}
							}
							else {
								// we print -1 to indicate error for automated testing
								outWriter.println(RmitMultiset.searchFailed);
								printErrorMsg(outWriter, "operation failed, instance number must be greater than 0.");
							}
						}
						else {
							// we print -1 to indicate error for automated testing
							outWriter.println(RmitMultiset.searchFailed);
							printErrorMsg(outWriter, "operation failed, id not found.");
						}
					}
					else {
						// we print -1 to indicate error for automated testing
						outWriter.println(RmitMultiset.searchFailed);
						printErrorMsg(outWriter, "not enough tokens.");
					}
					break;
				case "contains":
					outWriter.println("# " + line);
					if (tokens.length == 3) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							boolean bFound = multiset.contains(tokens[2]);
							if (bFound) {
								outWriter.println(tokens[2] + " is in multiset");
							}
							else {
								outWriter.println(tokens[2] + " is not in multiset");
							}
						}
						else {
							// we print -1 to indicate error for automated testing
							outWriter.println(RmitMultiset.searchFailed);
							printErrorMsg(outWriter, "operation failed, id not found.");
						}
					}
					else {
						// we print -1 to indicate error for automated testing
						outWriter.println(RmitMultiset.searchFailed);
						printErrorMsg(outWriter, "not enough tokens.");
					}
					break;
				// remove one instance of an element from a multiset
				case "removeOne":
					if (tokens.length == 3) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							multiset.removeOne(tokens[2]);
						}
						else {
							printErrorMsg("operation failed, id not found.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// print the elements in a multiset, in descending order of the number of instances in multiset
				case "print":
					outWriter.println("# " + line);
					if (tokens.length == 2) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							String sOut = multiset.print();
							outWriter.print(sOut);
							outWriter.flush();
						}
						else {
							printErrorMsg("operation failed, id not found.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// print all elements of a multiset that are within specified range
				case "printRange":
					outWriter.println("# " + line);
					if (tokens.length == 4) {
						RmitMultiset multiset = multisets.get(tokens[1]);
						if (multiset != null) {
							String sOut = multiset.printRange(tokens[2], tokens[3]);
							outWriter.print(sOut);
							outWriter.flush();
						}
						else {
							printErrorMsg("operation failed, id not found.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// compute the intersection of two multisets
				case "intersect":
					if (tokens.length == 4) {
						RmitMultiset multiset1 = multisets.get(tokens[1]);
						RmitMultiset multiset2 = multisets.get(tokens[2]);
						if (multiset1 != null && multiset2 != null && !multisets.containsKey(tokens[3])) {
							RmitMultiset newMultiset = multiset1.intersect(multiset2);
							if (newMultiset != null) {
								multisets.put(tokens[3], newMultiset);
							}
						}
						else {
							printErrorMsg("operation failed, id not found or duplicate id for new multiset.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// compute the union of two multisets
				case "union":
					if (tokens.length == 4) {
						RmitMultiset multiset1 = multisets.get(tokens[1]);
						RmitMultiset multiset2 = multisets.get(tokens[2]);
						if (multiset1 != null && multiset2 != null && !multisets.containsKey(tokens[3])) {
							RmitMultiset newMultiset = multiset1.union(multiset2);
							if (newMultiset != null) {
								multisets.put(tokens[3], newMultiset);
							}
						}
						else {
							printErrorMsg("operation failed, id not found or duplicate id for new multiset.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// compute the difference between two multisets
				case "difference":
					if (tokens.length == 4) {
						RmitMultiset multiset1 = multisets.get(tokens[1]);
						RmitMultiset multiset2 = multisets.get(tokens[2]);
						if (multiset1 != null && multiset2 != null && !multisets.containsKey(tokens[3])) {
							RmitMultiset newMultiset = multiset1.difference(multiset2);
							if (newMultiset != null) {
								multisets.put(tokens[3], newMultiset);
							}
						}
						else {
							printErrorMsg("operation failed, id not found or duplicate id for new multiset.");
						}
					}
					else {
						printErrorMsg("not enough tokens.");
					}
					break;
				// quit
				case "quit":
					bQuit = true;
					break;
				default:
					printErrorMsg("Unknown command.");
			}

		} // end of while

	} // end of processOperations()


	/**
	 * Main method.  Determines which implementation to test.
	 */
	public static void main(String[] args) {

		// check number of command line arguments
		if (args.length > 2 || args.length < 1) {
			printErrorMsg("Incorrect number of arguments.");
			usage(progName);
		}

		String implementationType = args[0];

		String outFilename = null;
		if (args.length == 2) {
			outFilename = args[1];
		}


		// Construct multiset container and the factory object to create multiset
		Map<String, RmitMultiset> hMultisets =  new HashMap<String, RmitMultiset>();
		// Factory
		try {
			MultisetImplCreator creator = new MultisetImplCreator(implementationType);

			// construct in and output streams/writers/readers, then process each operation.
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter outWriter = new PrintWriter(System.out, true);

			if (outFilename != null) {
				outWriter = new PrintWriter(new FileWriter(outFilename), true);
			}

			// process the operations
			processOperations(inReader, outWriter, creator, hMultisets);
		} catch (IllegalArgumentException e) {
				printErrorMsg(e.getMessage());
				usage(progName);
		}
		catch (IOException e) {
			printErrorMsg(e.getMessage());
		}

	} // end of main()

} // end of class MultisetTester
