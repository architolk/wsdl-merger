package nl.architolk.wsdlreader;

import com.ibm.wsdl.extensions.schema.SchemaImpl;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.List;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Types;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import org.w3c.dom.Element;

public class Generator {

  private static final String DWS_NAMESPACE = "http://dotwebstack.org/wsdl-extension/";
  private static final String DWS_INFOPROD = "informationProduct";

  public static void main(String[] args) {
		
    try {
      // Read the wsdl
      URL wsdlURL = new File("test-wsdl.xml").toURI().toURL();
      WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
      //uncomment the line below, if you don't want any verbose output
      wsdlReader.setFeature("javax.wsdl.verbose", false);
      //wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_PARSE_SCHEMA, true);
      Definition wsdlDefinition = wsdlReader.readWSDL(wsdlURL.toString());
      
      //Output flatten wsdl
      //WSDLWriter wsdlWriter = WSDLFactory.newInstance().newWSDLWriter();
      WSDLWriter wsdlWriter = new WSDLFlattenWriter();
      //wsdlWriter.setFeature(com.ibm.wsdl.Constants.FEATURE_PARSE_SCHEMA,true);
      wsdlWriter.writeWSDL(wsdlDefinition, System.out);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
}