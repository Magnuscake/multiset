package implementation;

import java.util.List;

/**
 * Array implementation of a multiset.  See comments in RmitMultiset to
 * understand what each overriden method is meant to do.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class ArrayMultiset extends RmitMultiset
{
    protected String[] array;
    
    @Override
	public void add(String elem) {
        // Implement me!
        // If array is empty
            // Add element
            // Update instance count
        // else
            // loop through the array and see if element exists
            // if element exists
                // update the instance count
            // else
                // create new element with count 1
        if (array == null) {

        }
          
        // this is just a test
        // to test the indention
    } // end of add()


    @Override
	public int search(String elem) {
        // Implement me!

        // Placeholder, please update.
        return searchFailed;
    } // end of search()


    @Override
    public List<String> searchByInstance(int instanceCount) {

        // Placeholder, please update.
        return null;
    } // end of searchByInstance


    @Override
	public boolean contains(String elem) {
        // Implement me!

        // Placeholder, please update.
        return false;
    } // end of contains()


    @Override
	public void removeOne(String elem) {
        // Implement me!
    } // end of removeOne()


    @Override
	public String print() {

        // Placeholder, please update.
        return new String();
    } // end of OrderedPrint


    @Override
	public String printRange(String lower, String upper) {

        // Placeholder, please update.
        return new String();
    } // end of printRange()


    @Override
	public RmitMultiset union(RmitMultiset other) {

        // Placeholder, please update.
        return null;
    } // end of union()


    @Override
	public RmitMultiset intersect(RmitMultiset other) {

        // Placeholder, please update.
        return null;
    } // end of intersect()


    @Override
	public RmitMultiset difference(RmitMultiset other) {

        // Placeholder, please update.
        return null;
    } // end of difference()

} // end of class ArrayMultiset
