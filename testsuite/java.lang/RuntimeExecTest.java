import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* Basic Runtime.exec/ProcessBuilder spawn tests. */
public class RuntimeExecTest
{
  private static String readAll(InputStream in) throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[256];
    int count;

    while ((count = in.read(buffer)) >= 0)
      out.write(buffer, 0, count);

    return out.toString("UTF-8");
  }

  private static void testRuntimeExec() throws Exception
  {
    Process process = Runtime.getRuntime().exec(new String[]
      { "/bin/sh", "-c", "echo out; echo err 1>&2" });

    String stdout = readAll(process.getInputStream());
    String stderr = readAll(process.getErrorStream());
    int exit = process.waitFor();

    if (exit != 0)
      {
  System.out.println("FAILED: Runtime.exec exit code " + exit);
  return;
      }

    if (stdout.indexOf("out") < 0)
      {
  System.out.println("FAILED: Runtime.exec missing stdout");
  return;
      }

    if (stderr.indexOf("err") < 0)
      {
  System.out.println("FAILED: Runtime.exec missing stderr");
  return;
      }

    System.out.println("PASSED: Runtime.exec captured stdout/stderr");
  }

  private static void testProcessBuilderRedirect() throws Exception
  {
    ProcessBuilder builder = new ProcessBuilder(
      new String[] { "/bin/sh", "-c", "echo out; echo err 1>&2" });
    builder.redirectErrorStream(true);

    Process process = builder.start();
    String output = readAll(process.getInputStream());
    int exit = process.waitFor();

    if (exit != 0)
      {
  System.out.println("FAILED: ProcessBuilder exit code " + exit);
  return;
      }

    if (output.indexOf("out") < 0 || output.indexOf("err") < 0)
      {
  System.out.println("FAILED: ProcessBuilder redirect missing output");
  return;
      }

    System.out.println("PASSED: ProcessBuilder redirect merged stderr");
  }

  public static void main(String[] args)
  {
    try
      {
  if (!new File("/bin/sh").exists())
    {
      System.out.println("PASSED: Runtime.exec skipped (no /bin/sh)");
      return;
    }

  testRuntimeExec();
  testProcessBuilderRedirect();
      }
    catch (Throwable t)
      {
  System.out.println("FAILED: " + t);
      }
  }
}
