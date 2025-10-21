package org.howard.edu.lsp.assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler {
    
        private final Path inputPath;
        private final Path outputPath;
    
        /**
         * Construct a CsvHandler for given input and output paths.
         *
         * @param inputPath  path to input CSV (data/products.csv)
         * @param outputPath path to output CSV (data/transformed_products.csv)
         */
        public CsvHandler(Path inputPath, Path outputPath) {
            this.inputPath = inputPath;
            this.outputPath = outputPath;
        }
    
        /**
         * Read all CSV rows from the input file and return as a list of String arrays.
         * Each element of the list corresponds to one line split by ',' using split(",", -1)
         * to preserve trailing empty fields. IOException is propagated to caller.
         *
         * @return list of rows, each row as String[]
         * @throws IOException if reading file fails
         */
        public List<String[]> readAll() throws IOException {
            List<String[]> rows = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(inputPath)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] cols = line.split(",", -1);
                    rows.add(cols);
                }
            }
            return rows;
        }
    
        /**
         * Write header and data lines to the output CSV file.
         * Recreates parent directories if necessary.
         *
         * @param header header line (single comma-separated string)
         * @param data   list of lines already formatted as comma-separated strings
         * @throws IOException if writing fails
         */
        public void writeAll(String header, List<String> data) throws IOException {
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(outputPath)) {
                if (header != null && !header.isEmpty()) {
                    bw.write(header);
                    bw.newLine();
                }
                for (String line : data) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
    
        /**
         * Helper to parse a CSV row into a Product. Behaves like original A2 parsing:
         * expects 4 columns in order: id, name, price, category.
         * Returns null if the row is malformed (to signify a skipped row).
         *
         * @param cols row tokens
         * @return Product instance or null if malformed
         */
        public Product parseRowToProduct(String[] cols) {
            if (cols == null || cols.length != 4) return null;
            String idStr = cols[0].trim();
            String name = cols[1].trim();
            String priceStr = cols[2].trim();
            String category = cols[3].trim();
    
            if (idStr.isEmpty() || name.isEmpty() || priceStr.isEmpty()) return null;
            int id;
            BigDecimal price;
            try {
                id = Integer.parseInt(idStr);
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                return null;
            }
            if (price.compareTo(BigDecimal.ZERO) < 0) return null;
            return new Product(id, name, price, category);
        }
    }
    
