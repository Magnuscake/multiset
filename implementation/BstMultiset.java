package implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BST implementation of a multiset.  See comments in RmitMultiset to
 * understand what each overriden method is meant to do.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class BstMultiset extends RmitMultiset {
    protected Node rootNode; //rootNode for new BstMultiset
    protected Node uRootNode;   //rootNode for BstMultiset returned by union
    protected Node iRootNode;   //rootNode for BstMultiset returned by intersect
    protected Node dRootNode;   //rootNode for BstMultiset returned by difference


    public BstMultiset() {
        rootNode = null;
    }


    @Override
    public void add(String value) {
        Node newNode = new Node(value, 1);

        if (rootNode == null) {
            rootNode = newNode;
        } else {
            Node prevNode = null;
            Node currNode = rootNode;
            boolean createNewItem = true;

            //traverse the tree iteratively
            while (currNode != null) {
                if (currNode.getValue().equals(value)) {
                    currNode.setCount(currNode.getCount() + 1);
                    createNewItem = false;
                    break;
                } else {
                    prevNode = currNode;

                    if (value.compareTo(currNode.getValue()) >= 0) {
                        currNode = currNode.getRightNode();
                    } else {
                        currNode = currNode.getLeftNode();
                    }
                }
            }

            //create new node if there is no duplicate
            if (createNewItem) {
                newNode.setPrevNode(prevNode);

                if (value.compareTo(prevNode.getValue()) >= 0) {
                    prevNode.rightNode = newNode;
                } else {
                    prevNode.leftNode = newNode;
                }
            }
        }
    }


    @Override
    public int search(String item) {
        int returnInt;

        if (rootNode == null) return searchFailed;
        else {
            if (searchTravel(rootNode, item) == null) {
                return searchFailed;
            } // call a recursive method to traverse the tree
            else {
                returnInt = searchTravel(rootNode, item).getCount();
            }
        }

        if (returnInt == -1) return searchFailed;
        else return returnInt;
    }


    public Node searchTravel(Node node, String item) {
        //recursively search the tree
        if (node == null || node.value.equals(item))
            return node;

        if (item.compareTo(node.value) < 0)
            return searchTravel(node.leftNode, item);

        return searchTravel(node.rightNode, item);
    }


    @Override
    public List<String> searchByInstance(int instanceCount) {
        List<String> elemList = new ArrayList<>();
        if (rootNode == null) return null;
        //building the return string using recursive method
        else return insTravel(rootNode, instanceCount, elemList);
    }


    public List<String> insTravel(Node node, int count, List<String> elemList) {
        if (node != null) {
            insTravel(node.leftNode, count, elemList);

            if (node.getCount() == count) {
                elemList.add(node.getValue());
            }
            insTravel(node.rightNode, count, elemList);
        }
        return elemList;
    }


    @Override
    public boolean contains(String item) {
        if (rootNode == null) return false;
        return searchTravel(rootNode, item) != null;
    }


    @Override
    public void removeOne(String item) {
        //first find the node that will be removed/decreased
        Node removeNode = searchTravel(rootNode, item);

        if (removeNode == null) return;

        //decrease the count
        if (removeNode.getCount() > 1) {
            removeNode.setCount(removeNode.getCount() - 1);
        }
        //remove the node from the tree
        else {
            deleteKey(item);
        }
    }


    public void deleteKey(String item) {
        //call a recursive method
        rootNode = deleteRec(rootNode, item);
    }


    public Node deleteRec(Node node, String item) {
        //delete the node and rearrange the tree
        if (node == null) return null;

        if (item.compareTo(node.value) < 0)
            node.leftNode = deleteRec(node.leftNode, item);
        else if (item.compareTo(node.value) > 0)
            node.rightNode = deleteRec(node.rightNode, item);
        else {
            if (node.leftNode == null)
                return node.rightNode;
            else if (node.rightNode == null)
                return node.leftNode;

            node.value = minValue(node.rightNode);

            node.rightNode = deleteRec(node.rightNode, node.value);
        }
        return node;
    }


    String minValue(Node node) {
        String minv = node.value;
        while (node.leftNode != null) {
            minv = node.leftNode.value;
            node = node.leftNode;
        }
        return minv;
    }


    @Override
    public String print() {
        //first convert bst to array then bubble sort the array before printing
        Node[] nodeArray;
        List<Node> list;
        StringBuilder sb = new StringBuilder();
        nodeArray = createArray(rootNode);
        list = Arrays.asList(nodeArray);
        List<Node> sList = bubbleSort(list);

        for (Node node : sList) {
            sb.append(node.getValue()).append(":").
                    append(node.getCount()).append("\n");
        }

        return sb.toString();
    }


    @Override
    public String printRange(String lower, String upper) {
        Node[] array = new Node[0];
        List<Node> ay;
        StringBuilder sb = new StringBuilder();
        createArray(rootNode);
        ay = Arrays.asList(array);
        List<Node> sList = bubbleSort(ay);

        for (Node node : sList) {
            if (node.getValue().compareTo(lower) >= 0
                    && node.getValue().compareTo(upper) <= 0) {
                sb.append(node.getValue()).append(": ").
                        append(node.getCount()).append("\n");
            }
        }
        return sb.toString();
    }

    //For union, intersect and difference
    //bst will be first converted to a normal array, then convert back to bst after the operation
    //the implementations for these 3 methods are the same with array multiset
    //except for the converting part
    //the data structure itself is always bst, array is only a medium for the operation
    @Override
    public RmitMultiset union(RmitMultiset other) {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray;
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index = 0;
        int count = 0;

        if (other instanceof BstMultiset) {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);
            newArray = nodeArray;

            for (Node value : otherArray) {
                found = false;

                for (int j = 0; j < nodeArray.length; j++) {
                    if (nodeArray[j].getValue().equals(value.getValue())) {
                        count = value.getCount() + nodeArray[j].getCount();
                        index = j;
                        found = true;
                        break;
                    }
                }


                if (found) {
                    Node newObj = new Node(value.getValue(), count);
                    newArray[index] = newObj;
                } else {
                    newArray = addSize(newArray);
                    newArray[newArray.length - 1] = value;
                }
            }

            for (Node node : newArray) {
                unionArrayToBst(node.getValue(), node.getCount());
            }
            newSet.rootNode = uRootNode;
        }

        return newSet;
    }

    //method for converting array back to bst
    public void unionArrayToBst(String value, int count) {
        Node newNode = new Node(value, count);

        if (uRootNode == null) {
            uRootNode = newNode;
        } else {
            Node prevNode = null;
            Node currNode = uRootNode;

            while (currNode != null) {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0) {
                    currNode = currNode.getRightNode();

                } else {
                    currNode = currNode.getLeftNode();
                }
            }

            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0) {
                prevNode.rightNode = newNode;
            } else {
                prevNode.leftNode = newNode;
            }
        }
    }

    @Override
    public RmitMultiset intersect(RmitMultiset other) {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray = new Node[0];
        Node[] bigArray;
        Node[] smallArray;
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index = 0;

        if (other instanceof BstMultiset) {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);

            if (nodeArray.length >= otherArray.length) {
                bigArray = nodeArray;
                smallArray = otherArray;
            } else {
                bigArray = otherArray;
                smallArray = nodeArray;
            }

            for (Node value : bigArray) {
                found = false;

                for (int j = 0; j < smallArray.length; j++) {
                    if (value.getValue().equals(smallArray[j].getValue())) {
                        index = j;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    newArray = addSize(newArray);

                    if (value.getCount() == (smallArray[index].getCount())) {
                        if (newArray[0] == null) {
                            newArray[0] = value;
                        } else newArray[newArray.length - 1] = value;
                    } else {
                        int count = Math.min(value.getCount(), smallArray[index].getCount());

                        if (newArray[0] == null) {
                            newArray[0] = value;
                            newArray[0].setCount(count);
                        } else {
                            newArray[newArray.length - 1] = value;
                            newArray[newArray.length - 1].setCount(count);
                        }
                    }
                }
            }

            for (Node node : newArray) {
                interArrayToBst(node.getValue(), node.getCount());
            }

            newSet.rootNode = iRootNode;
        }
        return newSet;
    }


    public void interArrayToBst(String value, int count) {
        Node newNode = new Node(value, count);

        if (iRootNode == null) {
            iRootNode = newNode;
        } else {
            Node prevNode = null;
            Node currNode = iRootNode;

            while (currNode != null) {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0) {
                    currNode = currNode.getRightNode();

                } else {
                    currNode = currNode.getLeftNode();
                }
            }

            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0) {
                prevNode.rightNode = newNode;
            } else {
                prevNode.leftNode = newNode;
            }
        }
    }

    @Override
    public RmitMultiset difference(RmitMultiset other) {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray = new Node[0];
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index = 0;

        if (other instanceof BstMultiset) {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);

            for (Node value : nodeArray) {
                int count = value.getCount();
                found = false;

                for (int j = 0; j < otherArray.length; j++) {
                    if (value.getValue().equals(otherArray[j].getValue())) {
                        found = true;
                        count = Math.abs(value.getCount() - otherArray[j].getCount());
                        index = j;
                        break;
                    }
                }

                if (found) {
                    if (value.getCount() > (otherArray[index].getCount())) {
                        newArray = addSize(newArray);

                        Node newObj = new Node(value.getValue(), count);
                        if (newArray[0] == null) {
                            newArray[0] = newObj;
                        } else {
                            newArray[newArray.length - 1] = newObj;
                        }
                    }
                } else {
                    newArray = addSize(newArray);

                    if (newArray[0] == null) {
                        newArray[0] = value;
                    } else {
                        newArray[newArray.length - 1] = value;
                    }
                }
            }

            for (Node node : newArray) {
                differArrayToBst(node.getValue(), node.getCount());
            }

            newSet.rootNode = dRootNode;
        }

        return newSet;
    }


    public void differArrayToBst(String value, int count) {
        Node newNode = new Node(value, count);

        if (dRootNode == null) {
            dRootNode = newNode;
        } else {
            Node prevNode = null;
            Node currNode = dRootNode;

            while (currNode != null) {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0) {
                    currNode = currNode.getRightNode();

                } else {
                    currNode = currNode.getLeftNode();
                }
            }
            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0) {
                prevNode.rightNode = newNode;
            } else {
                prevNode.leftNode = newNode;
            }
        }
    }

    //convert bst to array
    public Node[] createArray(Node root) {
        Node[] ay = new Node[0];
        Node current, pre;

        if (root == null) return null;

        current = root;

        while (current != null) {
            if (current.leftNode == null) {
                ay = addSize(ay);
                ay[ay.length - 1] = current;
                current = current.rightNode;
            } else {
                pre = current.leftNode;
                while (pre.rightNode != null && pre.rightNode != current)
                    pre = pre.rightNode;

                if (pre.rightNode == null) {
                    pre.rightNode = current;
                    current = current.leftNode;
                } else {
                    pre.rightNode = null;
                    ay = addSize(ay);
                    ay[ay.length - 1] = current;
                    current = current.rightNode;
                }
            }
        }
        return ay;
    }


    public List<Node> bubbleSort(List<Node> nodeList) {
        int n = nodeList.size();
        boolean swapped;
        Node tempNode;

        if (n != 1) {
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (nodeList.get(j).getCount() < nodeList.get(j + 1).getCount()) {
                        tempNode = nodeList.get(j);
                        nodeList.set(j, nodeList.get(j + 1));
                        nodeList.set(j + 1, tempNode);
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
        }
        return nodeList;
    }


    public Node[] addSize(Node[] array) {
        Node[] tempArray;
        tempArray = array;
        array = new Node[array.length + 1];

        if (tempArray.length >= 0)
            System.arraycopy(tempArray, 0, array, 0, tempArray.length);
        return array;
    }

    //node object that has 5 attributes: value, count, previous node,
    //right node and left node.
    public class Node {
        String value;
        int count;
        Node prevNode;
        Node leftNode;
        Node rightNode;

        Node(String value, int count) {
            this.value = value;
            this.count = count;
            prevNode = null;
            leftNode = null;
            rightNode = null;
        }

        //getters and setters
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Node getPrevNode() {
            return prevNode;
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }

        public void setPrevNode(Node prevNode) {
            this.prevNode = prevNode;
        }

        public void setLeftNode(Node leftNode) {
            this.leftNode = leftNode;
        }

        public void setRightNode(Node rightNode) {
            this.rightNode = rightNode;
        }

        public boolean hasNext() {
            return this.getLeftNode() != null || this.getRightNode() != null;
        }

        public boolean hasLeft() {
            return this.getLeftNode() != null;
        }

        public boolean hasRight() {
            return this.getRightNode() != null;
        }

        public void add(int amount) {
            this.count += amount;
        }

        public void remove(int amount) {
            this.count -= amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }

}
