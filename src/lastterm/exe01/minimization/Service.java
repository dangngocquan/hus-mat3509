package lastterm.exe01.minimization;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Service {
    public static String matrixToTable(Object[][] matrix) {
        int w = 0; // width per column
        for (Object[] objects: matrix)
            for (Object object: objects)
                w = Math.max(w, object.toString().length());
        w += 2;

        StringBuilder sb = new StringBuilder();
        sb.append(lineRowPattern1(matrix[0].length, w)).append("\n");

        for (int i = 0; i < matrix.length; i++) {
            sb.append(contentRowPattern(matrix[i], w, matrix[i].length)).append("\n");
            if (i == matrix.length - 1) {
                sb.append(lineRowPattern3(matrix[i].length, w));
            } else {
                sb.append(lineRowPattern2(matrix[i].length, w)).append("\n");
            }
        }

        return sb.toString();
    }

    public static String matrixToTablePattern2(Object[][] matrix) {
        if (matrix.length < 2) return "";

        int w = 0; // width per column
        for (Object[] objects: matrix)
            for (Object object: objects)
                w = Math.max(w, object.toString().length());
        w += 2;

        StringBuilder sb = new StringBuilder();
        int numberColumns = 2;
        sb.append(lineRowPattern1(numberColumns, w)).append("\n");
        sb.append(contentRowPattern(matrix[1], w, numberColumns)).append("\n");
        numberColumns++;

        for (int i = 2; i < matrix.length; i++) {
            sb.append(lineRowPattern4(numberColumns, w)).append("\n");
            sb.append(contentRowPattern(matrix[i], w, numberColumns++)).append("\n");
        }

        sb.append(lineRowPattern3(--numberColumns, w));

        return sb.toString();
    }

    public static String lineRowPattern1(int numberColumns, int widthPerColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("┌");
        for (int i = 0; i < numberColumns - 1; i++) {
            sb.append("─".repeat(widthPerColumn)).append("┬");
        }
        sb.append("─".repeat(widthPerColumn)).append("┐");
        return sb.toString();
    }

    public static String lineRowPattern2(int numberColumns, int widthPerColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("├");
        for (int i = 0; i < numberColumns - 1; i++) {
            sb.append("─".repeat(widthPerColumn)).append("┼");
        }
        sb.append("─".repeat(widthPerColumn)).append("┤");
        return sb.toString();
    }

    public static String lineRowPattern3(int numberColumns, int widthPerColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("└");
        for (int i = 0; i < numberColumns - 1; i++) {
            sb.append("─".repeat(widthPerColumn)).append("┴");
        }
        sb.append("─".repeat(widthPerColumn)).append("┘");
        return sb.toString();
    }

    public static String lineRowPattern4(int numberColumns, int widthPerColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("├");
        for (int i = 0; i < numberColumns - 1; i++) {
            sb.append("─".repeat(widthPerColumn)).append("┼");
        }
        sb.append("─".repeat(widthPerColumn)).append("┐");
        return sb.toString();
    }

    public static String contentRowPattern(Object[] row, int widthPerColumn, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("│");
        for (int i = 0; i < limit; i++) {
            sb.append(String.format(
                        String.format(" %%-%ds", widthPerColumn-1),
                        row[i].toString()
                    ))
                    .append("│");
        }
        return sb.toString();
    }

    public static String checkOrCreatePath(String path, String fileName) {
        // Create folder if not exist
        String pathDir = System.getProperty("user.dir") + path;
        File dir = new File(pathDir);
        if (!dir.exists()) {
            boolean wasSuccessful = dir.mkdirs();
            if (!wasSuccessful) {
                System.out.println("Create folder was not successful.");
            }
        }

        // Create file if not exist
        String pathFile = pathDir + "/" + fileName;
        File file = new File(pathFile);
        if (!file.exists()) {
            try {
                boolean wasSuccessful = file.createNewFile();
                if (!wasSuccessful) {
                    System.out.println("Create file was not successful.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return pathFile;
    }

    public static void writeFile(String contents) {
        // Check or create file if not exist
        String pathFile = checkOrCreatePath("/src/lastterm/exe01/minimization/output", Config.fileNameOutput);

        // Append content to file
        try {
            FileOutputStream f = new FileOutputStream(pathFile , true);
            OutputStreamWriter writer = new OutputStreamWriter(f, StandardCharsets.UTF_8);
            writer.write(contents);
            writer.flush();
            writer.close();
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] readFile() {
        // Check or create file if not exist
        String pathFile = checkOrCreatePath("/src/lastterm/exe01/minimization/input", Config.fileNameInput);

        // Get contents of file
        List<String> lines = new LinkedList<>();
        try {
            FileInputStream f = new FileInputStream(pathFile);
            InputStreamReader inputStreamReader = new InputStreamReader(f, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            // Close
            reader.close();
            inputStreamReader.close();
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] contents = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) contents[i] = lines.get(i);
        return contents;
    }

}
