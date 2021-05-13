package util;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import constant.Constant;
import lombok.extern.slf4j.Slf4j;
import model.EnergyReport;
import model.HourConsumption;

@Slf4j
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
public class SaxXmlParserTest {

	private RestTemplate restTemplate;

	@Before
	public void init() {
		restTemplate = new RestTemplate();
	}

	@Test
	public void shoulPraseXml() {
		String xmlString = restTemplate.getForObject(Constant.URL, String.class);
		try {
			EnergyReport energyReport = SaxXmlParser.parser(xmlString);
			assertNotNull(energyReport.getDocumentDateTime());

			assertNotNull(energyReport.getDocumentIdentification());

			assertNotNull(energyReport.getAccountTimeSeries().getAccountingPoint());
			List<HourConsumption> hourConsumption = energyReport.getAccountTimeSeries().getConsumptionHistory()
					.getHourConsumption();

			for (HourConsumption hc : hourConsumption) {
				assertNotNull(hc.getContent());
				assertNotNull(hc.getTs());
			}

		} catch (ParserConfigurationException e) {
			log.info(e.getMessage());
		} catch (SAXException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
		}

	}

}
