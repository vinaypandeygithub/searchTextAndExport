import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {

    static List<String[]> dataLines = new ArrayList<>();
    String fileExtension = "cs";
    String CSV_FILE_NAME = "C:\\Git\\search.csv";
    String searchtext = "= $\"";
    public static void main(String[] args) throws IOException {
        dataLines.add(new String[]
                { "Path", "LineNumber", "ExtractedLine" });
        new Program().walk("C:\\aa\\bb\\cc" );
        System.out.println("csv created successfully");
    }
    public void walk( String path ) throws IOException {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if (f.isDirectory() ) {
                walk( f.getAbsolutePath() );
            }
            else {
                if(f.getName().endsWith("cs")) {
                    Path filePath = Paths.get(f.getAbsolutePath());
                    List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                    int i=1;
                    for (String line : lines) {
                        if(line.toLowerCase().contains(searchtext))
                        {
                            String extractedLine = line.substring(line.indexOf(searchtext) + searchtext.length(),line.length() -2);
                            dataLines.add(new String[]
                                    { f.getAbsolutePath(), String.valueOf(i), extractedLine });
                        }
                        i++;
                    }

                }
            }
        }
        writeCsvFile();
    }
    public void writeCsvFile() throws IOException {
        File csvOutputFile = new File(CSV_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
