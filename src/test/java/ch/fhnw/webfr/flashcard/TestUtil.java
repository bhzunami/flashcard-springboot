package ch.fhnw.webfr.flashcard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;

public class TestUtil {
	
	public static class QuestionnaireBuilder {
		private Questionnaire questionnaire;
		
		public QuestionnaireBuilder(String id) {
			questionnaire = new Questionnaire();
			questionnaire.setId(id);
		}
		
		public QuestionnaireBuilder title(String title) {
			questionnaire.setTitle(title);
			return this;
		}
		
		public QuestionnaireBuilder description(String description) {
			questionnaire.setDescription(description);
			return this;
		}
		
		public Questionnaire build() {
			return questionnaire;
		}
		
	}

	
	public static byte[] convertObjectToJsonBytes(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
