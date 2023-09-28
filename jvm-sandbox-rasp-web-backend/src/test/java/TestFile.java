import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lue.rasp.utils.FileUtils.listMatchingDirectories;

public class TestFile {
    public static void main(String[] args) {
        String directoryPath = "/tmp";
        List<String> matchingDirectories = listMatchingDirectories(directoryPath, "yyyy-MM-dd");
        System.out.println("Matching directories in " + directoryPath + ":");
        for (String directory : matchingDirectories) {
            System.out.println(directory);
        }
    }

}
