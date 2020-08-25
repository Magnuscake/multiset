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
            return;
        }            

        Node currNode = head;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0) {
                currNode.setInstances(currNode.getInstances() + 1); 
                return;
            }
            currNode = currNode.getNextNode();
        }
        // Attach new node to head
        if (newNode.getItem().compareTo(head.getItem()) < 0) {
            newNode.setNextNode(head);
            head = newNode;
            length++;
            return;
        }

        currNode = head;
        Node nextNode = currNode.getNextNode();
        while (currNode != null) {
            if (currNode.getNextNode() == null) {
                currNode.setNextNode(newNode);
                break;
            }
            else if (newNode.getItem().compareTo(nextNode.getItem()) < 0) {
                currNode.setNextNode(newNode);
                newNode.setNextNode(nextNode);
                break;
            }

            currNode = nextNode;
            nextNode = currNode.getNextNode();
        }
        length++;

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
        // Store node before the prev node
        Node currNode = head;

        // Current item is being reffered to the head node
        // Check if head node is the same as the corresponding item
        if (currNode.getItem().compareTo(item) == 0) {
            currNode.setInstances(currNode.getInstances() - 1);
            if (head.getInstances() == 0) {
                head = currNode.getNextNode();
                length--;
            }
            return;
        }

        Node nextNode = currNode.getNextNode();
        while (nextNode != null) {
            if (nextNode.getItem().compareTo(item) == 0) {
                nextNode.setInstances(nextNode.getInstances() - 1);

                // Delete item when count is 0
                if (nextNode.getInstances() == 0) {
                    currNode.setNextNode(nextNode.getNextNode());
                    length--;
                }
                break;
            }
            currNode = nextNode;
            nextNode = currNode.getNextNode();
        }
    } // end of removeOne()


    @Override
	public String print() {
        Node currNode = head;

        StringBuffer strList = new StringBuffer();

        while (currNode != null) {
            strList.insert(0, currNode.getItem() + ":" + currNode.getInstances() + "\n");
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
        return new String();
    } // end of printRange()


    @Override
	public RmitMultiset union(RmitMultiset other) {
        // New multiset to be returned
        RmitMultiset unionedLinkedList = new OrderedLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = head;
        // Second multiset
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        Node multiset2CurrNode = getInitialNode(otherToLinkedListMultiset);

        if (multiset1CurrNode == null && multiset2CurrNode == null) {
            return null;
        }

        while (multiset1CurrNode != null) {
            for (int i = 1; i <= multiset1CurrNode.getInstances(); i++) {
                unionedLinkedList.add(multiset1CurrNode.getItem());
            }
            multiset1CurrNode =  multiset1CurrNode.getNextNode();
        }

        while (multiset2CurrNode != null) {
            for (int i = 1; i <= multiset2CurrNode.getInstances(); i++) {
                unionedLinkedList.add(multiset2CurrNode.getItem());
            }
            multiset2CurrNode =  multiset2CurrNode.getNextNode();
        }
        return unionedLinkedList;
    } // end of union()


    @Override
	public RmitMultiset intersect(RmitMultiset other) {
        OrderedLinkedListMultiset intersectedMultiset = new OrderedLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = head;
        // Second multiset
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        Node multiset2CurrNode = getInitialNode(otherToLinkedListMultiset);

        // Common item that exists in both multisets
        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if ((multiset1CurrNode.getItem()).compareTo(multiset2CurrNode.getItem()) == 0) {
                    // Combine instances from first and second multisets
                    int totalInstances = multiset1CurrNode.getInstances() + multiset2CurrNode.getInstances();

                    for (int i = 1; i <= totalInstances; i++) {
                        intersectedMultiset.add(multiset1CurrNode.getItem());
                    }

                    // Reset second multiset iteration
                    multiset2CurrNode = getInitialNode(otherToLinkedListMultiset);
                    break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();

            }
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }
        return intersectedMultiset;
    } // end of intersect()

    // TODO: proper implementation
    @Override
	public RmitMultiset difference(RmitMultiset other) {
        RmitMultiset diffMultiset = new OrderedLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = head;
        // Second multiset
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        Node multiset2CurrNode = getInitialNode(otherToLinkedListMultiset);

        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if (multiset1CurrNode.getItem().compareTo(multiset2CurrNode.getItem()) != 0) {
                   for (int i = 1; i <= multiset1CurrNode.getInstances(); i++) {
                       diffMultiset.add(multiset1CurrNode.getItem());
                   }
                   break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();
            }
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }
        return null;
    } // end of difference()

    /**
     * Return initial node of a multset
     *
     * @param orderedLinkedListMultiset Multiset to get initial node from
     */
    public Node getInitialNode(OrderedLinkedListMultiset orderedLinkedListMultiset) {
        Node initialNode = orderedLinkedListMultiset.head;
        return initialNode;
    } // end of getInitialNode()

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
} // end of class OrderedLinkedListMultiset


