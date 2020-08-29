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
    // Length of first and second set
    protected int length;


    public DualLinkedListMultiset() {
        headOne = null;
        headTwo = null;
        length = 0;
    }

    @Override
	public void add(String item) {
        // To avoid linking confusion
        Node newNodeOne = new Node(item);
        Node newNodeTwo = new Node(item);

        if (headOne == null && headTwo == null) {
            headOne = newNodeOne;
            headTwo = newNodeTwo;
            return;
        } 
        // Track previous node of current node 
        Node currNodeOne;
        Node currNodeTwo;
        boolean createNewItem = true;

        // First check if the item already exists
        // and if so, increment the instance count
        
        /** Linked list one */
        currNodeOne = headOne;

        while (currNodeOne != null) {
            if (currNodeOne.getItem().compareTo(item) == 0) {
                currNodeOne.setInstances(currNodeOne.getInstances() + 1); 
                createNewItem = false;
                break;
            }
            currNodeOne = currNodeOne.getNextNode();
        }

        /** Linked list two */
        Node prevNode = null;
        currNodeTwo = headTwo;

        while (currNodeTwo != null) {
            if (currNodeTwo.getItem().compareTo(item) == 0) {
                currNodeTwo.setInstances(currNodeTwo.getInstances() + 1); 

                Node nextNode = currNodeTwo.getNextNode();
                while (nextNode != null) {
                    if (currNodeTwo.getInstances() > nextNode.getInstances()) {
                        // swap currNodeTwo and nextNode
                        if (prevNode == null) {
                            headTwo = nextNode;
                            // Specify the new head
                        } else prevNode.setNextNode(nextNode);
                        currNodeTwo.setNextNode(nextNode.getNextNode());
                        nextNode.setNextNode(currNodeTwo);

                    } else break;

                    prevNode = nextNode;
                    nextNode = currNodeTwo.getNextNode();
                }
                // Since the item already exists, we don't have to proceed any further
                return;
            }
            prevNode = currNodeTwo;
            currNodeTwo = currNodeTwo.getNextNode();
        }

        /** Linked list one */
        if (createNewItem) {
            // Attach new node to head if corresponding item == head 
            if (newNodeOne.getItem().compareTo(headOne.getItem()) < 0) {
                newNodeOne.setNextNode(headOne);
                headOne = newNodeOne;
            } else {
                currNodeOne = headOne;
                Node nextNode = currNodeOne.getNextNode();
                while (currNodeOne != null) {
                    if (nextNode == null) {
                        currNodeOne.setNextNode(newNodeOne);
                        break;
                    }
                    else if (newNodeOne.getItem().compareTo(nextNode.getItem()) < 0) {
                        currNodeOne.setNextNode(newNodeOne);
                        newNodeOne.setNextNode(nextNode);
                        break;
                    }
                    currNodeOne = nextNode;
                    nextNode = currNodeOne.getNextNode();
                }

            }

            /** Linked list two */
            newNodeTwo.setNextNode(headTwo);
            headTwo = newNodeTwo;

            length++;

        }
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
                matchItems.add(0, currNode.getItem());
            }

            /**
             * Since the multiset is already ordered, we can
             * break once we find all items with specified instances
             * */
            if (currNode.getNextNode() != null && currNode.getNextNode().getInstances() < instanceCount) break;

            currNode = currNode.getNextNode();
        }
        return matchItems;
    } // end of searchByInstance    


    @Override
	public boolean contains(String item) {
        Node currNode = headOne;

        while (currNode != null) {
            if (currNode.getItem().compareTo(item) == 0) {
                return true;
            }
            currNode = currNode.getNextNode();
        }
        return false;
    } // end of contains()


    @Override
	public void removeOne(String item) {
        Node currNodeOne;
        Node currNodeTwo;
        // Decrement length counter by 1 if true
        boolean decrementLength = false;

listOne: {
        /** Remove one from linked list one **/
        // Check if head node is the same as the corresponding item
        if (headOne.getItem().compareTo(item) == 0) {
            headOne.setInstances(headOne.getInstances() - 1);
            if (headOne.getInstances() == 0) {
                headOne = headOne.getNextNode();
                decrementLength = true;
                break listOne;
            }
        }

        currNodeOne = headOne;
        Node nextNode = currNodeOne.getNextNode();
        while (nextNode != null) {
            if (nextNode.getItem().compareTo(item) == 0) {
                nextNode.setInstances(nextNode.getInstances() - 1);

                // Delete item when count is 0
                if (nextNode.getInstances() == 0) {
                    currNodeOne.setNextNode(nextNode.getNextNode());
                    decrementLength = true;
                }
                break;
            }
            currNodeOne = nextNode;
            nextNode = currNodeOne.getNextNode();
        }
             
}

listTwo: {
        /** Remove one from linked list two */
        Node prevNode = null;
        currNodeTwo = headTwo;

        while (currNodeTwo != null) {
            if (currNodeTwo.getItem().compareTo(item) == 0) {
                currNodeTwo.setInstances(currNodeTwo.getInstances() - 1);
                break;
            }
            prevNode = currNodeTwo;
            currNodeTwo = currNodeTwo.getNextNode();
        }

        if (currNodeTwo.getInstances() == 0) {
            if (currNodeTwo == headTwo) {
                headTwo = currNodeTwo.getNextNode();
            } else {
                prevNode.setNextNode(currNodeTwo.getNextNode());
                currNodeTwo = null;
                break listTwo;
            }
        } else {
            if (prevNode!= null && currNodeTwo.getInstances() < prevNode.getInstances()) {
                prevNode.setNextNode(currNodeTwo.getNextNode());
                // Current node - node to reorder
                Node tempNode = currNodeTwo;

                // Check if node to reorder should be placed at the start
                if (tempNode.getInstances() < headTwo.getInstances()) {
                   tempNode.setNextNode(headTwo);
                   headTwo = tempNode;
                   break listTwo;
                }

                // Reset currNodeTwo and prevNode
                currNodeTwo = headTwo.getNextNode();
                prevNode = headTwo;

                while (currNodeTwo != null) {
                    if (tempNode.getInstances() < currNodeTwo.getInstances()) {
                        prevNode.setNextNode(tempNode);
                        tempNode.setNextNode(currNodeTwo);
                        break;
                    }
                    prevNode = currNodeTwo;
                    currNodeTwo = currNodeTwo.getNextNode();
                }
            }
        }

}
        if (decrementLength) length--;

    } // end of removeOne()


    @Override
	public String print() {
        Node currNode = headTwo;

        StringBuffer strMultiset = new StringBuffer();

        while (currNode != null) {
            strMultiset.insert(0, currNode.getItem() + ":" + currNode.getInstances() + "\n");
            currNode = currNode.getNextNode();
        }
        return new String(strMultiset);

    } // end of OrderedPrint


    @Override
	public String printRange(String lower, String upper) {
        Node currNode = headOne;

        StringBuffer rangeList = new StringBuffer();

        while (currNode != null) {
            if (currNode.getItem().compareTo(lower) >= 0 && currNode.getItem().compareTo(upper) <= 0) {
            rangeList.insert(0, currNode.getItem() + ":" + currNode.getInstances() + "\n");
            }
            currNode = currNode.getNextNode();
        }
        return new String(rangeList);
    } // end of printRange()


    @Override
	public RmitMultiset union(RmitMultiset other) {
        // New multiset to be returned
        RmitMultiset unionedLinkedList = new DualLinkedListMultiset();

        // First multiset to union
        Node multiset1CurrNode = headOne;
        // Second multiset to union
        DualLinkedListMultiset otherToLinkedListMultiset = (DualLinkedListMultiset) other;
        Node multiset2CurrNode = otherToLinkedListMultiset.headOne;

        if (multiset1CurrNode == null && multiset2CurrNode == null) {
            return null;
        }
        // To store the current node in the multiset
        Node newNode;

        while (multiset1CurrNode != null) {
            newNode = new Node(multiset1CurrNode.getItem(), multiset1CurrNode.getInstances());
            
            addNewNode((DualLinkedListMultiset) unionedLinkedList, newNode);
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }

        while (multiset2CurrNode != null) {
            newNode = new Node(multiset2CurrNode.getItem(), multiset2CurrNode.getInstances());
            
            addNewNode((DualLinkedListMultiset) unionedLinkedList, newNode);
            multiset2CurrNode = multiset2CurrNode.getNextNode();

        }

        return unionedLinkedList;
    } // end of union()


    @Override
	public RmitMultiset intersect(RmitMultiset other) {
        RmitMultiset intersectedMultiset = new DualLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = headOne;
        // Second multiset
        DualLinkedListMultiset otherToLinkedListMultiset = (DualLinkedListMultiset) other;
        Node multiset2CurrNode = otherToLinkedListMultiset.headOne;

        // Common item that exists in both multisets
        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if ((multiset1CurrNode.getItem()).compareTo(multiset2CurrNode.getItem()) == 0) {
                    // Combine instances from first and second multisets
                    int totalInstances = Math.min(multiset1CurrNode.getInstances(), multiset2CurrNode.getInstances());

                    Node newNode = new Node(multiset1CurrNode.getItem(), totalInstances);

                    addNewNode((DualLinkedListMultiset) intersectedMultiset, newNode);
                     break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();

            }
            multiset2CurrNode = otherToLinkedListMultiset.headOne;
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }
        return intersectedMultiset;
    } // end of intersect()


    @Override
	public RmitMultiset difference(RmitMultiset other) {
        RmitMultiset diffMultiset = new DualLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = headOne;
        // Second multiset
        DualLinkedListMultiset otherToLinkedListMultiset = (DualLinkedListMultiset) other;
        // Start from the head of the multiset
        Node multiset2CurrNode = otherToLinkedListMultiset.headOne; 

        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if (multiset1CurrNode.getItem().compareTo(multiset2CurrNode.getItem()) == 0) {
                    Node newNode; 
                    int totalInstances = multiset1CurrNode.getInstances() - multiset2CurrNode.getInstances();
                        
                    if (totalInstances > 0) {
                        newNode = new Node(multiset1CurrNode.getItem(), totalInstances);
                        addNewNode((DualLinkedListMultiset) diffMultiset, newNode);
                    }
                   break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();
            }
            if (multiset2CurrNode == null) {
                addNewNode((DualLinkedListMultiset) diffMultiset, multiset1CurrNode);
            }
            multiset2CurrNode = otherToLinkedListMultiset.headOne;
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }
        return diffMultiset;
    } // end of difference()


    /**
     * Add a node with given item and instances 
     *
     * @param multiset Multiset to add node to 
     * @param newNode node to add to the multiset
     */
    public void addNewNode(DualLinkedListMultiset dualLinkedListMultiset, Node node) {

        for (int i = 1; i <= node.getInstances(); i++) {
            dualLinkedListMultiset.add(node.getItem());
        }
    } // end of addNewNode()


    class Node {
        protected String _item;
        private Node _nextNode;
        private int _instances;

        public Node(String item) {
            _item = item;
            _nextNode = null;
            _instances = 1;
        }

        public Node(String item, int instances) {
            _item = item;
            _nextNode = null;
            _instances = instances;
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
