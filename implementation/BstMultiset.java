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
public class BstMultiset extends RmitMultiset
{
    protected Node rootNode;
    protected Node uRootNode;
    protected Node iRootNode;
    protected Node dRootNode;


    public BstMultiset()
    {
        rootNode = null;
    }


    @Override
	public void add(String value)
    {
        Node newNode = new Node(value, 1);

        if(rootNode == null)
        {
            rootNode = newNode;
        }
        else
        {
            Node prevNode = null;
            Node currNode = rootNode;
            boolean createNewItem = true;

            while (currNode!= null)
            {
                if (currNode.getValue().equals(value))
                {
                    currNode.setCount(currNode.getCount() + 1);
                    createNewItem = false;
                    break;
                }
                else
                {
                    prevNode = currNode;

                    if (value.compareTo(currNode.getValue()) >= 0)
                    {
                        currNode = currNode.getRightNode();
                    }
                    else
                    {
                        currNode = currNode.getLeftNode();
                    }
                }
            }

            if (createNewItem)
            {
                newNode.setPrevNode(prevNode);

                if (value.compareTo(prevNode.getValue()) >= 0)
                {
                    prevNode.rightNode = newNode;
                }
                else
                {
                    prevNode.leftNode = newNode;
                }
            }
        }
    }


    @Override
	public int search(String item)
    {
        int returnInt;

        if (rootNode == null) return searchFailed;
        else
        {
            if (searchTravel(rootNode, item) == null)
            {
                return searchFailed;
            }
            else
            {
                returnInt = searchTravel(rootNode, item).getCount();
            }
        }

        if (returnInt == -1) return searchFailed;
        else return returnInt;
    }


    public Node searchTravel(Node node, String item)
    {
        if (node==null || node.value.equals(item))
            return node;

        if (item.compareTo(node.value) < 0)
            return searchTravel(node.leftNode, item);

        return searchTravel(node.rightNode, item);
    }


    @Override
	public List<String> searchByInstance(int instanceCount)
    {
        List<String> elemList = new ArrayList<>();
        if (rootNode == null) return null;
        else return insTravel(rootNode, instanceCount, elemList);
    }


    public List<String> insTravel(Node node, int count, List<String> elemList)
    {
        if (node != null)
        {
            insTravel(node.leftNode, count, elemList);

            if (node.getCount() == count)
            {
                elemList.add(node.getValue());
            }
            insTravel(node.rightNode, count, elemList);
        }
        return elemList;
    }


    @Override
	public boolean contains(String item)
    {
        if (rootNode == null) return false;
        if (searchTravel(rootNode, item) == null) return false;
        else return true;
    }


    @Override
	public void removeOne(String item)
    {
        Node removeNode = searchTravel(rootNode, item);

        if (removeNode == null) return;

        if (removeNode.getCount() > 1)
        {
            removeNode.setCount(removeNode.getCount() - 1);
        }
        else
        {
            deleteKey(item);
        }
    }


    public void deleteKey(String item)
    {
        rootNode = deleteRec(rootNode, item);
    }


    public Node deleteRec(Node node, String item)
    {
        if (node == null)  return node;

        if (item.compareTo(node.value) < 0)
            node.leftNode = deleteRec(node.leftNode, item);
        else if (item.compareTo(node.value) > 0)
            node.rightNode = deleteRec(node.rightNode, item);
        else
        {
            if (node.leftNode == null)
                return node.rightNode;
            else if (node.rightNode == null)
                return node.leftNode;

            node.value = minValue(node.rightNode);

            node.rightNode = deleteRec(node.rightNode, node.value);
        }
        return node;
    }


    String minValue(Node node)
    {
        String minv = node.value;
        while (node.leftNode != null)
        {
            minv = node.leftNode.value;
            node = node.leftNode;
        }
        return minv;
    }


    @Override
    public String print()
    {
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
	public String printRange(String lower, String upper)
    {
        Node[] array = new Node[0];
        List<Node> ay;
        StringBuilder sb = new StringBuilder();
        createArray(rootNode);
        ay = Arrays.asList(array);
        List<Node> sList = bubbleSort(ay);

        for (int i = 0; i < sList.size(); i++)
        {
            if (sList.get(i).getValue().compareTo(lower) >= 0
                    && sList.get(i).getValue().compareTo(upper) <= 0)
            {
                sb.append(sList.get(i).getValue()).append(": ").
                        append(sList.get(i).getCount()).append("\n");
            }
        }
        return sb.toString();
    }


    @Override
	public RmitMultiset union(RmitMultiset other)
    {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray;
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index= 0;
        int count = 0;

        if (other instanceof BstMultiset)
        {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);
            newArray = nodeArray;

            for (int i = 0; i < otherArray.length; i++)
            {
                found = false;

                for (int j = 0; j < nodeArray.length; j++)
                {
                    if (nodeArray[j].getValue().equals(otherArray[i].getValue()))
                    {
                        count = otherArray[i].getCount() + nodeArray[j].getCount();
                        index = j;
                        found = true;
                        break;
                    }
                }


                if (found)
                {
                    Node newObj = new Node(otherArray[i].getValue(), count);
                    newArray[index] = newObj;
                }
                else
                {
                    newArray = addSize(newArray);
                    newArray[newArray.length - 1] = otherArray[i];
                }
            }

            for (int i = 0; i < newArray.length; i++)
            {
                unionArrayToBst(newArray[i].getValue(), newArray[i].getCount());
            }
            newSet.rootNode = uRootNode;
        }

        return newSet;
    }


    public void unionArrayToBst(String value, int count)
    {
        Node newNode = new Node(value, count);

        if(uRootNode == null)
        {
            uRootNode = newNode;
        }
        else
        {
            Node prevNode = null;
            Node currNode = uRootNode;

            while (currNode!= null)
            {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0)
                {
                    currNode = currNode.getRightNode();

                }
                else
                {
                    currNode = currNode.getLeftNode();
                }
            }

            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0)
            {
                prevNode.rightNode = newNode;
            }
            else
            {
                prevNode.leftNode = newNode;
            }
        }
    }


    @Override
    public RmitMultiset intersect(RmitMultiset other)
    {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray = new Node[0];
        Node[] bigArray;
        Node[] smallArray;
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index= 0;

        if (other instanceof BstMultiset)
        {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);

            if (nodeArray.length >= otherArray.length)
            {
                bigArray = nodeArray;
                smallArray = otherArray;
            }
            else
            {
                bigArray = otherArray;
                smallArray = nodeArray;
            }

            for (int i = 0; i < bigArray.length; i++)
            {
                found = false;

                for (int j = 0; j < smallArray.length; j++)
                {
                    if (bigArray[i].getValue().equals(smallArray[j].getValue()))
                    {
                        index = j;
                        found = true;
                        break;
                    }
                }

                if (found)
                {
                    newArray = addSize(newArray);

                    if (bigArray[i].getCount() == (smallArray[index].getCount()))
                    {
                        if (newArray[0] == null)
                        {
                            newArray[0] = bigArray[i];
                        }
                        else newArray[newArray.length - 1] = bigArray[i];
                    }
                    else
                    {
                        int count = Math.min(bigArray[i].getCount(), smallArray[index].getCount());

                        if (newArray[0] == null)
                        {
                            newArray[0] = bigArray[i];
                            newArray[0].setCount(count);
                        }
                        else
                        {
                            newArray[newArray.length - 1] = bigArray[i];
                            newArray[newArray.length - 1].setCount(count);
                        }
                    }
                }
            }

            for (int i = 0; i < newArray.length; i++)
            {
                interArrayToBst(newArray[i].getValue(), newArray[i].getCount());
            }

            newSet.rootNode = iRootNode;
        }
        return newSet;
    }


    public void interArrayToBst(String value, int count)
    {
        Node newNode = new Node(value, count);

        if(iRootNode == null)
        {
            iRootNode = newNode;
        }
        else
        {
            Node prevNode = null;
            Node currNode = iRootNode;

            while (currNode!= null)
            {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0)
                {
                    currNode = currNode.getRightNode();

                }
                else
                {
                    currNode = currNode.getLeftNode();
                }
            }

            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0)
            {
                prevNode.rightNode = newNode;
            }
            else
            {
                prevNode.leftNode = newNode;
            }
        }
    }


    @Override
    public RmitMultiset difference(RmitMultiset other)
    {
        Node[] nodeArray;
        Node[] otherArray;
        Node[] newArray = new Node[0];
        BstMultiset newSet = new BstMultiset();
        boolean found;
        int index= 0;

        if (other instanceof BstMultiset)
        {
            Node otherRoot = ((BstMultiset) other).rootNode;
            nodeArray = createArray(rootNode);
            otherArray = createArray(otherRoot);

            for (int i = 0; i < nodeArray.length; i++)
            {
                int count = nodeArray[i].getCount();
                found = false;

                for (int j = 0; j < otherArray.length; j++)
                {
                    if (nodeArray[i].getValue().equals(otherArray[j].getValue()))
                    {
                        found = true;
                        count = Math.abs(nodeArray[i].getCount() - otherArray[j].getCount());
                        index = j;
                        break;
                    }
                }

                if (found)
                {
                    if (nodeArray[i].getCount() > (otherArray[index].getCount()))
                    {
                        newArray = addSize(newArray);

                        Node newObj = new Node(nodeArray[i].getValue(), count);
                        if (newArray[0] == null)
                        {
                            newArray[0] = newObj;
                        }
                        else
                        {
                            newArray[newArray.length - 1] = newObj;
                        }
                    }
                }
                else
                {
                    newArray = addSize(newArray);

                    if (newArray[0] == null)
                    {
                        newArray[0] = nodeArray[i];
                    }
                    else
                    {
                        newArray[newArray.length - 1] = nodeArray[i];
                    }
                }
            }

            for (int i = 0; i < newArray.length; i++)
            {
                differArrayToBst(newArray[i].getValue(), newArray[i].getCount());
            }

            newSet.rootNode = dRootNode;
        }

        return newSet;
    }


    public void differArrayToBst(String value, int count)
    {
        Node newNode = new Node(value, count);

        if(dRootNode == null)
        {
            dRootNode = newNode;
        }
        else
        {
            Node prevNode = null;
            Node currNode = dRootNode;

            while (currNode!= null)
            {
                prevNode = currNode;

                if (value.compareTo(currNode.getValue()) >= 0)
                {
                    currNode = currNode.getRightNode();

                }
                else
                {
                    currNode = currNode.getLeftNode();
                }
            }

            newNode.setPrevNode(prevNode);

            if (value.compareTo(prevNode.getValue()) >= 0)
            {
                prevNode.rightNode = newNode;
            }
            else
            {
                prevNode.leftNode = newNode;
            }
        }
    }


    public Node[] createArray(Node root)
    {
        Node[] ay = new Node[0];
        Node current, pre;

        if (root == null) return null;

        current = root;

        while (current != null)
        {
            if (current.leftNode == null)
            {
                ay = addSize(ay);
                ay[ay.length - 1] = current;
                current = current.rightNode;
            }
            else
            {
                pre = current.leftNode;
                while (pre.rightNode != null && pre.rightNode != current)
                    pre = pre.rightNode;

                if (pre.rightNode == null)
                {
                    pre.rightNode = current;
                    current = current.leftNode;
                }

                else
                {
                    pre.rightNode = null;
                    ay = addSize(ay);
                    ay[ay.length - 1] = current;
                    current = current.rightNode;
                }
            }
        }
        return ay;
    }


    public List<Node> bubbleSort(List<Node> nodeList)
    {
        int n = nodeList.size();
        boolean swapped;
        Node tempNode;

        if (n != 1)
        {
            for (int i = 0; i < n - 1; i++)
            {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++)
                {
                    if (nodeList.get(j).getCount() < nodeList.get(j+1).getCount())
                    {
                        tempNode = nodeList.get(j);
                        nodeList.set(j, nodeList.get(j+1));
                        nodeList.set(j+1, tempNode);
                        swapped = true;
                    }
                }

                if (!swapped) break;
            }
        }
        return nodeList;
    }


    public Node[] addSize(Node[] array)
    {
        Node[] tempArray;
        tempArray = array;
        array = new Node[array.length + 1];

        if (tempArray.length >= 0)
            System.arraycopy(tempArray, 0, array, 0, tempArray.length);
        return array;
    }


    public class Node
    {
        String value;
        int count;
        Node prevNode;
        Node leftNode;
        Node rightNode;

        Node(String value, int count)
        {
            this.value = value;
            this.count = count;
            prevNode = null;
            leftNode = null;
            rightNode = null;
        }

        //getters and setters
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public Node getPrevNode() { return prevNode; }
        public Node getLeftNode() { return leftNode; }
        public Node getRightNode() { return rightNode; }
        public void setPrevNode(Node prevNode) { this.prevNode = prevNode; }
        public void setLeftNode(Node leftNode) { this.leftNode = leftNode; }
        public void setRightNode(Node rightNode) { this.rightNode = rightNode; }

        public boolean hasNext() { return this.getLeftNode() != null || this.getRightNode() != null; }
        public boolean hasLeft() { return this.getLeftNode() != null; }
        public boolean hasRight() { return this.getRightNode() != null; }

        public void add(int amount) { this.count += amount; }
        public void remove(int amount) { this.count -= amount; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }

    }

}
