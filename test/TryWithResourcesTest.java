/**
 * Test program for Java 7 try-with-resources (ARM - Automatic Resource Management).
 * Tests: single resource, multiple resources, suppressed exceptions, and real I/O.
 */
public class TryWithResourcesTest {

    static int passed = 0;
    static int failed = 0;

    // --- Custom AutoCloseable resources ---

    static class Resource implements AutoCloseable {
        final String name;
        boolean closed = false;

        Resource(String name) {
            this.name = name;
            log("  opened: " + name);
        }

        @Override
        public void close() {
            closed = true;
            log("  closed: " + name);
        }
    }

    /** A resource whose close() throws. */
    static class BrokenResource implements AutoCloseable {
        final String name;

        BrokenResource(String name) {
            this.name = name;
            log("  opened: " + name);
        }

        @Override
        public void close() throws Exception {
            log("  close() throws: " + name);
            throw new Exception("close failed: " + name);
        }
    }

    // --- Test helpers ---

    static void log(String msg) { System.out.println(msg); }

    static void pass(String test) {
        System.out.println("PASS: " + test);
        passed++;
    }

    static void fail(String test, String reason) {
        System.out.println("FAIL: " + test + " -- " + reason);
        failed++;
    }

    // --- Tests ---

    /** Basic: resource is closed after normal exit. */
    static void testBasic() {
        log("[testBasic]");
        Resource r;
        try (Resource res = new Resource("A")) {
            r = res;
        }
        if (r.closed)
            pass("testBasic");
        else
            fail("testBasic", "resource not closed");
    }

    /** Multiple resources are closed in reverse order. */
    static void testMultipleResources() {
        log("[testMultipleResources]");
        java.util.List<String> order = new java.util.ArrayList<String>();

        class Tracked implements AutoCloseable {
            final String name;
            Tracked(String n) { name = n; log("  opened: " + n); }
            public void close() { log("  closed: " + name); order.add(name); }
        }

        try (Tracked a = new Tracked("first");
             Tracked b = new Tracked("second");
             Tracked c = new Tracked("third")) {
            log("  inside try block");
        }

        // Expect reverse order: third, second, first
        if (order.size() == 3
                && order.get(0).equals("third")
                && order.get(1).equals("second")
                && order.get(2).equals("first"))
            pass("testMultipleResources");
        else
            fail("testMultipleResources", "close order was: " + order);
    }

    /** Resource is closed even when an exception is thrown in the body. */
    static void testClosedOnException() {
        log("[testClosedOnException]");
        Resource r = null;
        try {
            try (Resource res = new Resource("B")) {
                r = res;
                throw new RuntimeException("body exception");
            }
        } catch (RuntimeException e) {
            log("  caught: " + e.getMessage());
        }
        if (r != null && r.closed)
            pass("testClosedOnException");
        else
            fail("testClosedOnException", "resource not closed on exception");
    }

    /** When both body and close() throw, body exception is primary; close() exception is suppressed. */
    static void testSuppressedException() {
        log("[testSuppressedException]");
        try {
            try (BrokenResource res = new BrokenResource("C")) {
                throw new RuntimeException("primary");
            }
        } catch (RuntimeException e) {
            Throwable[] suppressed = e.getSuppressed();
            if ("primary".equals(e.getMessage())
                    && suppressed.length == 1
                    && suppressed[0].getMessage().equals("close failed: C"))
                pass("testSuppressedException");
            else
                fail("testSuppressedException",
                     "primary=" + e.getMessage() + " suppressed=" + suppressed.length);
        } catch (Exception e) {
            fail("testSuppressedException", "unexpected exception type: " + e);
        }
    }

    /** When only close() throws (no body exception), it propagates normally. */
    static void testCloseExceptionPropagates() {
        log("[testCloseExceptionPropagates]");
        try {
            try (BrokenResource res = new BrokenResource("D")) {
                log("  body ok");
            }
            fail("testCloseExceptionPropagates", "expected exception not thrown");
        } catch (Exception e) {
            if (e.getMessage().equals("close failed: D")
                    && e.getSuppressed().length == 0)
                pass("testCloseExceptionPropagates");
            else
                fail("testCloseExceptionPropagates", "unexpected: " + e);
        }
    }

    /** Real I/O: read a file using try-with-resources. */
    static void testRealIO() {
        log("[testRealIO]");
        java.io.File tmp = null;
        try {
            tmp = java.io.File.createTempFile("twr_test_", ".txt");
            // Write something
            try (java.io.FileWriter fw = new java.io.FileWriter(tmp)) {
                fw.write("hello try-with-resources");
            }
            // Read it back
            StringBuilder sb = new StringBuilder();
            try (java.io.BufferedReader br =
                     new java.io.BufferedReader(new java.io.FileReader(tmp))) {
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line);
            }
            if ("hello try-with-resources".equals(sb.toString()))
                pass("testRealIO");
            else
                fail("testRealIO", "unexpected content: " + sb);
        } catch (Exception e) {
            fail("testRealIO", e.toString());
        } finally {
            if (tmp != null) tmp.delete();
        }
    }

    /** Null resource is allowed (spec: if initializer is null, close is not called). */
    static void testNullResource() {
        log("[testNullResource]");
        try {
            try (Resource res = null) {
                log("  body executed with null resource");
            }
            pass("testNullResource");
        } catch (NullPointerException e) {
            fail("testNullResource", "NPE thrown for null resource");
        } catch (Exception e) {
            fail("testNullResource", "unexpected: " + e);
        }
    }

    // --- Main ---

    public static void main(String[] args) {
        testBasic();
        testMultipleResources();
        testClosedOnException();
        testSuppressedException();
        testCloseExceptionPropagates();
        testRealIO();
        testNullResource();

        System.out.println();
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) System.exit(1);
    }
}
