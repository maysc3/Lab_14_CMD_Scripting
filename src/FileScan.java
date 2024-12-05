import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileScan
{
    public static void main(String[] args)
    {

        JFileChooser chooser = new JFileChooser();
        File selectedFile =null;
        String rec;

        String[] words;
        int line = 0;
        int charCnt = 0;
        int wordCnt = 0;

        if (args.length > 0) {

            selectedFile = new File(args[0]);
            if (!selectedFile.exists() || !selectedFile.canRead()) {
                System.out.println("Error: The file provided does not exist or cannot be read.");
            }
        }
        else {
            // uses a fixed known path:
            //  Path file = Paths.get("c:\\My Documents\\data.txt");

            // use the toolkit to get the current working directory of the IDE
            // Not sure if the toolkit is thread safe...
            File workingDirectory = new File(System.getProperty("user.dir"));

            // Typically, we want the user to pick the file so we use a file chooser
            // kind of ugly code to make the chooser work with NIO.
            // Because the chooser is part of Swing it should be thread safe.
            chooser.setCurrentDirectory(workingDirectory);
            // Using the chooser adds some complexity to the code.
            // we have to code the complete program within the conditional return of
            // the file chooser because the user can close it without picking a file

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();

            } else  // User closed the chooser without selecting a file
            {
                System.out.println("No file selected!!! ... exiting.\nRun the program again and select a file.");
            }
        }
        try {
            Path file = selectedFile.toPath();
            // Typical java pattern of inherited classes
            // we wrap a BufferedWriter around a lower level BufferedOutputStream
            InputStream in =
                    new BufferedInputStream(Files.newInputStream(file, CREATE));
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(in));

            // Finally we can read the file LOL!
            while (reader.ready()) {
                rec = reader.readLine();
                line++;
                charCnt += rec.length();
                words = rec.split(" ");
                wordCnt += words.length;
                // echo to screen
                System.out.printf("\nLine %4d %-60s ", line, rec);
            }
            reader.close(); // must close the file to seal it and flush buffer
            System.out.println("\n\nData file read!");

            System.out.println("The filename is " + file.getFileName());
            System.out.println("The total lines are " + line);
            System.out.println("The total words are " + wordCnt);
            System.out.println("The characters are " + charCnt);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}