package window.learning.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;

import window.learning.constant.MetaField;

public class DataTypeSelector implements OutputSelector<Map<String, String>>{
	private static final long serialVersionUID = 8579955777259904702L;

	@Override
	public Iterable<String> select(Map<String, String> input) {
		List<String> tags=new ArrayList<String>();
		if(input!=null) {
			tags.add(input.get(MetaField.ITEM_DATA_TYPE));
		}
		return tags;
	}

}
