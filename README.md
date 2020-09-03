# Multist implementation in Java

## Contributors
Shawn Liu @ https://github.com/realshawnliu
- Array
- Binary Search Tree

Saneyar Tajjdeen Khazin @ https://github.com/Magnuscake
- Ordered Linked List
- Dual Linked List

Multisets, also known as bags, are an important abstract data type. Sets aer unordered collection of elements with unique values. Multisets are a modification of Sets such that they allow duplicate values. Nodes are represented as a 2-tuple form _(A, m)_, where A represents the value or item and m the number of instances of that item.

The multiset implementation has been done based on 4 data structures;

1. Array (unordered)
2. Ordered singly linked list
3. Binary search tree (BST)
4. Dual ordered linked list


### Supported operation

- Add an element
- Search for an element and get the number of instances for that element
- Search for an element with a particular number of instances
- Contains an element
- Remove one instance of an element
- Union two multisets
- Intersect two multisets
- Compute the difference between two multisets
- Print elements ordered by number of instances
- Print out a subset from a specified range
