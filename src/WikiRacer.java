/*
 * AUTHOR: Cooper Harris / 210 Instructors
 * FILE: WikiRacer.java
 * PURPOSE: This program uses WikiScraper to get wikilinks and a 
 * MaxPQ to create ladders to get from a start wikilink to an end wikilink.
 * 
 * USAGE: Takes input from the command line in the form Start_Page End_Page.
 * Returns an ArrayList<String> of the links used to get from the start page
 * to the endpage
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WikiRacer {
	/*
	 * Do not edit this main function
	 */
	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
     * Creates the wikiladder of links used to get from start to end.
     * Also prints the time the ladder took to build.
     * 
     * @param start - the start wiki page
     * @param end - the end wiki page
     * 
     * @return A List<String> of the wikilinks used or an empty 
     * List<String> if no path could be found.
     */
	private static List<String> findWikiLadder(String start, String end) {	
		long timeStart = System.currentTimeMillis();
		
		MaxPQ queue = new MaxPQ(start, end);
		
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start);
		queue.enqueue(ladder);
		// Keeping track of links that have been visited so far
		Set<String> visited = new HashSet<>();
		
		while (!queue.isEmpty()) {
			// The highest priority ladder
			ArrayList<String> newest = queue.dequeue();
			// checking for the desiered page in the current page's links
			Set<String> links = WikiScraper.findWikiLinks(newest.get(newest.size() - 1));
			visited.add(newest.get(newest.size() - 1));
			
			if (links.contains(end)) {
				newest.add(end);
				long timeEnd = System.currentTimeMillis();
				System.out.println("Getting from " + start + " to " + end + " takes " + 
                        (timeEnd - timeStart) / 1000 + " seconds");
				return (List<String>) newest;
			}
			// parallelization code
			links.parallelStream().forEach(link -> {
				WikiScraper.findWikiLinks(link);
				});
			// creating an array of the set's contents to iterate through it
			String[] linksArray = links.toArray(new String[links.size()]);
			
			for (int i = 0; i < links.size(); i++) {
				if (!visited.contains(linksArray[i])) {
					// Create a copy of the current partial-ladder.
					ArrayList<String> copy = queue.copy(newest);
					
					// Put the neighbor page String name on top of the copied ladder.
					copy.add(linksArray[i]);
					visited.add(linksArray[i]);
					
					// Add the copied ladder to the queue.
					queue.enqueue(copy);
				}
			}
		}
		// exit means there was no ladder found
		System.out.println("No path");
		return new ArrayList<String>();
	}
	
}
