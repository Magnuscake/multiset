package implementation;

import java.util.ArrayList;
import java.util.List;


public class ArrayMultiset extends RmitMultiset {
    //array for holding each array object
    ArrayObj[] array;

    public ArrayMultiset() {
        array = null;
    }


    @Override
    public void add(String elem) {
        boolean found = false;
        //if array is not empty, check duplicate
        if (array != null) {
            for (ArrayObj arrayObj : array) {
                if (arrayObj.getValue().equals(elem)) {
                    found = true;
                    arrayObj.setCount(arrayObj.getCount() + 1);
                }
            }
            //if the value is new to the array, add object to the end of array
            if (!found) {
                ArrayObj[] newArray = new ArrayObj[array.length + 1];
                ArrayObj obj = new ArrayObj(elem);

                System.arraycopy(array, 0, newArray, 0, array.length);

                newArray[newArray.length - 1] = obj;
                array = newArray;
            }
        } else {
            array = new ArrayObj[1];
            ArrayObj obj = new ArrayObj(elem);
            array[0] = obj;
        }
    }


    @Override
    public int search(String elem) {
        boolean found = false;
        int returnInt = 0;

        if (array == null) return searchFailed;

        else {
            for (ArrayObj arrayObj : array) {
                if (arrayObj.getValue().equals(elem)) {
                    found = true;
                    returnInt = arrayObj.getCount();
                }
            }

            if (!found) return searchFailed;
            else return returnInt;
        }
    }


    @Override
    public List<String> searchByInstance(int instanceCount) {
        List<String> elemList = new ArrayList<>();

        //building the return string
        for (ArrayObj arrayObj : array) {
            if (arrayObj.getCount() == instanceCount) {
                elemList.add(arrayObj.getValue());
            }
        }

        if (elemList.size() == 0) {
            return null;
        } else return elemList;
    }


    @Override
    public boolean contains(String elem) {
        boolean contains = false;

        if (array == null) return false;
        else {
            for (ArrayObj arrayObj : array) {
                if (arrayObj.getValue().equals(elem)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }


    @Override
    public void removeOne(String elem) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].getValue().equals(elem)) {
                //if the count of this object is 1, remove this object
                if (array[i].getCount() == 1) {
                    ArrayObj[] newArray = new ArrayObj[array.length - 1];

                    System.arraycopy(array, 0, newArray, 0, i);

                    if (array.length - i + 1 >= 0)
                        System.arraycopy(array, i + 1, newArray,
                                i + 1 - 1, array.length - i + 1);
                    array = newArray;
                } else {
                    array[i].setCount(array[i].getCount() - 1);
                }
            }
        }
    }


    @Override
    public String print() {
        try {
            return printSet(array);
        } catch (NullPointerException e) {
            return null;
        }
    }


    public String printSet(ArrayObj[] array) {
        //sort the array before printing
        ArrayObj[] sArray = bubbleSort(array);
        int n = sArray.length;
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < n; i++) {
            sb.append(sArray[i].getValue());

            sb.append(":").append(sArray[i].getCount());

            if (i != n - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    @Override
    public String printRange(String lower, String upper) {
        int newLength = 0;

        for (ArrayObj arrayObj : array) {
            if (arrayObj.getValue().compareTo(lower) >= 0
                    && arrayObj.getValue().compareTo(upper) <= 0) {
                newLength++;
            }
        }

        ArrayObj[] newArray = new ArrayObj[newLength];
        int counter = 0;

        for (ArrayObj arrayObj : array) {
            if (arrayObj.getValue().compareTo(lower) >= 0
                    && arrayObj.getValue().compareTo(upper) <= 0) {
                newArray[counter++] = arrayObj;
            }
        }

        return printSet(newArray);
    }


    @Override
    public RmitMultiset union(RmitMultiset other) {
        boolean found;
        int index = 0;
        int count = 0;
        ArrayObj[] newArray = new ArrayObj[array.length];
        //copy the objects in first array to the array which will be returned
        System.arraycopy(array, 0, newArray, 0, array.length);

        if (other instanceof ArrayMultiset) {
            ArrayObj[] otherArray = ((ArrayMultiset) other).array;

            //checking whether each object in the second array is new to the first array
            for (ArrayObj arrayObj : otherArray) {
                found = false;

                for (int j = 0; j < array.length; j++) {
                    if (array[j].getValue().equals(arrayObj.getValue())) {
                        count = arrayObj.getCount() + array[j].getCount();
                        index = j;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    ArrayObj newObj = new ArrayObj(arrayObj.getValue());
                    newObj.setCount(count);
                    newArray[index] = newObj;
                } else {
                    newArray = addSize(newArray);
                    newArray[newArray.length - 1] = arrayObj;
                }
            }
        }

        ArrayMultiset newSet = new ArrayMultiset();
        newSet.array = newArray;
        return newSet;
    }


    @Override
    public RmitMultiset intersect(RmitMultiset other) {
        ArrayObj[] newArray = new ArrayObj[0];
        boolean found;
        int index = 0;
        ArrayObj[] bigArray;
        ArrayObj[] smallArray;

        if (other instanceof ArrayMultiset) {
            ArrayObj[] otherArray = ((ArrayMultiset) other).array;

            //find the array with larger size
            if (array.length >= otherArray.length) {
                bigArray = array;
                smallArray = otherArray;
            } else {
                bigArray = otherArray;
                smallArray = array;
            }

            //check whether each object in the smaller array is in the larger array
            for (ArrayObj arrayObj : bigArray) {
                found = false;

                for (int j = 0; j < smallArray.length; j++) {
                    if (arrayObj.getValue().equals(smallArray[j].getValue())) {
                        index = j;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    newArray = addSize(newArray);

                    if (arrayObj.getCount() == (smallArray[index].getCount())) {
                        if (newArray[0] == null) {
                            newArray[0] = arrayObj;
                        } else newArray[newArray.length - 1] = arrayObj;
                    } else {
                        int count = Math.min(arrayObj.getCount(), smallArray[index].getCount());

                        if (newArray[0] == null) {
                            newArray[0] = arrayObj;
                            newArray[0].setCount(count);
                        } else {
                            newArray[newArray.length - 1] = arrayObj;
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
    public RmitMultiset difference(RmitMultiset other) {
        ArrayObj[] newArray = new ArrayObj[0];
        boolean found;
        int index = 0;

        if (other instanceof ArrayMultiset) {
            ArrayObj[] otherArray = ((ArrayMultiset) other).array;

            for (ArrayObj arrayObj : array) {
                int count = arrayObj.getCount();
                found = false;

                //check duplicate value in these 2 arrays
                for (int j = 0; j < otherArray.length; j++) {
                    if (arrayObj.getValue().equals(otherArray[j].getValue())) {
                        found = true;
                        count = Math.abs(arrayObj.getCount() - otherArray[j].getCount());
                        index = j;
                        break;
                    }
                }
                // if found duplicate value, check the count of the object
                if (found) {
                    if (arrayObj.getCount() > (otherArray[index].getCount())) {
                        newArray = addSize(newArray);

                        ArrayObj newObj = new ArrayObj(arrayObj.getValue());
                        if (newArray[0] == null) {
                            newArray[0] = newObj;
                            newArray[0].setCount(count);
                        } else {
                            newArray[newArray.length - 1] = newObj;
                            newArray[newArray.length - 1].setCount(count);
                        }
                    }
                } else {
                    newArray = addSize(newArray);

                    if (newArray[0] == null) {
                        newArray[0] = arrayObj;
                    } else {
                        newArray[newArray.length - 1] = arrayObj;
                    }
                }
            }
        }

        ArrayMultiset newSet = new ArrayMultiset();
        newSet.array = newArray;
        return newSet;
    }

    //Array object that has 2 attributes, value and count
    public class ArrayObj {
        private String value;
        private int count;


        public ArrayObj(String value) {
            this.value = value;
            this.count = 1;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public ArrayObj[] bubbleSort(ArrayObj[] array) {
        int n = array.length;
        boolean swapped;
        ArrayObj tempObj;

        if (n != 1) {
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (array[j].getCount() < array[j + 1].getCount()) {
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


    public ArrayObj[] addSize(ArrayObj[] array) {
        ArrayObj[] tempArray;
        tempArray = array;
        array = new ArrayObj[array.length + 1];

        if (tempArray.length >= 0) System.arraycopy(tempArray, 0, array, 0, tempArray.length);
        return array;
    }
}