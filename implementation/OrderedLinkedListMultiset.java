package implementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Ordered linked list implementation of a multiset.  See comments in RmitMultiset to
 * understand what each overriden method is meant to do.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class OrderedLinkedListMultiset extends RmitMultiset
{
    // Reference to the head node 
    protected Node head;
    // Reference to the length of list
    protected int length;

    public OrderedLinkedListMultiset() {
        head = null;
        length = 0;
    }
    
    @Override
	public void add(String item) {
        Node newNode = new Node(item);

        if (head == null) {
            head = newNode;

        } else {
            Node prevNode = null;
            Node currNode = head;

            boolean createNewItem = true;

            while (currNode != null) {
                if (currNode.getItem().equals(item)) {
                    currNode.setInstances(currNode.getInstances() + 1); 
                    
                    Node nextNode = currNode.getNextNode();
                    while (nextNode != null) {
                        if (currNode.getInstances() > nextNode.getInstances()) {
                            // swap currNode and nextNode
                            if (prevNode != null) {
                                prevNode.setNextNode(nextNode);
                                // Specify the new head
                            } else head = nextNode;
                            currNode.setNextNode(nextNode.getNextNode());
                            nextNode.setNextNode(currNode);
                        } else break;

                        prevNode = nextNode;
                        nextNode = currNode.getNextNode();
                    }
                    createNewItem = false;
               }

               prevNode = currNode;
               currNode = currNode.getNextNode();
            }

            if (createNewItem) {
                newNode.setNextNode(head);
                head = newNode;
            }
            length++;

            
        } 
    } // end of add()


    @Override
	public int search(String item) {
        Node currNode = head;

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
        Node currNode = head;

        while (currNode != null) {
            if (currNode.getInstances() == instanceCount) {
                matchItems.add(currNode.getItem());
            }
            currNode = currNode.getNextNode();
        }
        return matchItems;
    } // end of searchByInstance


    @Override
	public boolean contains(String item) {
        Node currNode = head;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0) {
                return true;
            }
        }
        return false;
    } // end of contains()


    @Override
	public void removeOne(String item) {
        // TODO: order element as and when they are removed
        if (head == null) {
            return;
        }

        Node currNode = head;
        Node prevNode = null;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0) {
                currNode.setInstances(currNode.getInstances() - 1);

                // delete item when count is 0
                if (currNode.getInstances() == 0) {
                    // length of 1 means only head is remaining
                    if (length == 1) {
                        head = null;
                    } 
                    else if (currNode == head) {
                        head = currNode.getNextNode();
                        currNode = null;
                        return;
                    }

                    currNode = null;
                } else {
                    while (currNode != null) {
                        // Order list logic
                        break;
                    }
                }
            }

            prevNode = currNode;
            currNode = currNode.getNextNode();
        }
        
        // Implement me!
    } // end of removeOne()


    @Override
	public String print() {

        Node currNode = head;

        StringBuffer strList = new StringBuffer();

        while (currNode != null) {
            strList.append(currNode.getItem() + ":" + currNode.getInstances() + "\n");
            currNode = currNode.getNextNode();
        }

        return strList.toString();
    } // end of OrderedPrint


    @Override
	public String printRange(String lower, String upper) {
        Node currNode = head;

        StringBuffer rangeList = new StringBuffer();

        while (currNode != null) {
            if (currNode.getItem().compareTo(lower) >= 0 && currNode.getItem().compareTo(upper) <= 0) {
                rangeList.append(currNode);
            }
            currNode = currNode.getNextNode();
        }
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

} // end of class OrderedLinkedListMultiset

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
