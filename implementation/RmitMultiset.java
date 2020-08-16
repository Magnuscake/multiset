package implementation;

import java.util.List;

/**
 * Abstract class for a multiset, this sets the interface for a multiset for this
 * assignment.  Provided skeleton code extends all multiset implementation class
 * from this abstract class.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public abstract class RmitMultiset
{
	/** Constant to indicate that a search operation has failed. */
	public static final int searchFailed = -1;


	/**
	 * Add an element into multiset.
	 *
	 * @param elem Element to add.
	 */
	public abstract void add(String elem);


	/**
	 * Searches for an element in the multiset.
	 *
	 * @param elem Element to search for.
	 *
	 * @return The number of instance of the element in the multiset.  If element is not in the multiset, return 0.
	 */
	public abstract int search(String elem);


	/**
	 * Return all elements that have the input instance count.
     *
     * @param instanceCount Number of instances that we are seeking elements for.
     *
     * @return List of elements that have the specified number of instances.
	 */
	public abstract List<String> searchByInstance(int instanceCount);


	/**
	 * Checks if an element is the multiset.
	 *
	 * @param elem Element to search for.
	 *
	 * @return Whether the element is in the multiset.
	 */
	public abstract boolean contains(String elem);


	/**
	 * Remove one instance of element from the multiset.  If element doesn't exist, method just returns.
	 *
	 * @param elem Element to remove.
	 */
	public abstract void removeOne(String elem);


	/**
	 * Print out the elements, ordered by their number of instances.  If two or more elements have
	 * the same number of instances, their relative ordering doesn't matter, i.e, their order among
	 * themselves can be any, as long as they appear before (and after) elements that have less instances
	 * (more instances).
	 *
	 * @return String to print out.
	 */
	public abstract String print();


	/**
	 * Print out a subset of the elements.  The subset should contain all elements
	 * that are alphabetically greater than or equal to lower and less than or equal
	 * to upper.  The elements can be printed in any order.
     *
     * @param lower Lower boundary.
     * @param upper Upper boundary.
     *
     * @return String to print out.
	 */
	public abstract String printRange(String lower, String upper);


	/**
	 * Compute the union between this and other multisets.
	 *
	 * @param other 2nd multiset to compute union on.
	 *
	 * @return New multiset that results from the union of this and other multiset.
	 */
	public abstract RmitMultiset union(RmitMultiset other);


	/**
	 * Compute the intersection between this and other multisets.
	 *
	 * @param other 2nd multiset to compute intersection on.
	 *
	 * @return New multiset that results from the intersection of this and other multiset.
	 */
	public abstract RmitMultiset intersect(RmitMultiset other);


	/**
	 * Compute the difference between this and other multiset.  Difference is those
	 * elements in this multiset, subtract the elements in the other multiset.
	 *
	 * @return New multiset that results from taking the difference between this and other multiset.
	 */
	public abstract RmitMultiset difference(RmitMultiset other);

} // end of abstract class RmitMultiset
