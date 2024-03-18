/*
 * AUTHOR: Cooper Harris
 * FILE: MaxPQ.java
 * PURPOSE: This program creates a priority queue backed by 
 * a binary maximum heap. The queue holds ArrayLists<String>.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * TODO: This file should contain your priority queue backed by a binary
 * max-heap.
 */
public class MaxPQ {
	private static final int DEFAULT_SIZE = 100;
    private ArrayList<String>[] array;
    private int size;
    private String start;
    private String end;
    private static Map<String, Integer> memo = new HashMap<>();
    private static Map<Set<String>, String[]> linksDict = new HashMap<>();
    

    public MaxPQ(String start, String end) {
        array = (ArrayList<String>[]) new ArrayList[DEFAULT_SIZE];
        size = 1;
        this.start = start;
        this.end = end;
    }
    
    /*
     * Helper method to grow the size of the array backing the queue
     * 
     * @return null
     */
    private void growArray() {
    	ArrayList<String>[] newArray = (ArrayList<String>[]) new ArrayList[4 * array.length];

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }

        array = newArray;
    }
    
    /*
     * Creates a copy of the ladder passed in.
     * 
     * @return the new ArrayList<String>
     */
    public ArrayList<String> copy(ArrayList<String> copy) {
    	ArrayList<String> newArrayList = (ArrayList<String>) copy.clone();
    	
    	return newArrayList;
    }

    /*
     * Add an element to the back of the queue. It will then be bubbled
     * up if neccessary.
     * 
     * @param ladder - The ladder object to be enqueued
     * 
     * @return null
     */
    public void enqueue(ArrayList<String> ladder) {
        if (array.length == size) {
            growArray();
        }

        array[size] = ladder;

        if (size > 1) {
            bubbleUp(size);
        }
        size++;
    }
    
    /*
     * Checks to see how many links two wiki pages share. I used
     * this function to measure priority.
     * 
     * @param startLinks - The first page to check
     * @param compare - The second page to check
     * 
     * @return the number of links the two pages share
     */
    public int linksInCommon(Set<String> startLinks, Set<String> compare) {
    	String[] linksArray = null;
    	
    	if (linksDict.containsKey(startLinks)) {
    		linksArray = linksDict.get(startLinks);
    	} else {
    		linksArray = startLinks.toArray(new String[startLinks.size()]);
    		linksDict.put(startLinks, linksArray);
    	}
    	
		int count = 0;
		
		for (int i = 0; i < linksArray.length; i++) {
			String link = linksArray[i];
			
			if (startLinks.contains(link) && compare.contains(link)) {
				count++;
			}
		}
		return count;
	}
    
    /*
     * A second part to linksInCommon that handles getting
     * the actual wikilinks in a set.
     * 
     * @param start - The first page to create a set of wikilinks for
     * @param end - The second page to create a set of wikilinks for
     * 
     * @return the number of links the two pages share
     */
    public int getLinksInCommon(String start, String end) {
    	if (memo.containsKey(start)) {
    		return memo.get(start);
    	}
    	
    	Set<String> startLinks = WikiScraper.findWikiLinks(start);
    	Set<String> endLinks = WikiScraper.findWikiLinks(end);
    	
    	int commonLinks = linksInCommon(startLinks, endLinks);
    	
    	memo.put(start, commonLinks);
		return commonLinks;
    }
    
    /*
     * A private helper method to handle shifting the
     * elements to their correct orders. This function
     * moves elements left if neccessary.
     * 
     * @Param index - the index of the current element
     * to be checked against its parent.
     * 
     * @Return null
     */
    private void bubbleUp(int index) {
    	int i = index;
    	while (i > 1) {
    		ArrayList<String> parent = array[i / 2];
        	ArrayList<String> child = array[i];
        	
        	int parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
        	int childPriority = getLinksInCommon(child.get(child.size() - 1), end);
            // swapping indeces of parent and child if child has higher priority
            if (parentPriority < childPriority) {
                array[i / 2] = child;
                array[i] = parent;
            }
            i /= 2;
    	}
//        if (index > 1) {
//        	ArrayList<String> parent = array[index / 2];
//        	ArrayList<String> child = array[index];
//        	
//        	int parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
//        	int childPriority = getLinksInCommon(child.get(child.size() - 1), end);
//            // swapping indeces of parent and child if child has higher priority
//            if (parentPriority < childPriority) {
//                array[index / 2] = child;
//                array[index] = parent;
//            }
//            bubbleUp(index / 2);
//        }
    }
    
    /*
     * returns the index of the ladder with the highest priority
     * 
     * @return the highest priority index
     */
    public int getHighestPriorityIndex() {
    	int index = 0;
    	int count = 0;
    	
    	for (int i = 1; i < size; i++) {
    		ArrayList<String> ladder = array[i];
        	
        	int ladderPriority = getLinksInCommon(ladder.get(ladder.size() - 1), end);
        	
    		if (ladderPriority > count) {
    			index = i;
    			count = ladderPriority;
    		}
    	}
    	return index;
    }
    
    /*
     * Remove and return the ladder with the highest priority.
     * 
     * @return the element with highest priority
     */
    public ArrayList<String> dequeue() {
    	if (size == 2) {
    		size--;
    		return array[1];
    	}
    	
    	int highestPriority = getHighestPriorityIndex();
        ArrayList<String> ladderRemoved = array[highestPriority];

        array[highestPriority] = array[size - 1];
        array[size - 1] = null;
        
        bubbleDown(highestPriority);

        size--;
        
        return ladderRemoved;
    }

    /*
     * A private helper method to handle shifting the
     * elements to their correct orders.
     * 
     * @Param index - the index of the current element
     * to be checked against its child. This function
     * moves elements right if neccessary.
     * 
     * @Return null
     */
    private void bubbleDown(int index) {
//    	int i = index;
//    	while (i < array.length && index != 0) {
//    		ArrayList<String> parent = array[i];
//            int parentPriority = 0;
//            int child1Priority = 0;
//            int child2Priority = 0;
//            // checking to make sure the parent has not already been removed
//            if (parent != null) {
//            	parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
//            }
//            // Checking to make sure the child indeces are not out of bounds
//            if ((i * 2) + 1 < array.length || (i * 2) < array.length) {
//            	ArrayList<String> child1 = array[i * 2];
//            	ArrayList<String> child2 = array[(i * 2) + 1];
//            	// making sure child1 exists
//                if (child1 != null) {
//                	child1Priority = getLinksInCommon(child1.get(child1.size()-1), end);
//                    // Swapping with child1 if neccessary
//                    if (parentPriority < child1Priority) {
//                        array[i] = child1;
//                        array[i * 2] = parent;
//                        // resetting the parent and priority in case child 2 needs to be switched
//                        parent = array[i];
//                        parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
//                      // making sure child2 exists
//                    }
//                    if (child2 != null) {
//                    	child2Priority = getLinksInCommon(child2.get(child2.size()-1), end);
//                        // Swapping with child2 if neccessary
//                        if (parentPriority < child2Priority) {
//                            array[i] = child2;
//                            array[(i * 2) + 1] = parent;
//                            // if the priority of two elements is equal, compare
//                            // them alphabetically
//                        }
//                    }
//                }
//            }
//            i *= 2;
//    	}
        if (index < size && index != 0) {
            ArrayList<String> parent = array[index];
            int parentPriority = 0;
            int child1Priority = 0;
            int child2Priority = 0;
            // checking to make sure the parent has not already been removed
            if (parent != null) {
            	parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
            }
            // Checking to make sure the child indeces are not out of bounds
            if ((index * 2) + 1 < array.length || (index * 2) < array.length) {
            	ArrayList<String> child1 = array[index * 2];
            	ArrayList<String> child2 = array[(index * 2) + 1];
            	// making sure child1 exists
                if (child1 != null) {
                	child1Priority = getLinksInCommon(child1.get(child1.size()-1), end);
                    // Swapping with child1 if neccessary
                    if (parentPriority < child1Priority) {
                        array[index] = child1;
                        array[index * 2] = parent;
                        // resetting the parent and priority in case child 2 needs to be switched
                        parent = array[index];
                        parentPriority = getLinksInCommon(parent.get(parent.size() - 1), end);
                      // making sure child2 exists
                    } else if (child2 != null) {
                    	child2Priority = getLinksInCommon(child2.get(child2.size()-1), end);
                        // Swapping with child2 if neccessary
                        if (parentPriority < child2Priority) {
                            array[index] = child2;
                            array[(index * 2) + 1] = parent;
                            // if the priority of two elements is equal, compare
                            // them alphabetically
                        }
                    }
                }
            }
            bubbleDown(index * 2);
        }
    }

    /*
     * Returns true if the queue has no elements.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * Returns the number of elements in the queue.
     */
    public int size() {
        return size - 1;
    }

    /*
     * ToString method that returns the queue in the form
     * {name, (priority), name (priority)}
     * 
     * @return ret - String representing queue
     */
    @Override
    public String toString() {
        String ret = "{";

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                ret += array[i].toString() + ", ";
            }
        }
        if (ret.length() >= 2) {
            ret = ret.substring(0, ret.length() - 2);
        }

        ret += "}";

        return ret;
    }
}