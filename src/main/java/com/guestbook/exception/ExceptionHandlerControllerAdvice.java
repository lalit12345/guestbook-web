package com.guestbook.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	@ExceptionHandler(BusinessException.class)
	public ModelAndView handleException(BusinessException businessException) {

		log.error("Error processing the request: {}", businessException.getMessage());

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("error/500");

		return modelAndView;
	}
}
