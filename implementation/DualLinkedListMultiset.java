package implementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Dual linked list implementation of a multiset.  See comments in RmitMultiset to
 * understand what each overriden method is meant to do.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class DualLinkedListMultiset extends RmitMultiset
{
    // Reference to the head node of first set
    protected Node headOne;
    // Reference to the head node of second set
    protected Node headTwo;
    // Reference to the length of first set
    protected int length;



    public DualLinkedListMultiset() {
        headOne = null;
        headTwo = null;
        length = 0;
    }

    @Override
	public void add(String item) {
        Node newNode = new Node(item);

        if (headOne == null) {
            headOne = newNode;
            headTwo = newNode;

        } else {
            // Track previous node of current node in set one
            Node prevNodeOne = null;
            // Track previous node of current node in set two
            Node prevNodeTwo = null;
            
            Node currNodeOne = headOne;
            Node currNodeTwo = headTwo;

            boolean createNewItem = true;

            while (currNodeTwo != null) {
                if (currNodeTwo.getItem().compareTo(item) == 0) {
                    currNodeTwo.setInstances(currNodeTwo.getInstances() + 1); 
                    
                    Node nextNode = currNodeTwo.getNextNode();
                    while (nextNode != null) {
                        if (currNodeTwo.getInstances() > nextNode.getInstances()) {
                            // swap currNodeTwo and nextNode
                            if (prevNodeTwo != null) {
                                prevNodeTwo.setNextNode(nextNode);
                                // Specify the new head
                            } else headOne = nextNode;
                            currNodeTwo.setNextNode(nextNode.getNextNode());
                            nextNode.setNextNode(currNodeTwo);

                        } else break;

                        prevNodeTwo = nextNode;
                        nextNode = currNodeTwo.getNextNode();
                    }
                    createNewItem = false;
               }

               prevNodeTwo = currNodeTwo;
               currNodeTwo = currNodeTwo.getNextNode();
            }

            if (createNewItem) {
                // TODO: add newNode to both set
                newNode.setNextNode(headOne);
                headOne = newNode;
            }
        }
        length++;

        
        // Implement me!
    } // end of add()


    @Override
	public int search(String item) {
        Node currNode = headOne;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0)
                return currNode.getInstances();

            currNode = currNode.getNextNode();
        }
        return searchFailed;
    } // end of search()


    @Override
	public List<String> searchByInstance(int instanceCount) {
        List<String> matchItems = new ArrayList<>();
        Node currNode = headTwo;

        while (currNode != null) {
            if (currNode.getInstances() == instanceCount) {
                matchItems.add(currNode.getItem());
            }

            /**
             * Since the multiset is already ordered, we can break once we find all items with specified instances
             * */

            if (currNode.getNextNode().getInstances() > instanceCount)
            currNode = currNode.getNextNode();
        }
        return matchItems;
    } // end of searchByInstance    


    @Override
	public boolean contains(String item) {
        // Implement me!

        // Placeholder, please update.
        return false;
    } // end of contains()


    @Override
	public void removeOne(String item) {
        // TODO: headOne impementation 

        // Store node before the prev node
        Node prevNode = null;
        Node currNode = headTwo;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0) {
                currNode.setInstances(currNode.getInstances() - 1);

                // delete item when count is 0
                if (currNode.getInstances() == 0) {
                    // length of 1 means only headTwo is remaining
                    if (length == 1) {
                        headTwo = null;
                    } 
                    else if (currNode == headTwo) {
                        headTwo = currNode.getNextNode();
                    }
                    currNode = null;
                    length--;

                } else {
                    if (prevNode!= null && currNode.getInstances() < prevNode.getInstances()) {
                        prevNode.setNextNode(currNode.getNextNode());

                        Node comp_currNode = headTwo;
                        Node comp_prevNode = null;
                        
                        while (comp_currNode != null) {
                            if (currNode.getInstances() < comp_currNode.getInstances()) {
                                currNode.setNextNode(comp_currNode);
                                comp_prevNode.setNextNode(currNode);
                                return;
                            }
                            comp_prevNode = comp_currNode;
                            comp_currNode = comp_currNode.getNextNode();
                        }
                    }
                }
            }

            prevNode = currNode;
            currNode = currNode.getNextNode();
        }

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

    class Node {
        protected String _item;
        private Node _nextNode;
        private int _instances;

        public Node(String item) {
            _item = item;
            _nextNode = null;
            _instances = 1;
        }

        public String getItem() {
            return _item;
        }

        public Node getNextNode() {
            return _nextNode;
        }

        public int getInstances() {
            return _instances;
        }

        public void setNextNode(Node nextNode) {
            _nextNode = nextNode;
        }

        public void setInstances(int instances) {
            _instances = instances;
        }
    }
} // end of class DualLinkedListMultiset
