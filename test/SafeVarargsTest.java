import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the difference in varargs warnings between -source 1.6 and -source 1.7.
 *
 * With -source 1.6:
 *   3 warnings — one per call site (unchecked generic array creation).
 *   No warning at the declaration site.
 *
 * With -source 1.7:
 *   4 warnings — the same 3 call-site warnings PLUS a new declaration-site
 *   warning: "Possible heap pollution from parameterized vararg type List<T>".
 *   Adding @SafeVarargs to combine() would suppress all 4.
 *
 * To see the difference:
 *   javac -source 1.6 -Xlint:unchecked SafeVarargsTest.java
 *   javac -source 1.7 -Xlint:unchecked SafeVarargsTest.java
 */
public class SafeVarargsTest
{
  // Generic varargs: non-reifiable element type triggers warnings.
  static <T> List<T> combine(List<T>... lists)
  {
    return lists.length > 0 ? lists[0] : null;
  }

  public static void main(String[] args)
  {
    List<String>  a = Arrays.asList("foo", "bar");
    List<String>  b = Arrays.asList("baz");
    List<Integer> c = Arrays.asList(1, 2, 3);

    combine(a, b);        // call site 1 — warning in both 1.6 and 1.7
    combine(b);           // call site 2 — warning in both 1.6 and 1.7
    combine(a, b, a);     // call site 3 — warning in both 1.6 and 1.7
  }
}
