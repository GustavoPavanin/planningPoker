package com.planning.poker.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest {

	@Test
	public void deve_retornar_media_calculada_corretamente_3_votos(){
		Double media = MathUtils.calculateMean(List.of(3d, 5d, 2d));
		assertEquals(3.33, media);
	}

	@Test
	public void deve_retornar_mediana_calculada_corretamente_3_votos(){
		Double mediana = MathUtils.calculateMedian(List.of(3d, 5d, 2d));
		assertEquals(3, mediana);
	}

	@Test
	public void deve_retornar_moda_corretamente_3_votos(){
		String moda = MathUtils.modeStringify(List.of(4d, 5d, 5d));
		assertEquals("5.0", moda);
	}
}
