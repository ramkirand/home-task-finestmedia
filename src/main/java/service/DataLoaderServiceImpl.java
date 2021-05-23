package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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
  public String loadSeedData() throws ParserConfigurationException, SAXException, IOException {
    ResponseEntity<String> respXmlString = null;
    try {
      String url = createUrl();
      respXmlString = restTemplate.getForEntity(url, String.class);
      if (respXmlString.getBody().contains(INVALID))
        throw new ApiRequestException(Constant.SERVICE_UNAVIALABLE);
      EnergyReport energyReport = SaxXmlParser.parser(respXmlString.getBody());

      int size =
          energyReport.getAccountTimeSeries().getConsumptionHistory().getHourConsumption().size();

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
    HourConsumption hourConsumption =
        data.getAccountTimeSeries().getConsumptionHistory().getHourConsumption().get(index);

    String measurmentPrice = hourConsumption.getContent();
    String str[] = hourConsumption.getTs().substring(0, 10).split("-");
    StringBuilder sb = new StringBuilder();

    sb.append(str[2]).append("-").append(str[1]).append("-").append(str[0]);

    consumptionData.setMeasurmentPrice(measurmentPrice);
    consumptionData.setDocumentDateTime(sb.toString());
    return consumptionData;
  }

  private String createUrl() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_PATTERN);
    LocalDate currentTime = LocalDate.now();
    LocalDate endTime = currentTime.minusDays(1);
    LocalDate prev = currentTime.minusDays(Constant.DAYS_TO_SUBTRACT);
    String startTime = formatter.format(prev);
    String currentLocaTime = formatter.format(endTime);
    String str = Constant.URL_START + startTime + Constant.URL_END_PART + currentLocaTime;
    return str;

  }

}
