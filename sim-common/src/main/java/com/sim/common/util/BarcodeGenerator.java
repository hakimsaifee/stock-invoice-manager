package com.sim.common.util;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeGenerator {


  public static String createBarCode128(String filePath, String code) {
    try {
    	
    	
      Code128Bean bean = new Code128Bean();
      final int dpi = 160;

      //Configure the barcode generator
      bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

      bean.doQuietZone(false);

      //Open output file
      File outputFile = new File(filePath + code + ".JPG");

      FileOutputStream out = new FileOutputStream(outputFile);
    
      BitmapCanvasProvider canvas = new BitmapCanvasProvider(
          out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

      //Generate the barcode
      bean.generateBarcode(canvas, code);
   
      //Signal end of generation
      canvas.finish();
    
      System.out.println("Bar Code is generated successfully…");
      return outputFile.getAbsolutePath();
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }



 

  public static void createBarCode39(String filePath, String code) {

        try {
          Code39Bean bean39 = new Code39Bean();
          final int dpi = 160;

          //Configure the barcode generator
          bean39.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

          bean39.doQuietZone(false);

          //Open output file
          File outputFile = new File(filePath + code + ".JPG");
        
          FileOutputStream out = new FileOutputStream(outputFile);
        

          //Set up the canvas provider for monochrome PNG output
          BitmapCanvasProvider canvas = new BitmapCanvasProvider(
              out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

          //Generate the barcode
          bean39.generateBarcode(canvas, code);
       
          //Signal end of generation
          canvas.finish();
        
          System.out.println("Bar Code is generated successfully…");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
      }

}