package ch.fhnw.webfr.flashcard.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
	
	private static final Log logger = LogFactory.getLog(QuestionnaireController.class);

	
	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET) 
	public ResponseEntity<List<Questionnaire>> findAll() throws InterruptedException {
		logger.debug("Get all questionnaiers");
		Sort sort = new Sort(Direction.ASC, "id");
		List<Questionnaire> questionnaires = questionnaireRepository.findAll(sort);
		return new ResponseEntity<>(questionnaires, HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Questionnaire> create(@RequestBody @Valid Questionnaire question, BindingResult result ) {
		if(result.hasErrors()) {
			logger.error("Error in form detected");
			for(ObjectError e: result.getAllErrors()) {
				logger.error("Error code: " +e.getCode() +" Message: " +e.getDefaultMessage());
			}
			return new ResponseEntity<Questionnaire>(HttpStatus.BAD_REQUEST);
		}
		
		//question.setId(null);
		question = questionnaireRepository.save(question);
		
		// Das ist eine URL und nicht mehr ein Template
		return new ResponseEntity<>(question, HttpStatus.CREATED);
	}
	
	@CrossOrigin
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Questionnaire> update(@PathVariable String id, @RequestBody @Valid Questionnaire question, BindingResult result ) {
		if(result.hasErrors()) {
			logger.error("Error in form detected");
			for(ObjectError e: result.getAllErrors()) {
				logger.error("Error code: " +e.getCode() +" Message: " +e.getDefaultMessage());
			}
			return new ResponseEntity<Questionnaire>(HttpStatus.BAD_REQUEST);
		}
		
		Questionnaire to_be_updated = questionnaireRepository.findOne(id);
		
		if(to_be_updated == null) {
			logger.error("Question with id " +id +" not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		to_be_updated.setTitle(question.getTitle());
		to_be_updated.setDescription(question.getDescription());
		
		questionnaireRepository.save(question);
		logger.debug("Updated question: " +question.getId());
		return new ResponseEntity<>(question, HttpStatus.OK);
		

	}
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<Questionnaire> delete(@PathVariable String id ) {
		if(questionnaireRepository.findOne(id) == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		questionnaireRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

}
