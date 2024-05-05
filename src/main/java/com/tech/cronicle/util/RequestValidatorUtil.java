package com.tech.cronicle.util;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import com.tech.cronicle.constants.ErrorCodes;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.requests.JobRequest;

@Component
public class RequestValidatorUtil {
	private void throwException(boolean throwException, String fieldName) throws FinalException {
		if (throwException) {
			throw new FinalException(ErrorCodes.NULL_OR_EMPTY, ErrorCodes.NULL_OR_EMPTY_MESSAGE + fieldName);
		}
	}

	public boolean isStringNullOrEmpty(String value) {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isValidEnum(String value, Class<?> clazz) {
		if (value != null && clazz.isEnum()) {
			for (Object object : clazz.getEnumConstants()) {
				if (value.equals(object.toString())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isListEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isFirstDateBeforeSecond(Date firstDate, Date secondDate) {
		if (firstDate != null && secondDate != null && firstDate.before(secondDate)) {
			return true;
		}
		return false;

	}

	private boolean isValidUrl(String url) {
		if (isStringNullOrEmpty(url)) {
			return false;
		}
		String regex = "((http|https)://)(www.)?[a-zA-Z0-9@:%.\\-_\\+~#?&//=]{2,256}";
		return Pattern.matches(regex, url);
	}

	private boolean isValidCronExpression(String expression) {
		if (CronExpression.isValidExpression(expression)) {
			return true;
		}
		return false;
	}

	public void validateInsertJobRequest(JobRequest objJobRequest) throws FinalException {
		throwException(isStringNullOrEmpty(objJobRequest.getJobName()), "Job Name");
		throwException(isStringNullOrEmpty(objJobRequest.getDescription()), "Job Description");
		throwException(!isValidUrl(objJobRequest.getRequestUrl()), "Request URL");
		throwException(!isValidCronExpression(objJobRequest.getCronExpression()), "Cron Expression");
		if (!isStringNullOrEmpty(objJobRequest.getCallbackUrl())) {
			throwException(!isValidUrl(objJobRequest.getCallbackUrl()), "Callback URL");
		}
	}

	public void validateUpdateJobRequest(JobRequest objJobRequest) throws FinalException {
		if (objJobRequest.getJobID() == null) {
			throwException(true, "jobID");
		}
		if (!isStringNullOrEmpty(objJobRequest.getRequestUrl())) {
			throwException(!isValidUrl(objJobRequest.getRequestUrl()), "Request URL");
		}
		if (!isStringNullOrEmpty(objJobRequest.getCallbackUrl())) {
			throwException(!isValidUrl(objJobRequest.getCallbackUrl()), "Callback URL");
		}
		if (!isStringNullOrEmpty(objJobRequest.getCronExpression())) {
			throwException(!isValidCronExpression(objJobRequest.getCronExpression()), "Cron Expression");
		}
	}

}
