/*
 * AUTHOR: Cooper Harris/ 210 Instructors
 * FILE: WikiScraper.java
 * PURPOSE This program scrapes wikilinks off the internet and creates
 * a HashSet of all the wikilinks on a given page. 
 * 
 */

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * TODO: You will have to implement memoization somewhere in this class.
 */
public class WikiScraper {
	private static Map<String, Set<String>> memo = new HashMap<>();
	
	/*
     * Method to create and return the set of wikilinks 
     * found on a page.
     * 
     * @return links - The set of wikilinks
     */
	public static Set<String> findWikiLinks(String link) {
		if (memo.containsKey(link)) {
			return memo.get(link);
		}
		
		String html = fetchHTML(link);
		Set<String> links = scrapeHTML(html);
		memo.put(link, links);
		
		return links;
	}
	
	/*
     * Method to get the html of a wiki page.
     * 
     * @return buffer.toString() - a string of html that
     * contains the information used to find wikilinks
     */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	
	/*
     * Takes a string and turns it into a usable url.
     * 
     * @return the url for a wiki page
     */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
     * Method to search an html string for wiki links and add
     * them to a set.
     * 
     * @return stringSet - The set of wikilinks
     */
	private static Set<String> scrapeHTML(String html) {
		Set<String> stringSet = new HashSet<>();
		
		String s = html;    
		
	    int index = s.indexOf("<a href=\"/wiki/");
	    
	    while(index != -1)  {
	        s = s.substring(index+15); 
	        
	        String link = s.substring(0, s.indexOf("\""));
	        
	        if (link.indexOf('#') == -1 && link.indexOf(':') == -1) {
	        	stringSet.add(link);
	        }
	        index = s.indexOf("<a href=\"/wiki/");
	    }
		return stringSet;
	}
}
