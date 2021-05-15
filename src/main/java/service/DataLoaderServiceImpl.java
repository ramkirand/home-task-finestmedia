package service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import annotation.TrackExecutionTime;
import constant.Constant;
import exception.ApiRequestException;
import model.ConsumptionData;
import model.EnergyReport;
import model.HourConsumption;
import repository.EnergyReportRepository;
import util.SaxXmlParser;

@Component
public class DataLoaderServiceImpl implements DataLoaderService {

	private static final String INVALID = "Invalid";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private EnergyReportRepository energyReportRepository;

	@Override
	@TrackExecutionTime
	public String loadSeedData(String url) throws ParserConfigurationException, SAXException, IOException {
		ResponseEntity<String> respXmlString = null;
		try {
			respXmlString = restTemplate.getForEntity(url, String.class);
			if (respXmlString.getBody().contains(INVALID))
				throw new ApiRequestException(Constant.SERVICE_UNAVIALABLE);
			EnergyReport energyReport = SaxXmlParser.parser(respXmlString.getBody());

			int size = energyReport.getAccountTimeSeries().getConsumptionHistory().getHourConsumption().size();

			for (int index = 0; index < size; index++) {
				ConsumptionData consumptionData = buildCustomerData(energyReport, index);
				energyReportRepository.save(consumptionData);
			}

		} catch (HttpStatusCodeException ex) {
			throw new ApiRequestException(ex.getMessage());
		}
		return Constant.DATALOADED_SUCCESS;
	}

	public String loadSeedDataFallBack() {
		return Constant.SERVICE_UNAVIALABLE;
	}

	private ConsumptionData buildCustomerData(EnergyReport data, int index) {
		ConsumptionData consumptionData = new ConsumptionData();
		consumptionData.setDocumentDateTime(data.getDocumentDateTime());
		consumptionData.setDocumentIdentification(data.getDocumentIdentification());
		consumptionData.setAccountingPoint((data.getAccountTimeSeries().getAccountingPoint()));
		consumptionData.setMeasurementUnit(data.getAccountTimeSeries().getMeasurementUnit());
		HourConsumption hourConsumption = data.getAccountTimeSeries().getConsumptionHistory().getHourConsumption()
				.get(index);

		String measurmentPrice = hourConsumption.getContent();
		String timeTs = hourConsumption.getTs();

		consumptionData.setMeasurmentPrice(measurmentPrice);
		consumptionData.setDocumentDateTime(timeTs);
		return consumptionData;
	}

}
