package com.sma.plugins.jira.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.InvalidInputException;

import java.util.Map;

public class resolution_validator implements Validator {
	private static final Logger log = LoggerFactory
			.getLogger(resolution_validator.class);

	// public static final String FIELD_WORD="word";
	public static final long FIELD_ID=11020;
	
	public void validate(Map transientVars, Map args, PropertySet ps)
			throws InvalidInputException {
		// String word = (String) transientVars.get(FIELD_WORD);

		Issue issue = (Issue) transientVars.get("issue");

		// if(null == issue.getDescription() ||
		// "".equals(issue.getDescription()) ||
		// !issue.getDescription().contains(word)) {
		// throw new InvalidInputException("Issue must contain the word '" +
		// word + "' in the description");
		// }

		IssueManager issueManager = ComponentAccessor.getIssueManager();

		CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
		CustomField customField = customFieldManager.getCustomFieldObject(FIELD_ID);
		if(customField==null)
			throw new InvalidInputException(
					"Не удалось получить кастомное поле по идентификатору"+FIELD_ID+"!");
		Issue issue_old=issueManager.getIssueObject(issue.getId());
		if(issue_old==null)
			throw new InvalidInputException(
					"Не удалось получить старый объект задачи!");
		String res_old_str="";
		String res_new_str="";
		Object res_old = issue_old
				.getCustomFieldValue(customField);
		if(res_old!=null)
			res_old_str=res_old.toString();
		Object res_new = issue
				.getCustomFieldValue(customField);
		if(res_new!=null)
		{
			res_new_str=res_new.toString();
		}
		if (res_new_str.equalsIgnoreCase(res_old_str))
			throw new InvalidInputException(
					"Резолюция согласования не может остаться неизменной!");
	}
}
