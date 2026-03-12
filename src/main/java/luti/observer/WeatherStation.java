package luti.observer;

import luti.observer.subject.WeatherData;
import luti.observer.display.CurrentConditionsDisplay;
import luti.observer.display.StatisticsDisplay;
import luti.observer.display.ForecastDisplay;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();

		System.out.println("---------- [1] 옵저버 3개 등록 ----------");
		CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
		StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
		ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

		System.out.println("\n---------- [2] 첫 번째 측정 (3개 모두 알림) ----------");
		weatherData.setMeasurements(80, 65, 30.4f);

		System.out.println("\n---------- [3] 두 번째 측정 (3개 모두 알림) ----------");
		weatherData.setMeasurements(82, 70, 29.2f);

		System.out.println("\n---------- [4] StatisticsDisplay 구독 해제 ----------");
		weatherData.removeObserver(statisticsDisplay);

		System.out.println("\n---------- [5] 세 번째 측정 (2개만 알림, Statistics 제외) ----------");
		weatherData.setMeasurements(78, 90, 29.2f);
	}
}
