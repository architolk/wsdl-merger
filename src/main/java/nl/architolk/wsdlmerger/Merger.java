package nl.architolk.wsdlmerger;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

public class Merger {

  public static void main(String[] args) {

    try {
      if (args.length != 2) {
        System.out.println("Usage: WSDLMerge <input filename> <output filename>");
      }
      else {
        System.out.println(String.format("Input file:  %s",args[0]));
        System.out.println(String.format("Output file: %s",args[1]));
        // Read the wsdl
        URL wsdlURL = new File(args[0]).toURI().toURL();
        WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
        //uncomment the line below, if you don't want any verbose output
        wsdlReader.setFeature("javax.wsdl.verbose", false);
        //wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_PARSE_SCHEMA, true);
        Definition wsdlDefinition = wsdlReader.readWSDL(wsdlURL.toString());

        //Output flatten wsdl
        //WSDLWriter wsdlWriter = WSDLFactory.newInstance().newWSDLWriter();
        WSDLWriter wsdlWriter = new WSDLFlattenWriter();
        //wsdlWriter.setFeature(com.ibm.wsdl.Constants.FEATURE_PARSE_SCHEMA,true);
        wsdlWriter.writeWSDL(wsdlDefinition, new FileOutputStream(args[1]));
      }
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
