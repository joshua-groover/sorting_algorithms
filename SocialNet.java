/*-----------------------------------------------------------------------------
GWU CSCI1112 Fall 2019
author: Joshua Groover

This class encapsulates the logic necessary to perform simple steganography 
cyphering.
------------------------------------------------------------------------------*/
import java.awt.*;
import java.awt.image.*;
import java.lang.*;
import java.util.*;
import java.text.*;

public class SocialNet {
    //------------------------------------------------------------------------- 
    // Base Problems
    //------------------------------------------------------------------------- 
    /// A referential copy (shallow copy of each row) and not an element-wise 
    /// copy (deep copy).  We are sorting elements with respect to the original
    /// data rather than generating a new set of data.
    /// @param posts data containing the rows to reference
    /// @return the shallow copy of rows
    public static int[][] createView(int[][] posts) {
        // TODO : Implement Here
        
	//initialize a new 2d array, #rows = #rows in posts
	int[][] view = new int[posts.length][];

	//iterate through each row, set the posts row equal to the view row
	for (int row=0; row<posts.length; row++){
	    view[row] = posts[row];
	}

	return view;
    }
 
    //------------------------------------------------------------------------- 
    /// Compute the differential between "ups" (at index 1) and "downs" 
    /// (at index 2). The differential is not maintained in the data but is a 
    /// virtual field derived by the calculation performed here
    public static int differential(int[] post) {
        // TODO : Implement Here

	//calculate the difference between index 1&2
	int dif = post[1]-post[2];

        return dif;
    }

    //------------------------------------------------------------------------- 
    /// Performs a comparison between two posts that is equivalent to a less
    /// than operation so that a sort can use this function to order posts.
    /// The less than criteria is an evaluation between the differentials of
    /// two posts.
    /// @param post1 a post record that is used as the "left" operand for a
    ///        less than comparison 
    /// @param post2 a post record that is used as the "right" operand for a
    ///        less than comparison 
    /// @return returns true if the computed differential for post1 is less than
    ///         the computed differential for post2; otherwise, returns false 
    ///         (false implies that differential for post1 is greater than or
    ///         equal to post2)
    public static boolean lessThan(int[] post1, int[] post2) {
        // TODO : Implement Here
        
	//find the difference between ups/downs for both posts
	int dif1 = differential(post1);
	int dif2 = differential(post2);

	//if dif1<dif2, return true
	if (dif1<dif2)
	    return true;

	//if dif1>=dif2, return falsee
	else
	    return false;
    }
    //------------------------------------------------------------------------- 
    /// Swaps references to posts.  Note that this is a "shallow" swap and not 
    /// a "deep" swap
    /// @param view A shallow copy of a set of posts 
    /// @param i the index of the first reference to swap
    /// @param j the index of the second reference to swap
    public static void swapPosts(int[][] view, int i, int j) {
        // TODO : Implement Here
	
	//create a copy of view's pointers
	int[][] view_copy = new int[view.length][];

	//copy all of the pointers into view_copy
	for (int idx=0; idx<view.length; idx++){
	    view_copy[idx] = view[idx];
	}
	
	//switch the pointers
	view[i] = view_copy[j];
	view[j] = view_copy[i];

    }

    //------------------------------------------------------------------------- 
    /// Sorts (shallow) a set of references to posts in descending order 
    /// subject to the differential between ups and downs using one of
    /// the iterative sorts we discussed in class, i.e. selection, bubble, or 
    /// insertion sort
    /// @param view A shallow copy of a set of posts 
    /// @return a set of profile information containing a count of 
    ///         0: allocations, 1:comparisons, and 2: swaps
    public static int[] iterativeSort(int[][] view) {
        // profile[0:allocs (ignore profile), 1:comparisons, 2:swaps]
        int[] profile = new int[3];

	// TODO : Implement Here
        	
	//initialize things
	int allocs = 0;
	int comparisons = 0;
	int swaps = 0;
	
	//iterate through all of the posts
	for (int pos=0; pos<view.length-1; pos++){
	    
	    //set the default min equal to the pos
	    int max_pos = pos;
	    allocs++;
	
	    //iterate through all of the posts past the initial point
	    for (int sub_pos=max_pos+1; sub_pos<view.length; sub_pos++){
	
		comparisons++;

		//if the dif of the current is greater than start, set
		//equal to new max
		if (lessThan(view[max_pos], view[sub_pos])){
		    allocs++;
		    max_pos = sub_pos;
		}
	    }
	    
	    //swap the max with the original pos
	    swaps++;
	    swapPosts(view, pos, max_pos);

	}
	//fill in the profile list to return
	profile[0] = allocs;
	profile[1] =  comparisons;
	profile[2] =  swaps;
		
        return profile;
    }

    //------------------------------------------------------------------------- 
    // Extension Problems
    //------------------------------------------------------------------------- 
    /// Sorts (shallow) a set of references to posts in descending order 
    /// subject to the differential between ups and downs using a recursive
    /// approach, i.e. quicksort.
    /// @param view A shallow copy of a set of posts 
    /// @return a set of profile information containing a count of 
    ///         0: allocations, 1:comparisons, and 2: swaps
    
    static int swap_global = 0;
    static int comparisons_global = 0;
    static int allocs_global = 0;

    public static int[] recursiveSort(int[][] view) {
        // profile[0:allocs (ignore profile), 1:comparisons, 2:swaps]
        int[] profile = new int[3];

        // TODO : Implement Here
        
	quickSort(view, 0, view.length-1);
	
	//fill in the profile list to return
        profile[0] = allocs_global;
        profile[1] =  comparisons_global;
        profile[2] =  swap_global;
	return profile;
    }

    //performs a recursive quick sort
    //@param view: a list of all the posts
    //@param left: leftmost position for partition
    //@param right: rightmost position for partition
    static void quickSort(int[][] view, int left, int right) {
	
	//check if left position is less than right (otherwise finished)
	if (left<right){
	
	   //partion the right place for the leftmost element
	   int  partitionPosition = quickSortPartition(view, left, right);

	   //recursive on the left side
	   quickSort(view, left, partitionPosition-1);

	   //recursive on the right side
	   quickSort(view, partitionPosition+1, right);
	}


    }

    //determines the position to split the data on, then determines which data
    //difs are larger and smaller than the split. Then, sorts based on that
    //@params view: the list of posts
    //@left: the left most element involved in the partition
    //@right: the right most element involved in the partition
    static int quickSortPartition(int[][] view, int left, int right){
	
	//if the value of left and right the same, nothing to sort
	comparisons_global++;
	if (left==right)
	    return left;
	
	//set the partition element to the rightmost
        allocs_global++;
	int swap_pos = right;

	//iterate through all of the posts
	for (int post=right-1; post>=left; post--){
	    
	    comparisons_global++;
            //if the value of current post is greater than right swap
            if(lessThan(view[post], view[right])){
		swap_global++;
		swap_pos--;
		swapPosts(view, swap_pos, post);
	    }
	}
	
	swap_global++;
	//swap the pivot and the right most poitn
	swapPosts(view, swap_pos, right);
	
	return swap_pos;

    }
}


