package com.planning.poker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.planning.poker.util.Utils.formatDouble;

public class MathUtils {
	public static Double calculateMean(List<Double> votes) {
		double sum = 0;
		for (Double vote : votes) {
			sum += vote;
		}
		return formatDouble(divide(sum, votes.size()));
	}

	public static Double calculateMedian(List<Double> votes) {

		List<Double> sortedValues = votes.stream()
				.sorted().collect(Collectors.toList());
		int size = sortedValues.size();
		int midIndex = getMidOfSize(size);
		return votes.size() > 0 ? calculateMedianPairOrOdd(sortedValues, size, midIndex) : 0;
	}

	public static String modeStringify(List<Double> votes){
		Map<Double, Long> FrequencyOfVotes = getFrequencyOfVotes(votes);

		List<Map.Entry<Double, Long>> moda = new ArrayList<>();
		Long maxValue = Long.MIN_VALUE;

		for (Map.Entry<Double, Long> entry : FrequencyOfVotes.entrySet()) {
			Long value = entry.getValue();
			processMode(maxValue, value, moda, entry);

		}
		return generateStringMode(moda);
	}

	private static double calculateMedianPairOrOdd(List<Double> sortedValues, int size, int midIndex) {
		return size % 2 == 0 ? medianOdd(sortedValues, midIndex) : sortedValues.get(midIndex);
	}

	private static double medianOdd(List<Double> sortedValues, int midIndex) {
		return (sortedValues.get(midIndex - 1) + sortedValues.get(midIndex)) / 2;
	}

	private static double divide(Double sumVotes, int votesQuantity) {
		return sumVotes / (double) Math.max(votesQuantity, 1);
	}

	private static int getMidOfSize(int size) {
		return size / 2;
	}

	private static Map<Double, Long> getFrequencyOfVotes(List<Double> votes){
		return votes.stream()
				.collect(Collectors.groupingBy(vote -> vote, Collectors.counting()));
	}

	private static void processMode(Long maxValue, Long value, List<Map.Entry<Double, Long>> moda, Map.Entry<Double, Long> entry){
		if (value.equals(maxValue)) {
			moda.add(entry);
		}
		if (value > maxValue) {
			moda.clear();
			moda.add(entry);
			maxValue = value;
		}
	}
	private static String generateStringMode(List<Map.Entry<Double, Long>> moda){
		String stringMode = "";
		if(moda.isEmpty() || moda.size() > 2){
			stringMode = "N/A";
		}else{
			stringMode = generateStringModes(moda, stringMode);
		}
		return stringMode;
	}

	private static String generateStringModes(List<Map.Entry<Double, Long>> moda, String stringMode) {
		for (Map.Entry<Double, Long> entry : moda) {
			if(!stringMode.equals("")){
				stringMode = stringMode.concat(" e ");
			}
			stringMode = stringMode.concat(String.valueOf(entry.getKey()));
		}
		return stringMode;
	}
}
