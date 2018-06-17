package nl.architolk.wsdlreader;

import com.ibm.wsdl.xml.WSDLWriterImpl;
import javax.wsdl.Types;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import com.ibm.wsdl.extensions.schema.SchemaImpl;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Element;
import java.io.PrintWriter;

public class WSDLFlattenWriter extends WSDLWriterImpl {
  
  @Override
  protected void printTypes(Types types, Definition def, PrintWriter pw) throws WSDLException{
    
    pw.println("<wsdl:types>");
    List<ExtensibilityElement> elements = types.getExtensibilityElements();
    for (ExtensibilityElement element : elements) {
      if ( element instanceof SchemaImpl ) {
        Map<String, List> includes = ((SchemaImpl) element).getImports();
        for (List<SchemaImport> includeList : includes.values()) {
          for (SchemaImport schemaImport : includeList) {
            Schema schema = schemaImport.getReferencedSchema();
            printSchema(def, schema, pw);
          }
        }
        printSchema(def, (Schema) element, pw);
      } 
    }
    pw.println("</wsdl:types>");
    //super.printTypes(types, def, pw);
  }
  
  private static void printSchema(Definition definition, Schema schema, PrintWriter pw) throws WSDLException {
    
    Element schemaElement = schema.getElement(); 
    Map<String, String> namespaces = definition.getNamespaces(); 
    for( Entry<String, String> entry : namespaces.entrySet() ) { 
      if ( entry.getKey().equals( "xmlns" ) || entry.getKey().trim().isEmpty() ) { 
        continue; 
      } 
      if ( schemaElement.getAttribute( "xmlns:" + entry.getKey() ).isEmpty() ) { 
        schemaElement.setAttribute( "xmlns:" + entry.getKey(), entry.getValue() ); 
      } 
    }
    printElement(schemaElement, pw);
  }

  private static void printElement(Element element, PrintWriter pw) throws WSDLException {
    
    try {
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(element), new StreamResult(pw));
    } catch (Exception e) {
      throw new WSDLException(WSDLException.PARSER_ERROR,"Error writing types", e);
    }
  }
  
}