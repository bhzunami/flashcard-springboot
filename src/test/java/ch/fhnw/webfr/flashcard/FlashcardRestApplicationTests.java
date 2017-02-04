package ch.fhnw.webfr.flashcard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.fhnw.webfr.flashcard.TestUtil.QuestionnaireBuilder;
import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.web.QuestionnaireController;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class FlashcardRestApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private QuestionnaireRepository questionnaireRepositoryMock;
	
	@Before
	public void setUp() {
		Mockito.reset(questionnaireRepositoryMock);
	}
	
	@Test
	public void get_AllQuestionnaire_ShouldReturnOK() throws Exception {
		List<Questionnaire> questionnaires = new ArrayList<>();
		questionnaires.add(new QuestionnaireBuilder("1")
				.description("MyDescription")
				.title("Titel 1")
				.build());
		
		questionnaires.add(new QuestionnaireBuilder("2")
				.description("MyDescription2")
				.title("Titel 2")
				.build());
		
		questionnaires.add(new QuestionnaireBuilder("3")
				.description("MyDescription3")
				.title("Titel 3")
				.build());
		
		when(questionnaireRepositoryMock.findAll(new Sort("id"))).thenReturn(questionnaires);
		
		mockMvc.perform(get("/questionnaires")
				.header("Accept", "application/json"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id", is("1")))
		.andExpect(jsonPath("$[0].title", is("Titel 1")))
		.andExpect(jsonPath("$[0].description", is("MyDescription")))
		.andExpect(jsonPath("$[1].id", is("2")))
		.andExpect(jsonPath("$", hasSize(3)));
		
		Mockito.verify(questionnaireRepositoryMock, times(1)).findAll(new Sort("id"));

	}

	@Test
	public void create_NewQuestionnaire_ShouldReturnOK() throws JsonProcessingException, Exception {
		Questionnaire questionnaire = new QuestionnaireBuilder("1")
				.description("MyDescription")
				.title("Titel 1")
				.build();
		
		Questionnaire questionnaire2 = new QuestionnaireBuilder("1")
				.description("MyDescription")
				.title("Titel 1")
				.build();
		
		
		when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire2);
		
		mockMvc.perform(post("/questionnaires")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaire)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is("1")))
		.andExpect(jsonPath("$.title", is("Titel 1")))
		.andExpect(jsonPath("$.description", is("MyDescription")));
		
		Mockito.verify(questionnaireRepositoryMock, times(1)).save(questionnaire);
	}
	
	
	@Test
	public void update_NewQuestionnaire_ShouldReturnOK() throws JsonProcessingException, Exception {
		Questionnaire questionnaire = new QuestionnaireBuilder("1")
				.description("MyDescription")
				.title("Titel 1")
				.build();
		
		
		when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);
		when(questionnaireRepositoryMock.findOne(questionnaire.getId())).thenReturn(questionnaire);
		
		mockMvc.perform(post("/questionnaires")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaire)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is("1")))
		.andExpect(jsonPath("$.title", is("Titel 1")))
		.andExpect(jsonPath("$.description", is("MyDescription")));
		
		Mockito.verify(questionnaireRepositoryMock, times(1)).save(questionnaire);
		
		
		questionnaire.setTitle("Neuer Titel");
		questionnaire.setDescription("Desc 2");
		
		mockMvc.perform(put("/questionnaires/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaire)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is("1")))
		.andExpect(jsonPath("$.title", is("Neuer Titel")))
		.andExpect(jsonPath("$.description", is("Desc 2")));
		
		
		
	}
	
	
	

}
