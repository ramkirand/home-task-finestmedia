package util;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import annotation.TrackExecutionTime;
import model.EnergyReport;

public class SaxXmlParser {

  @TrackExecutionTime
  public static EnergyReport parser(String xmlString)
      throws ParserConfigurationException, SAXException, IOException {

    initialSetUp(xmlString);
    return SaxXmlDefaultHandler.getEnergyReport();

  }

  private static void initialSetUp(String xmlString)
      throws ParserConfigurationException, SAXException, IOException {
    SaxXmlDefaultHandler datahandler = new SaxXmlDefaultHandler();
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      saxParser.parse(new InputSource(new StringReader(xmlString)), datahandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
