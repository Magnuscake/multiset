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

        // Add new node
        // Attach new node to head if corresponding item == head 
        if (newNode.getItem().compareTo(head.getItem()) < 0) {
            newNode.setNextNode(head);
            head = newNode;
            length++;
            return;
        }

        currNode = head;
        Node nextNode = currNode.getNextNode();
        while (currNode != null) {
            if (nextNode == null) {
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
        // Check if head node is the same as the corresponding item
        if (head.getItem().compareTo(item) == 0) {
            head.setInstances(head.getInstances() - 1);
            if (head.getInstances() == 0) {
                head = head.getNextNode();
                length--;
            }
            return;
        }

        Node currNode = head;
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
        // By default, multiset already has a length of 1
        // Therefore, we add an extra one to the length
        Node[] multisetArray = new Node[length + 1];
        int arrayCount = 0;
        Node currNode = head;

        while (currNode != null) {
            multisetArray[arrayCount] = currNode;
            currNode = currNode.getNextNode();
            arrayCount++;
        }

        for (int i = 0; i < multisetArray.length; i++) {
           for (int j = i + 1; j < multisetArray.length; j++) {
               // Temporary node
               Node temp;

               if (multisetArray[i].getInstances() > multisetArray[j].getInstances()) {
                   temp = multisetArray[i];
                   multisetArray[i] = multisetArray[j];
                   multisetArray[j] = temp;
               }
           }
        }
        
        StringBuilder strMultiset = new StringBuilder();

        for (int i = 0; i < multisetArray.length; i++) {
            strMultiset.insert(0, multisetArray[i].getItem() + ":" + multisetArray[i].getInstances() + "\n");
        }

        return new String(strMultiset);
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
        return new String(rangeList);
    } // end of printRange()


    @Override
	public RmitMultiset union(RmitMultiset other) {
        // New multiset to be returned
        RmitMultiset unionedLinkedList = new OrderedLinkedListMultiset();

        // First multiset to union
        Node multiset1CurrNode = head;
        // Second multiset to union
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        Node multiset2CurrNode = otherToLinkedListMultiset.head;

        if (multiset1CurrNode == null && multiset2CurrNode == null) {
            return null;
        }
        // To store the current node in the multiset
        Node newNode;

        while (multiset1CurrNode != null) {
            newNode = new Node(multiset1CurrNode.getItem(), multiset1CurrNode.getInstances());
            
            addNewNode((OrderedLinkedListMultiset) unionedLinkedList, newNode);
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }

        while (multiset2CurrNode != null) {
            newNode = new Node(multiset2CurrNode.getItem(), multiset2CurrNode.getInstances());
            
            addNewNode((OrderedLinkedListMultiset) unionedLinkedList, newNode);
            multiset2CurrNode = multiset2CurrNode.getNextNode();

        }

        return unionedLinkedList;
    } // end of union()


    @Override
	public RmitMultiset intersect(RmitMultiset other) {
        RmitMultiset intersectedMultiset = new OrderedLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = head;
        // Second multiset
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        Node multiset2CurrNode = otherToLinkedListMultiset.head;

        // Common item that exists in both multisets
        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if ((multiset1CurrNode.getItem()).compareTo(multiset2CurrNode.getItem()) == 0) {
                    // Combine instances from first and second multisets
                    int totalInstances = Math.min(multiset1CurrNode.getInstances(), multiset2CurrNode.getInstances());

                    Node newNode = new Node(multiset1CurrNode.getItem(), totalInstances);

                    addNewNode((OrderedLinkedListMultiset) intersectedMultiset, newNode);
                     break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();

            }
            multiset2CurrNode = otherToLinkedListMultiset.head;
            multiset1CurrNode = multiset1CurrNode.getNextNode();
        }
        return intersectedMultiset;
    } // end of intersect()


    @Override
	public RmitMultiset difference(RmitMultiset other) {
        RmitMultiset diffMultiset = new OrderedLinkedListMultiset();

        // First multiset
        Node multiset1CurrNode = head;
        // Second multiset
        OrderedLinkedListMultiset otherToLinkedListMultiset = (OrderedLinkedListMultiset) other;
        // Start from the head of the multiset
        Node multiset2CurrNode = otherToLinkedListMultiset.head; 

        while (multiset1CurrNode != null) {
            while (multiset2CurrNode != null) {
                if (multiset1CurrNode.getItem().compareTo(multiset2CurrNode.getItem()) == 0) {
                    Node newNode; 
                    int totalInstances = multiset1CurrNode.getInstances() - multiset2CurrNode.getInstances();
                        
                    if (totalInstances > 0) {
                        newNode = new Node(multiset1CurrNode.getItem(), totalInstances);
                        addNewNode((OrderedLinkedListMultiset) diffMultiset, newNode);
                    }
                   break;
                }
                multiset2CurrNode = multiset2CurrNode.getNextNode();
            }
            if (multiset2CurrNode == null) {
                addNewNode((OrderedLinkedListMultiset) diffMultiset, multiset1CurrNode);
            }
            multiset2CurrNode = otherToLinkedListMultiset.head;
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
    public void addNewNode(OrderedLinkedListMultiset orderedLinkedListMultiset, Node node) {

        for (int i = 1; i <= node.getInstances(); i++) {
            orderedLinkedListMultiset.add(node.getItem());
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
} // end of class OrderedLinkedListMultiset


