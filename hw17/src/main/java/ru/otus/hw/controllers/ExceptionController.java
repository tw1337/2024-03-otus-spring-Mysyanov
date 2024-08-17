package ru.otus.hw.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundEx(EntityNotFoundException ex) {
        var errorMessage = ex.getMessage();
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", errorMessage);
        mav.setViewName("error/entitynotfound");
        return mav;
    }
}
