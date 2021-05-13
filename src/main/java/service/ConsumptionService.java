package service;

import java.util.List;

import org.springframework.stereotype.Service;

import model.ConsumptionData;

@Service
public interface ConsumptionService {
	List<ConsumptionData> findAllData();

	List<ConsumptionData> findByMeasurmentPrice(String startDate, String price);
}
