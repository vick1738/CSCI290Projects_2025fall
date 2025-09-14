/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 *
 * 8/2015 Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code  
 * 2024 - Updated for Java 17
 *****************************************************************************
 */

package student;

import java.util.*;
import java.util.stream.Stream;

/**
 * Search by Artist Prefix searches the artists in the song database
 * for artists that begin with the input String
 * @author Bob Booth
 */
public class SearchByArtistPrefix {
    // keep a local direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
        String prefix = artistPrefix.toLowerCase();

        // Create comparator and temporary song
        Song.CmpArtist cmp = new Song.CmpArtist();
        cmp.resetCmpCnt();
        Song tempSong = new Song(prefix, "", "");

        // Binary search to find approximate position
        int index = Arrays.binarySearch(songs, tempSong, cmp);
        int binaryComparisons = cmp.getCmpCnt();

        // Handle binary search result
        if (index < 0) {
            index = -index - 1; // Insertion point
        }

        // Now find all matches starting from the approximate position
        List<Song> matches = new ArrayList<>();
        int listComparisons = 0;

        // First, walk backward to find the first match
        int start = index;
        while (start > 0) {
            String currentArtist = songs[start - 1].getArtist().toLowerCase();
            listComparisons++;
            if (!currentArtist.startsWith(prefix)) {
                break;
            }
            start--;
        }

        // Then, walk forward to collect all matches
        int end = index;
        while (end < songs.length) {
            String currentArtist = songs[end].getArtist().toLowerCase();
            listComparisons++;
            if (!currentArtist.startsWith(prefix)) {
                break;
            }
            matches.add(songs[end]);
            end++;
        }

        // statistics
        int totalComparisons = binaryComparisons + listComparisons;
        System.out.println("Searching for '" + artistPrefix + "'");
        System.out.println("Index from binary search is: " + index);
        System.out.println("Binary search comparisons: " + binaryComparisons);
        System.out.println("Comparisons to build the list: " + listComparisons);
        System.out.println("Actual complexity is: " + totalComparisons);
        System.out.println("k is " + matches.size());
        double log2n = Math.log(songs.length) / Math.log(2);
        System.out.println("log₂(n) = " + (int)Math.ceil(log2n));
        System.out.println("Theoretical complexity k + log₂(n) is: " +
                (matches.size() + (int)Math.ceil(log2n)));
        System.out.println("----------------------------------------");

        return matches.toArray(new Song[0]);
    }

    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties -
     * the first argument is the data file name and the second is the partial
     * artist name, e.g. be which should return beatles, beach boys, bee gees,
     * etc.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [be]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            // Display results
            System.out.println("Total matches: " + byArtistResult.length);
            System.out.println("First 10 matches:");
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}