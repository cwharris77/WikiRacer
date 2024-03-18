//import java.util.ArrayList;
//
//import MaxPQ.Ladder;
//
//public class Storage {
//	private static final int DEFAULT_SIZE = 10;
//	private Ladder[] array;
//	private int size;
//	
//	public MaxPQ() {
//		array = new Ladder[DEFAULT_SIZE];
//		size = 1;
//	}
//	
//	/*
//     * Helper method to grow the size of the array backing the queue
//     * 
//     * @return null
//     */
//    private void growArray() {
//        Ladder[] newArray = new Ladder[2 * array.length];
//
//        for (int i = 0; i < size; i++) {
//            newArray[i] = array[i];
//        }
//
//        array = newArray;
//    }
//    
//    /*
//     * Add an element to the back of the queue. It will then be bubbled
//     * up if neccessary.
//     * 
//     * @param patient - The ladder object to be enqueued
//     * 
//     * @reurn null
//     */
//    public void enqueue(String s, int priority) {
//        if (array.length == size) {
//            growArray();
//        }
//        
//        if (size == 1) {
//        	array[size] = createLadder(s, priority);
//        } else {
//        	array[size] = createLadder(array[size / 2], priority);
//        	array[size].add(s);
//        }
//        
//
//        if (size > 1) {
//            bubbleUp(size);
//        }
//        size++;
//    }
//    
//    public void enqueue(Ladder ladder, int priority) {
//        if (array.length == size) {
//            growArray();
//        }
//        
//        array[size] = ladder;
//        ladder.setPriority(priority);
//        
//        if (size > 1) {
//            bubbleUp(size);
//        }
//        size++;
//    }
//	
//    /*
//     * A private helper method to handle shifting the
//     * elements to their correct orders. This function
//     * moves elements left if neccessary.
//     * 
//     * @Param index - the index of the current element
//     * to be checked against its parent.
//     * 
//     * @Return null
//     */
//    private void bubbleUp(int index) {
//        if (index > 1) {
//            Ladder parent = array[index / 2];
//            Ladder child = array[index];
//            
//            // swapping indeces of parent and child if child has lower priority
//            if (parent.priority < child.priority) {
//                array[index / 2] = child;
//                array[index] = parent;
//            } 
//            
//            bubbleUp(index / 2);
//        }
//    }
//    
//    public boolean isEmpty() {
//    	return size == 1;
//    }
//    
//    public Ladder dequeue() {
//        if (size == 1) {
//        	return null;
//        }
//        
//        int highestPriority = getHighestPriority();
//        Ladder ladderRemoved = array[highestPriority];
//
//        array[highestPriority] = array[size - 1];
//        array[size - 1] = null;
//
//        bubbleDown(highestPriority);
//
//        size--;
//
//        return ladderRemoved;
//    }
//    
//    private void bubbleDown(int index) {
//        if (index < size) {
//            Ladder parent = array[index];
//
//            // Checking to make sure the child indeces are not out of bounds
//            if ((index * 2) + 1 < array.length || (index * 2) < array.length) {
//                Ladder child1 = array[index * 2];
//                Ladder child2 = array[(index * 2) + 1];
//
//                if (child1 != null) {
//                    // Swapping with child1 if neccessary
//                    if (parent.getPriority() > child1.getPriority()) {
//                        array[index] = child1;
//                        array[index * 2] = parent;
//                    } 
//                } 
//                if (child2 != null) {
//                        // Swapping with child2 if neccessary
//                        if (parent.getPriority() > child2.getPriority()) {
//                            array[index] = child2;
//                            array[(index * 2) + 1] = parent;     
//                        } 
//                    }
//                }
//            bubbleDown(index * 2);
//        }
//    }
//    
//    public int getHighestPriority() {
//    	int index = 0;
//    	int count = 0;
//    	
//    	for (int i = 1; i < size; i++) {
//    		if (array[i].getPriority() > count) {
//    			index = i;
//    			count = array[i].getPriority();
//    		}
//    	}
//    	return index;
//    }
//    
//    
//    @Override
//    public String toString() {
//        String ret = "{";
//
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] != null) {
//                ret += array[i].toString() + ", ";
//            }
//        }
//        if (ret.length() >= 2) {
//            ret = ret.substring(0, ret.length() - 2);
//        }
//
//        ret += "}";
//
//        return ret;
//    }
//    
//    public Ladder createLadder(String s, int priority) {
//    	Ladder ladder = new Ladder(s, priority);
//    	
////    	for (int i = 0; i < ladder.getSize(); i++) {
////    		System.out.println(ladder.get(i));
////    	}
//    	
//    	return ladder;
//    }
//    
//    public Ladder createLadder(Ladder ladder, int priority) {
//    	Ladder newLadder = new Ladder(ladder, priority);
//    	
//    	return newLadder;
//    }
//    
//    public class Ladder {
//    	private ArrayList<String> list;
//    	private int priority;
//    	
//    	public Ladder(String s, int priority) {
//    		this.priority = priority;
//    		this.list = new ArrayList<>();
//    		list.add(s);
//    	}
//    	
//    	public Ladder(Ladder ladder, int priority) {
//    		this.priority = priority;
//    		this.list = new ArrayList<>();
//    		
//    		for (int i = 0; i < ladder.getSize(); i++) {
//    			this.list.add(ladder.get(i));
//    		}
//    	}
//    	
//    	public void add(String s) {
//    		list.add(s);
//    		
//    	}
//    	
//    	public void setPriority(int priority) {
//    		this.priority = priority;
//    	}
//    	
//    	public int getPriority() {
//    		return priority;
//    	}
//    	
//    	public int getSize() {
//    		return list.size();
//    	}
//    	
//    	public String get(int i) {
//    		return list.get(i);
//    	}
//    	
//    	@Override
//    	public String toString() {
//    		String ret = "";
//    		for (int i = 0; i < list.size(); i++) {
//    			ret += list.get(i).toString() + ", ";
//    		}
//    		ret = ret.substring(0, ret.length() - 2);
//    		return ret;
//    	}
//    	
//    }
//	
//}
//
//




//				
//			int mostInCommon = 0;
//			String bestLink = "";
//			
//			for (int i = 0; i < startLinks.size(); i++) {
//				String currLink = linksArray[i];
//				
//				Set<String> currentLinks = WikiScraper.findWikiLinks(currLink);
//				
//				if (linksInCommon(endLinks, currentLinks) > mostInCommon) {
//					bestLink = currLink;
//				}
//				
//			}
//			queue.enqueue(bestLink, mostInCommon);
//		}
