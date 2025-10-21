package org.howard.edu.lsp.assignment3;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * ETLPipeline orchestrates extraction, transformation, and loading.
 * This class contains the main() entry point and retains the counters:
 * rowsRead, rowsTransformed, rowsSkipped to match Assignment 2 behavior.
 *
 * All transformation logic follows Assignment 2 exactly.
 */
public class ETLPipeline {

    private int rowsRead = 0;
    private int rowsTransformed = 0;
    private int rowsSkipped = 0;

    private final Path inputPath;
    private final Path outputPath;
    private final CsvHandler csvHandler;

    /**
     * Construct ETLPipeline with input and output paths.
     *
     * @param inputPath  input CSV path
     * @param outputPath output CSV path
     */
    public ETLPipeline(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.csvHandler = new CsvHandler(inputPath, outputPath);
    }

    /**
     * Run the pipeline. This method reads, parses, transforms, and writes the CSV.
     *
     * @throws IOException if file I/O fails
     */
    public void run() throws IOException {
        List<String[]> rows = csvHandler.readAll();

        List<String> outLines = new ArrayList<>();

        if (rows.isEmpty()) {
            // empty file -> write header only (same behavior as A2)
            csvHandler.writeAll("ProductID,Name,Price,Category,PriceRange", outLines);
            return;
        }

        boolean firstIsHeader = looksLikeHeader(rows.get(0));
        int startIndex = firstIsHeader ? 1 : 0;

        for (int i = startIndex; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            // mimic A2's stopping condition: stop on the first blank line
            boolean blank = true;
            for (String c : cols) {
                if (c != null && !c.trim().isEmpty()) { blank = false; break; }
            }
            if (blank) break;

            Product p = csvHandler.parseRowToProduct(cols);
            if (p == null) {
                rowsSkipped++;
                continue;
            }
            rowsRead++;

            // Transform exactly as in A2:
            // 1) name -> uppercase
            p.setName(p.getName().toUpperCase());

            // 2) if originalCategory == "Electronics", apply 10% discount
            BigDecimal price = p.getPrice();
            if ("Electronics".equalsIgnoreCase(p.getOriginalCategory())) {
                price = price.multiply(new BigDecimal("0.9"));
            }

            // 3) round price to 2 decimals HALF_UP
            price = price.setScale(2, RoundingMode.HALF_UP);
            p.setPrice(price);

            // 4) if originalCategory == Electronics and price > 500, set category = "Premium Electronics"
            if ("Electronics".equalsIgnoreCase(p.getOriginalCategory())
                    && price.compareTo(new BigDecimal("500.00")) > 0) {
                p.setCategory("Premium Electronics");
            }

            // 5) compute price range (same thresholds as A2)
            p.setPriceRange(computePriceRange(price));

            // 6) format output line exactly like A2
            String outLine = String.format("%d,%s,%s,%s,%s",
                    p.getId(),
                    p.getName(),
                    p.getPrice().toPlainString(),
                    p.getCategory(),
                    p.getPriceRange());

            outLines.add(outLine);
            rowsTransformed++;
        }

        // write output csv
        csvHandler.writeAll("ProductID,Name,Price,Category,PriceRange", outLines);
    }

    private boolean looksLikeHeader(String[] row) {
        if (row == null || row.length == 0) return false;
        String first = row[0].toLowerCase();
        return first.contains("id") || first.contains("name") || first.contains("product");
    }

    private String computePriceRange(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) >= 0 && price.compareTo(new BigDecimal("10.00")) <= 0) return "Low";
        if (price.compareTo(new BigDecimal("10.01")) >= 0 && price.compareTo(new BigDecimal("100.00")) <= 0) return "Medium";
        if (price.compareTo(new BigDecimal("100.01")) >= 0 && price.compareTo(new BigDecimal("500.00")) <= 0) return "High";
        if (price.compareTo(new BigDecimal("500.01")) >= 0) return "Premium";
        return "Low";
    }

    public int getRowsRead() { return rowsRead; }
    public int getRowsTransformed() { return rowsTransformed; }
    public int getRowsSkipped() { return rowsSkipped; }

    /**
     * Program entry point. Uses default relative paths:
     * input -> data/products.csv
     * output -> data/transformed_products.csv
     *
     * @param args unused
     */
    public static void main(String[] args) {
        Path in = Paths.get("data", "products.csv");
        Path out = Paths.get("data", "transformed_products.csv");
        ETLPipeline pipeline = new ETLPipeline(in, out);
        System.out.println("Working dir: " + Paths.get("").toAbsolutePath());
        try {
            pipeline.run();
            System.out.println("\nRUN SUMMARY");
            System.out.println("Rows read       : " + pipeline.getRowsRead());
            System.out.println("Rows transformed: " + pipeline.getRowsTransformed());
            System.out.println("Rows skipped    : " + pipeline.getRowsSkipped());
            System.out.println("Output written to (relative path): data/transformed_products.csv");
        } catch (Exception e) {
            System.err.println("ETL failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
