package musicGame.util;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Test;

public class HashMapBuilderTest {

	@Test
	public void buildsEmptyMap() {
		assertThat(HashMapBuilder.newBuilder().build(), is(new HashMap<>()));
	}
	
	@Test
	public void buildsMapUsingElementsProvided() {
		HashMap<String, String> expectedMap = new HashMap<>();
		expectedMap.put("key", "value");
		assertThat(HashMapBuilder.newBuilder().put("key", "value").build(), is(expectedMap));
	}
	
	@Test
	public void buildReturnsCopyOfBuilderMap() {
		HashMapBuilder<String, String> builder =
				HashMapBuilder.<String, String>newBuilder().put("key", "value");
		HashMap<String, String> modifiedMap = builder.build();
		modifiedMap.put("key", "value2");
		assertThat(builder.build(), is(not(modifiedMap)));
	}
}
