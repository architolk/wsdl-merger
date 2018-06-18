@echo off
java -cp "target/WSDLMerger-0.0.1.jar;target/lib/*" nl.architolk.wsdlmerger.Merger input-wsdl.xml output-wsdl.xml
pause
