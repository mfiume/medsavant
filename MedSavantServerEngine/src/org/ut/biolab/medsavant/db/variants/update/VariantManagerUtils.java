/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.db.variants.update;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.ut.biolab.medsavant.format.CustomField;
import org.ut.biolab.medsavant.db.connection.ConnectionController;
import org.ut.biolab.medsavant.db.util.query.VariantQueryUtil;
import org.ut.biolab.medsavant.server.log.ServerLogger;
import org.ut.biolab.medsavant.vcf.VCFHeader;
import org.ut.biolab.medsavant.vcf.VCFParser;
import org.ut.biolab.medsavant.vcf.VariantRecord;

/**
 *
 * @author Andrew
 */
public class VariantManagerUtils {
    
    private static final int outputLinesLimit = 1000000;
    //private static final int MAX_SUBSET_SIZE = 100000000; //bytes = 100MB
    private static final int MIN_SUBSET_SIZE = 100000000; //bytes = 100MB
    private static final int SUBSET_COMPRESSION = 1000; // times
    
    public static void annotateTDF(String sid, String tdfFilename, String outputFilename, int[] annotationIds) throws Exception {
        (new VariantAnnotator(tdfFilename, outputFilename, annotationIds)).annotate(sid);
    }

    public static void appendToFile(String baseFilename, String appendingFilename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(baseFilename, true));
        BufferedReader reader = new BufferedReader(new FileReader(appendingFilename));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.write("\r\n");
        }
        writer.close();
        reader.close();
    }

    public static void variantsToFile(String sid, String tableName, File file, String conditions, boolean complete, int step) throws SQLException {
        String query;
        
        if(complete) {
            query = "SELECT *";
        } else {
            query = "SELECT `upload_id`, `file_id`, `variant_id`, `dna_id`, `chrom`, `position`,"
                + " `dbsnp_id`, `ref`, `alt`, `qual`, `filter`, `variant_type`, `zygosity`, `custom_info`";
        }
              
        query +=
                " INTO OUTFILE \"" + file.getAbsolutePath().replaceAll("\\\\", "/") + "\""
                + " FIELDS TERMINATED BY ',' ENCLOSED BY '\"'"
                + " LINES TERMINATED BY '\\r\\n'"
                + " FROM " + tableName;
        
        if(step > 1){
            if(conditions != null && conditions.length()> 1){
                conditions += " AND `variant_id`%" + step + "=0";
            } else {
                conditions = "`variant_id`%" + step + "=0";
            }
        }

        if(conditions != null && conditions.length()> 1){
            query += " WHERE " + conditions;
        }  
        
        System.err.println(query);

        ConnectionController.executeQuery(sid, query);
    }

    public static void removeTemp(String filename) {
        removeTemp(new File(filename));
    }

    public static void removeTemp(File temp) {
        if (!temp.delete()) {
            temp.deleteOnExit();
        }
    }

    public static void dropTableIfExists(String sid, String tableName) throws SQLException {
        ConnectionController.execute(sid, "DROP TABLE IF EXISTS " + tableName + ";");
    }

    public static void splitFileOnColumn(File splitDir, String outputFilename, int i) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(outputFilename));

        String line = "";

        Map<String, BufferedWriter> outputFileMap = new HashMap<String, BufferedWriter>();

        BufferedWriter out;

        while ((line = br.readLine()) != null) {
            String[] parsedLine = line.split(",");

            String fileId = parsedLine[i];

            if (!outputFileMap.containsKey(fileId)) {
                outputFileMap.put(fileId, new BufferedWriter(new FileWriter(new File(splitDir, fileId))));
            }
            out = outputFileMap.get(fileId);

            out.write(line + "\n");
        }

        for (BufferedWriter bw : outputFileMap.values()) {
            bw.flush();
            bw.close();
        }
    }

    // might not be the most efficient
    public static void concatenateFilesInDir(File fromDir, String outputPath) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));

        String line;
        for (File inFile : fromDir.listFiles()) {

            if (inFile.getName().startsWith(".")) { continue; }

            line = "";

            ServerLogger.log(VariantManager.class, "Merging " + inFile.getAbsolutePath() + " with to the result file " + (new File(outputPath)).getAbsolutePath());
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
            }
        }

        bw.flush();
        bw.close();

    }

    public static void sortFileByPosition(String inFile, String outfile) throws IOException, InterruptedException {
        String sortCommand = "sort -t , -k 5,5 -k 6,6n -k 7 " + ((new File(inFile)).getAbsolutePath());

        ServerLogger.log(VariantManager.class, "Sorting file: " + ((new File(inFile)).getAbsolutePath()));

        if (!(new File(inFile)).exists()) {
            throw new IOException("File not found " + ((new File(inFile)).getAbsolutePath()));
        }

        Process p = Runtime.getRuntime().exec(sortCommand);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        ServerLogger.log(VariantManager.class, "Writing results to file: " + ((new File(outfile)).getAbsolutePath()));

        BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
        String s = null;
        // read the output from the command
        while ((s = stdInput.readLine()) != null) {
            bw.write(s + "\n");
        }

        stdInput.close();
        bw.close();

        if (!(new File(outfile)).exists()) {
            throw new IOException("Problem sorting file; no output");
        }

        ServerLogger.log(VariantManager.class, "Done sorting");

    }

    public static void logFileSize(String fn) {
        ServerLogger.log(VariantManager.class, "Size of " + fn + ": " + ((new File(fn)).length()));
    }

    public static void addCustomVcfFields(String infile, String outfile, List<CustomField> customFields, int customInfoIndex) throws FileNotFoundException, IOException {

        String[] infoFields = new String[customFields.size()];
        Class[] infoClasses = new Class[customFields.size()];
        for(int i = 0; i < customFields.size(); i++){
            infoFields[i] = customFields.get(i).getColumnName();
            infoClasses[i] = customFields.get(i).getColumnClass();
        }

        BufferedReader reader = new BufferedReader(new FileReader(infile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
        String line = null;
        while((line = reader.readLine()) != null){
            String[] split = line.split(",");
            String info = split[customInfoIndex].substring(1, split[customInfoIndex].length()-1); //remove quotes
            writer.write(line + "," + VariantRecord.createTabString(VariantRecord.parseInfo(info, infoFields, infoClasses)) + "\n");
        }
        reader.close();
        writer.close();
    }
    
    public static void addTagsToUpload(String sid, int uploadID, String[][] variantTags) throws SQLException, RemoteException {
        try {
            VariantQueryUtil.getInstance().addTagsToUpload(sid, uploadID, variantTags);
        } catch (SQLException e) {
            throw new SQLException("Error adding tags", e);
        }
    }

    public static void checkInterrupt() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
        
    /*
     * Given vcf files, parse out the relevant information and concatenate to 
     * create a single, ready-to-use csv. 
     */
    public static File parseVCFs(File[] vcfFiles, File tmpDir, int updateId, boolean includeHomoRef) throws IOException, InterruptedException, ParseException {
        
        boolean variantFound = false;
        File outfile = new File(tmpDir, "1_tmp.tdf");

        //add files to staging table
        for (int i = 0; i < vcfFiles.length; i++) {
      
            //create temp file
            VariantManagerUtils.checkInterrupt();
            int lastChunkWritten = 0;
            int iteration = 0;

            //create csv reader
            Reader reader;
            if (vcfFiles[i].getAbsolutePath().endsWith(".gz") || vcfFiles[i].getAbsolutePath().endsWith(".zip")) {
                FileInputStream fin = new FileInputStream(vcfFiles[i].getAbsolutePath());
                reader = new InputStreamReader(new GZIPInputStream(fin));
            } else {
                reader = new FileReader(vcfFiles[i]);
            }
            CSVReader r = new CSVReader(reader, VCFParser.defaultDelimiter);
            
            //CSVReader r = new CSVReader(new FileReader(vcfFiles[i]), VCFParser.defaultDelimiter);
            if (r == null) {
                throw new FileNotFoundException();
            }

            //get header
            VCFHeader header = VCFParser.parseVCFHeader(r);

            while (iteration == 0 || lastChunkWritten >= outputLinesLimit) {

                iteration++;

                //parse vcf file
                VariantManagerUtils.checkInterrupt();
                System.out.println("Current file: " + vcfFiles[i].getName());
                lastChunkWritten = VCFParser.parseVariantsFromReader(r, header, outputLinesLimit, outfile, updateId, i, includeHomoRef);
                if (lastChunkWritten > 0) {
                    variantFound = true;
                }
            }

            try {
                r.close();
            } catch (IOException ex) {}

            //cleanup
            //outfile.delete();
            System.gc();
        }

        //make sure files weren't all empty
        if (!variantFound) {
            throw new ParseException("No variants were found. Ensure that your files are in the correct format. ", 0);
        }
        
        return outfile;
    }
    
    public static int determineStepForSubset(long length) {
        int step;
        if(length <= MIN_SUBSET_SIZE){
            step = 1;
        } else {
            long targetSize = Math.max(MIN_SUBSET_SIZE, length / 1000);
            step = (int)Math.ceil((double)length / (double)targetSize);
        }
        return step;
    }
    
    public static void generateSubset(File inFile, File outFile) throws IOException, InterruptedException{
    
        System.out.println("generate subset");
        long length = inFile.length();
        int step;
        if(length <= MIN_SUBSET_SIZE){
            step = 1;
        } else {
            long targetSize = Math.max(MIN_SUBSET_SIZE, length / 1000);
            step = (int)Math.ceil((double)length / (double)targetSize);
        }
        
        
        
        /*double ratio = (double)MAX_SUBSET_SIZE / length;
        int step;
 
        if(ratio >= 1){ // file is smaller than max size, copy directly
            step = 1;
        } else {
            step = (int)Math.ceil(1.0 / ratio);
        }*/

        System.out.println("length: " + length + "  step: " + step);
        
        /*String sedCommand = "sed -n '0~" + step + "p' " + inFile.getAbsolutePath() + " > " + outFile.getAbsolutePath();
        System.out.println(sedCommand);

        Process p = Runtime.getRuntime().exec(sedCommand);
        p.waitFor();
         */
        
        BufferedReader in = new BufferedReader(new FileReader(inFile));
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        
        int i = 1;
        String line;
        while((line = in.readLine()) != null){
            if(i >= step){
                out.write(line + "\n");
                i = 1;
            } else {
                i++;
            }
        }
        
        in.close();
        out.close();
    }
    
    
}
