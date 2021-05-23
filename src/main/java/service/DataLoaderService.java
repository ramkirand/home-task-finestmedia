package service;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public interface DataLoaderService {
  String loadSeedData(String url) throws ParserConfigurationException, SAXException, IOException;
}
