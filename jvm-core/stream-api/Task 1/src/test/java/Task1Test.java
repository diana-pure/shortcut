import com.example.streams.task2.StreamBasics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Task1Test {

    private StreamBasics basics;

    @BeforeEach
    void setUp() {
        basics = new StreamBasics();
    }

    // --- filterLongWords ---

    @Test
    void filterLongWords_returnsWordsLongerThanMinLength() {
        List<String> words = List.of("cat", "elephant", "dog", "hippopotamus", "ox");
        List<String> result = basics.filterLongWords(words, 3);
        assertEquals(List.of("elephant", "hippopotamus"), result);
    }

    @Test
    void filterLongWords_emptyList_returnsEmpty() {
        assertTrue(basics.filterLongWords(List.of(), 3).isEmpty());
    }

    @Test
    void filterLongWords_noneMatch_returnsEmpty() {
        List<String> result = basics.filterLongWords(List.of("a", "bb"), 5);
        assertTrue(result.isEmpty());
    }

    // --- toUpperCase ---

    @Test
    void toUpperCase_convertsAllWords() {
        List<String> result = basics.toUpperCase(List.of("hello", "world"));
        assertEquals(List.of("HELLO", "WORLD"), result);
    }

    @Test
    void toUpperCase_emptyList_returnsEmpty() {
        assertTrue(basics.toUpperCase(List.of()).isEmpty());
    }

    // --- wordLengths ---

    @Test
    void wordLengths_returnsLengthsInOrder() {
        List<Integer> result = basics.wordLengths(List.of("cat", "elephant", "ox"));
        assertEquals(List.of(3, 8, 2), result);
    }

    @Test
    void wordLengths_emptyList_returnsEmpty() {
        assertTrue(basics.wordLengths(List.of()).isEmpty());
    }

    // --- filterAndTransform ---

    @Test
    void filterAndTransform_filtersAndUpperCases() {
        List<String> words = List.of("cat", "elephant", "dog", "hippopotamus");
        List<String> result = basics.filterAndTransform(words, 3);
        assertEquals(List.of("ELEPHANT", "HIPPOPOTAMUS"), result);
    }

    @Test
    void filterAndTransform_emptyList_returnsEmpty() {
        assertTrue(basics.filterAndTransform(List.of(), 3).isEmpty());
    }

    @Test
    void filterAndTransform_noneMatch_returnsEmpty() {
        List<String> result = basics.filterAndTransform(List.of("a", "bb"), 5);
        assertTrue(result.isEmpty());
    }
}
