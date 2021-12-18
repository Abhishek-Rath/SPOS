// Abhishek Rath
// TECOC313

import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

public class pageReplacement {


    static int FIFO(int pages[], int n, int page_frames) {

        HashSet<Integer> set = new HashSet<>(page_frames);  //Represents set of current pages

        // using linked list implementation of queue to store pages in FIFO manner
        Queue<Integer> queue = new LinkedList<>();  

        int page_faults = 0;
        for(int i=0;i<n;i++) {
            if(set.size() < page_frames) {
                if(!set.contains(pages[i])) {
                    set.add(pages[i]);
                    page_faults++;
                    queue.add(pages[i]);
                }
            }
            else {
                if(!set.contains(pages[i])) {
                    int value = queue.peek(); //get head of queue
                    queue.poll(); //remove head of queue i.e. first element
                    set.remove(value);  //also remove val from set

                    set.add(pages[i]); //insert new page in set
                    queue.add(pages[i]); //insert new page in queue
                    page_faults++;
                }
            }
        }
        return page_faults;
    }

    
    static int LRU(int pages[], int n, int page_frames) {

        // Set to check whether page is present in set or not
        HashSet<Integer> set = new HashSet<>(page_frames);

        // store least recently used indexes in a map
        HashMap<Integer, Integer> indexes = new HashMap<>();

        int page_faults = 0;
        for (int i=0; i<n;i++) {

            // Check whether set can hold pages
            if(set.size() < page_frames) {

                // If the page is not already present
                if(!set.contains(pages[i])) {

                    // Add the page into set and increment the page_fault
                    set.add(pages[i]);
                    page_faults++;
                }

                indexes.put(pages[i], i); // Store index of each page
            }

            // Set is full
            else  {
                if(!set.contains(pages[i])) {

                    int lru = Integer.MAX_VALUE;
                    int value = Integer.MIN_VALUE;

                    Iterator<Integer> itr = set.iterator();
                     
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if (indexes.get(temp) < lru)
                        {
                            lru = indexes.get(temp);
                            value = temp;
                        }
                    }
                 
                    // Remove the indexes page
                    set.remove(value);
                   //remove lru from hashmap
                   indexes.remove(value);
                    // insert the current page
                    set.add(pages[i]);
      
                    page_faults++;
                }
      
                // Update the current page index
                indexes.put(pages[i], i);

            }
        }
        return page_faults;
    }

    static int optimal(int pages[], int n,  int page_frames) {

        HashSet<Integer> set = new HashSet<>(page_frames);

        HashMap<Integer, Integer> indexes = new HashMap<>();

        int page_faults = 0;
        for (int i=0; i<n;i++) {
            // Check whether set can hold pages
            if(set.size() < page_frames) {

                // If the page is not already present
                if(!set.contains(pages[i])) {

                    // Add the page into set and increment the page_fault
                    set.add(pages[i]);
                    page_faults++;
                }
            }

            // Set is full
            else  {
                if(!set.contains(pages[i])) {
                    indexes.clear();
                    for(int j = i+1; j < n; j++){
                        if((indexes.get(pages[j]) == null)){
                            
                            indexes.put(pages[j], j);
                            
                        }
                        else{
                            if(indexes.get(pages[j]) <= i){
                                indexes.put(pages[j], j);
                            }
                        }

                    }

                    int opt = Integer.MIN_VALUE;
                    int value = Integer.MIN_VALUE;

                    Iterator<Integer> itr = set.iterator();
                     
                    ArrayList<Integer> not_in_pages = new ArrayList<>();
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if(indexes.get(temp) != null){
                            if (indexes.get(temp) > opt)
                            {
                                opt = indexes.get(temp);
                                value = temp;
                            }
                        }
                        else{
                            not_in_pages.add(temp);
                        }
                    }
                    if(not_in_pages.size() == 0){
                        
                        // Remove the indexes page
                        set.remove(value);
                        //remove opt from hashmap
                        indexes.remove(value);
                        // insert the current page
                        set.add(pages[i]);
                    }
                    else{
                        set.remove(not_in_pages.get(not_in_pages.size()-1));
                        set.add(pages[i]);
                    }
                    page_faults++;
                }
            }
        }
        // System.out.println("Page Faults " + page_faults);
        return page_faults;
    }

    public static void main(String[] args) {
        // System.out.println("Hello, world!");
        int pages[] = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
        int page_frames = 4;
        int fifo = FIFO(pages, pages.length, page_frames);
        System.out.println("Number of page faults in fifo: "+fifo);

        int LRU = LRU(pages, pages.length, page_frames);
        System.out.println("Number of page faults in LRU: " + LRU);

        int opt = optimal(pages, pages.length, page_frames);
        System.out.println("Number of page faults in Optimal: " + opt);

    }
}



// Output
/* 
    Number of page faults in fifo: 7
    Number of page faults in LRU: 6    
    Number of page faults in Optimal: 6
*/