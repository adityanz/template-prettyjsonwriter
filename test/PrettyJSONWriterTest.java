import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class PrettyJSONWriterTest {

	public static final Path ACTUAL_DIR = Paths.get("out");
	public static final Path EXPECTED_DIR = Paths.get("test");

	@BeforeAll
	public static void setupEnvironment() throws IOException {
		Files.createDirectories(ACTUAL_DIR);
	}

	public static void compareFiles(Path actualPath, Path expectPath) throws IOException {
		List<String> actual = Files.readAllLines(actualPath, StandardCharsets.UTF_8);
		List<String> expect = Files.readAllLines(expectPath, StandardCharsets.UTF_8);

		String debug = String.format("%nCompare %s and %s for differences.%n", actualPath, expectPath);
		assertEquals(expect, actual, () -> debug);
	}

	@Nested
	public class ArrayTests {

		public void runTest(TreeSet<Integer> elements, String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			PrettyJSONWriter.asArray(elements, actualPath);
			compareFiles(actualPath, expectPath);
		}

		@Test
		public void testEmptySet() throws IOException {
			String name = "json-array-empty.txt";
			runTest(new TreeSet<Integer>(), name);
		}

		@Test
		public void testSingleSet() throws IOException {
			String name = "json-array-single.txt";

			TreeSet<Integer> elements = new TreeSet<>();
			elements.add(42);

			runTest(elements, name);
		}

		@Test
		public void testSimple() throws IOException {
			String name = "json-array-simple.txt";

			TreeSet<Integer> elements = new TreeSet<>();
			Collections.addAll(elements, 1, 2, 3, 4);

			runTest(elements, name);
		}
	}

	@Nested
	public class ObjectTests {

		public void runTest(TreeMap<String, Integer> elements, String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			PrettyJSONWriter.asObject(elements, actualPath);
			compareFiles(actualPath, expectPath);
		}

		@Test
		public void testEmptyMap() throws IOException {
			String name = "json-object-empty.txt";
			runTest(new TreeMap<String, Integer>(), name);
		}

		@Test
		public void testSinglePair() throws IOException {
			String name = "json-object-single.txt";

			TreeMap<String, Integer> elements = new TreeMap<>();
			elements.put("The Answer", 42);

			runTest(elements, name);
		}

		@Test
		public void testSimpleMap() throws IOException {
			String name = "json-object-simple.txt";

			TreeMap<String, Integer> elements = new TreeMap<>();
			elements.put("a", 4);
			elements.put("b", 3);
			elements.put("c", 2);
			elements.put("d", 1);

			runTest(elements, name);
		}
	}

	@Nested
	public class NestedObjectTests {

		public void runTest(TreeMap<String, TreeSet<Integer>> elements, String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			PrettyJSONWriter.asNestedObject(elements, actualPath);
			compareFiles(actualPath, expectPath);
		}

		@Test
		public void testEmptyMap() throws IOException {
			String name = "json-object-empty.txt";
			runTest(new TreeMap<String, TreeSet<Integer>>(), name);
		}

		@Test
		public void testSingleEntry() throws IOException {
			String name = "json-nested-single.txt";

			TreeMap<String, TreeSet<Integer>> elements = new TreeMap<>();
			elements.put("The Answer", new TreeSet<>());
			elements.get("The Answer").add(42);

			runTest(elements, name);
		}

		@Test
		public void testSimpleTree() throws IOException {
			String name = "json-nested-simple.txt";

			TreeMap<String, TreeSet<Integer>> elements = new TreeMap<>();
			elements.put("a", new TreeSet<>());
			elements.put("b", new TreeSet<>());
			elements.put("c", new TreeSet<>());

			elements.get("a").add(1);
			elements.get("b").add(2);
			elements.get("b").add(3);
			elements.get("b").add(4);

			runTest(elements, name);
		}
	}
}
