/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 *  *
 * 2025-09-04 - Keith - Finalized main() to print total songs and first 10
 *                      using Stream.of(...) as shown in the assignment.
 *
 * 2025-09-03 - Keith - Implemented sorting with Arrays.sort() so songs
 *                      are ordered by artist then title via Song.compareTo.
 *
 * 2025-09-03 - Keith - Built ArrayList to collect Song objects, then
 *                      converted it into a Song[] using toArray().
 *
 * 2025-09-03 - Keith - Added regex-based file loading to parse ARTIST,
 *                      TITLE, and LYRICS sections while preserving newlines.
 *
 *
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing
 ************************************************************************
 */

package student;

//author keith all imports needed for task 2
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SongCollection.java
 * Reads the specified data file and build an array of songs.
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore
     * not portable. Java runs on a virtual machine that IS portable. So this
     * is permissable because we are programming in Java and Java runs on a
     * virtual machine not directly on the hardware.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */

    //author keith constructor reads the file once then builds Song[] via ArrayList,and sorts by artist then title using Song.compareTo().

    public SongCollection(String filename) {
        try {
            loadFromFile(filename);
            Arrays.sort(songs); //
        } catch (IOException ioe) {

            System.err.println("Error reading file: " + ioe.getMessage());
            songs = new Song[0];
        }
    }

    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     *
    //Author Keith Returns the sorted song array for use by later code

     */
    public Song[] getAllSongs() {
        return songs;
    }



    /**
     // Author: Keith Reads the whole file and parses ARTIST/TITLE/LYRICS triplets into Song objects.
     // Also Preserves lyric newlines; final storage a Song[].

     */
    private void loadFromFile(String path) throws IOException {
        String content = readWholeFile(path);


        Pattern p = Pattern.compile(
                "ARTIST=\"(.*?)\"\\s*TITLE=\"(.*?)\"\\s*LYRICS=\"(.*?)\"",
                Pattern.DOTALL
        );
        Matcher m = p.matcher(content);

        ArrayList<Song> list = new ArrayList<>();
        while (m.find()) {
            String artist = m.group(1).trim();
            String title  = m.group(2).trim();

            String lyrics = m.group(3).replace("\r\n", "\n").replace("\r", "\n");
            if (lyrics.endsWith("\n")) {
                lyrics = lyrics.substring(0, lyrics.length() - 1);
            }

            list.add(new Song(artist, title, lyrics));
        }

        songs = list.toArray(new Song[0]);
    }

    /**
     //Author keith  Reads the entire file into a single String efficiently

     */
    private static String readWholeFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder(1 << 20);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            char[] buf = new char[8192];
            int n;
            while ((n = br.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
        }
        return sb.toString();
    }

    /**
     * unit testing method
     * @param args command line arguments (expects a song file path)
     *
    //Author  Keith Prints total count and the first up to 10 songs as required.


     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        Song[] list = sc.getAllSongs();

        System.out.println("Total songs: " + list.length);


        Stream.of(list).limit(10).forEach(System.out::println);

    }
}