package org.howard.edu.lsp.assignment2;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.util.*;

public class ETLPipeline {

    private static int rowsRead = 0, rowsTransformed = 0, rowsSkipped = 0;

    private static class Product {
        int id; String name, category, originalCategory, priceRange; BigDecimal price;
        Product(int id, String name, BigDecimal price, String category) {
            this.id=id; this.name=name; this.price=price; this.category=category; this.originalCategory=category;
        }
    }

    public static void main(String[] args) {
        Path in = Paths.get("data","products.csv"), out = Paths.get("data","transformed_products.csv");
        System.out.println("Working dir: "+Paths.get("").toAbsolutePath());
        List<Product> products = extract(in);
        List<Product> transformed = transform(products);
        load(transformed, out);
        System.out.println("\nRUN SUMMARY");
        System.out.println("Rows read       : "+rowsRead);
        System.out.println("Rows transformed: "+rowsTransformed);
        System.out.println("Rows skipped    : "+rowsSkipped);
        System.out.println("Output written to (absolute path): "+out.toAbsolutePath());
    }

    private static List<Product> extract(Path inputPath){
        List<Product> products=new ArrayList<>();
        if(!Files.exists(inputPath)){System.err.println("ERROR: Input file not found: "+inputPath); System.exit(1);}
        try(BufferedReader br=Files.newBufferedReader(inputPath)){
            String header=br.readLine();
            if(header==null) return products;
            String line;
            while((line=br.readLine())!=null && !line.trim().isEmpty()){
                String[] t=line.split(",",-1);
                if(t.length!=4){rowsSkipped++; continue;}
                try{products.add(new Product(Integer.parseInt(t[0].trim()), t[1].trim(), new BigDecimal(t[2].trim()), t[3].trim())); rowsRead++;} 
                catch(Exception e){rowsSkipped++;}
            }
        } catch(IOException e){System.err.println("Error reading file: "+e.getMessage()); System.exit(1);}
        return products;
    }

    private static List<Product> transform(List<Product> products){
        List<Product> out=new ArrayList<>();
        for(Product p:products){
            p.name=p.name.toUpperCase();
            BigDecimal price=p.price;
            if(p.originalCategory.equalsIgnoreCase("Electronics")) price=price.multiply(new BigDecimal("0.9"));
            price=price.setScale(2,RoundingMode.HALF_UP);
            if(p.originalCategory.equalsIgnoreCase("Electronics") && price.compareTo(new BigDecimal("500.00"))>0) p.category="Premium Electronics";
            p.price=price; p.priceRange=computePriceRange(price);
            out.add(p); rowsTransformed++;
        }
        return out;
    }

    private static String computePriceRange(BigDecimal price){
        if(price.compareTo(BigDecimal.ZERO)>=0 && price.compareTo(new BigDecimal("10.00"))<=0) return "Low";
        if(price.compareTo(new BigDecimal("10.01"))>=0 && price.compareTo(new BigDecimal("100.00"))<=0) return "Medium";
        if(price.compareTo(new BigDecimal("100.01"))>=0 && price.compareTo(new BigDecimal("500.00"))<=0) return "High";
        if(price.compareTo(new BigDecimal("500.01"))>=0) return "Premium";
        return "Low";
    }

    private static void load(List<Product> products, Path out){
        try{
            if(out.getParent()!=null) Files.createDirectories(out.getParent());
            try(BufferedWriter w=Files.newBufferedWriter(out)){
                w.write("ProductID,Name,Price,Category,PriceRange"); w.newLine();
                for(Product p:products) w.write(String.format("%d,%s,%s,%s,%s\n",p.id,p.name,p.price.toPlainString(),p.category,p.priceRange));
            }
            System.out.println("Successfully wrote output file: "+out);
        } catch(IOException e){System.err.println("Error writing file: "+e.getMessage()); System.exit(1);}
    }
}

