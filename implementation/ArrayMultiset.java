package implementation;

import java.util.ArrayList;
import java.util.List;


public class ArrayMultiset extends RmitMultiset
{
    ArrayObj [] array;

    public ArrayMultiset()
    {
        array = null;
    }


    @Override
    public void add(String elem)
    {
        boolean found = false;

        if (array == null)
        {
            array = new ArrayObj[1];
            ArrayObj obj = new ArrayObj(elem);
            array[0] = obj;
        }
        else
        {
            for (int i = 0; i < array.length; i++)
            {
                if (array[i].getValue().equals(elem))
                {
                    found = true;
                    array[i].setCount(array[i].getCount() + 1);
                }
            }

            if (!found)
            {
                ArrayObj[] newArray = new ArrayObj[array.length + 1];
                ArrayObj obj = new ArrayObj(elem);

                for (int i = 0; i < array.length; i++)
                {
                    newArray[i] = array[i];
                }

                newArray[newArray.length - 1] = obj;
                array = newArray;
            }
        }
    }


    @Override
    public int search(String elem)
    {
        boolean found = false;
        int returnInt = 0;

        if (array == null) return searchFailed;

        else
        {
            for (int i = 0; i < array.length; i++)
            {
                if (array[i].getValue().equals(elem))
                {
                    found = true;
                    returnInt = array[i].getCount();
                }
            }

            if (!found) return searchFailed;

            else return returnInt;
        }
    }


    @Override
    public List<String> searchByInstance(int instanceCount)
    {
        List<String> elemList = new ArrayList<>();

        for (int i = 0; i < array.length; i++)
        {
            if (array[i].getCount() == instanceCount)
            {
                elemList.add(array[i].getValue());
            }
        }

        if (elemList.size() == 0)
        {
            return null;
        }
        else return elemList;
    }


    @Override
    public boolean contains(String elem)
    {
        boolean contains = false;

        if (array == null) return false;

        else
        {
            for (int i = 0; i < array.length; i++)
            {
                if (array[i].getValue().equals(elem))
                {
                    contains = true;
                }
            }
        }
        return contains;
    }


    @Override
    public void removeOne(String elem)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].getValue().equals(elem))
            {
                if (array[i].getCount() == 1)
                {
                    ArrayObj[] newArray = new ArrayObj[array.length - 1];

                    for (int j = 0; j < i; j++)
                    {
                        newArray[j] = array[j];
                    }

                    for (int j = i + 1; j < array.length; j++)
                    {
                        newArray[j - 1] = array[j];
                    }
                    array = newArray;
                }
                else
                {
                    array[i].setCount(array[i].getCount() - 1);
                }
            }
        }
    }


    @Override
    public String print()
    {
        try
        {
            return printSet(array);
        }
        catch (NullPointerException  e)
        {
            return null;
        }
    }


    @Override
    public String printRange(String lower, String upper)
    {
        int newLength = 0;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i].getValue().compareTo(lower) >= 0
                    && array[i].getValue().compareTo(upper) <= 0)
            {
                newLength++;
            }
        }

        ArrayObj[] newArray = new ArrayObj[newLength];
        int counter = 0;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i].getValue().compareTo(lower) >= 0
                    && array[i].getValue().compareTo(upper) <= 0)
            {
                newArray[counter++] = array[i];
            }
        }

        ArrayObj[] sArray = newArray;
        return printSet(sArray);
    }


    @Override
    public RmitMultiset union(RmitMultiset other)
    {
        boolean found;
        int index = 0;
        int count = 0;
        ArrayObj[] newArray = new ArrayObj[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);

        if (other instanceof ArrayMultiset)
        {
            ArrayObj [] otherArray = ((ArrayMultiset) other).array;

            for (int i = 0; i < otherArray.length; i++)
            {
                found = false;

                for (int j = 0; j < array.length; j++)
                {
                    if (array[j].getValue().equals(otherArray[i].getValue()))
                    {
                        count = otherArray[i].getCount() + array[j].getCount();
                        index = j;
                        found = true;

                        break;
                    }
                }

                if (found)
                {
                    ArrayObj newObj = new ArrayObj(otherArray[i].getValue());
                    newObj.setCount(count);
                    newArray[index] = newObj;
                }
                else
                {
                    newArray = addSize(newArray);
                    newArray[newArray.length - 1] = otherArray[i];
                }
            }
        }

        ArrayMultiset newSet = new ArrayMultiset();
        newSet.array = newArray;
        return newSet;
    }


    @Override
    public RmitMultiset intersect(RmitMultiset other)
    {
        ArrayObj[] newArray = new ArrayObj[0];
        boolean found;
        int index = 0;
        ArrayObj[] bigArray;
        ArrayObj[] smallArray;

        if (other instanceof ArrayMultiset)
        {
            ArrayObj [] otherArray = ((ArrayMultiset) other).array;

            if (array.length >= otherArray.length)
            {
                bigArray = array;
                smallArray = otherArray;
            }
            else
            {
                bigArray = otherArray;
                smallArray = array;
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
        }

        ArrayMultiset newSet = new ArrayMultiset();
        newSet.array = newArray;
        return newSet;
    }


    @Override
    public RmitMultiset difference(RmitMultiset other)
    {
        ArrayObj[] newArray = new ArrayObj[0];
        boolean found;
        int index = 0;

        if (other instanceof ArrayMultiset)
        {
            ArrayObj[] otherArray = ((ArrayMultiset) other).array;

            for (int i = 0; i < array.length; i++)
            {
                int count = array[i].getCount();
                found = false;

                for (int j = 0; j < otherArray.length; j++)
                {
                    if (array[i].getValue().equals(otherArray[j].getValue()))
                    {
                        found = true;
                        count = Math.abs(array[i].getCount() - otherArray[j].getCount());
                        index = j;
                        break;
                    }
                }

                if (found)
                {
                    if (array[i].getCount() > (otherArray[index].getCount()))
                    {
                        newArray = addSize(newArray);

                        ArrayObj newObj = new ArrayObj(array[i].getValue());
                        if (newArray[0] == null)
                        {
                            newArray[0] = newObj;
                            newArray[0].setCount(count);
                        }
                        else
                        {
                            newArray[newArray.length - 1] = newObj;
                            newArray[newArray.length - 1].setCount(count);
                        }
                    }
                }
                else
                {
                    newArray = addSize(newArray);

                    if (newArray[0] == null)
                    {
                        newArray[0] = array[i];
                    }
                    else
                    {
                        newArray[newArray.length - 1] = array[i];
                    }
                }
            }
        }

        ArrayMultiset newSet = new ArrayMultiset();
        newSet.array = newArray;
        return newSet;
    }


    public class ArrayObj
    {
        private String value;
        private int count;


        public ArrayObj(String value)
        {
            this.value = value;
            this.count = 1;
        }

        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }

        public String getValue() { return value; }
        public void setValue(String value) {this.value = value; }
    }


    public ArrayObj[] bubbleSort(ArrayObj[] array)
    {
        int n = array.length;
        boolean swapped;
        ArrayObj tempObj;

        if (n != 1)
        {
            for (int i = 0; i < n - 1; i++)
            {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++)
                {
                    if (array[j].getCount() < array[j + 1].getCount())
                    {
                        tempObj = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = tempObj;
                        swapped = true;
                    }
                }

                if (!swapped) break;
            }
        }
        return array;
    }


    public String printSet(ArrayObj[] array)
    {
        ArrayObj[] sArray = bubbleSort(array);
        int n = sArray.length;
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < n; i++)
        {
            sb.append(sArray[i].getValue());

            sb.append(":").append(sArray[i].getCount());

            if (i != n - 1)
            {
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    public ArrayObj[] addSize(ArrayObj[] array)
    {
        ArrayObj[] tempArray;
        tempArray = array;
        array = new ArrayObj[array.length + 1];

        if (tempArray.length >= 0) System.arraycopy(tempArray, 0, array, 0, tempArray.length);
        return array;
    }
}