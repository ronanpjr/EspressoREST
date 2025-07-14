package br.ufscar.dc.dsw.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/error-404");
        mav.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler({IllegalStateException.class, AccessDeniedException.class})
    public Object handleBusinessExceptions(
            Exception ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        String requestURI = request.getRequestURI();
        logger.warn("Operação negada ou inválida na URI [{}]: {}", requestURI, ex.getMessage());

        // Check if it's an API call
        if (requestURI.startsWith("/api/")) {
            HttpStatus status = (ex instanceof AccessDeniedException) ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST;
            Map<String, String> errorBody = Map.of(
                    "status", String.valueOf(status.value()),
                    "error", status.getReasonPhrase(),
                    "message", ex.getMessage(),
                    "path", requestURI
            );
            return new ResponseEntity<>(errorBody, status);
        }

        // Keep the original redirect logic for web pages
        redirectAttributes.addFlashAttribute("mensagemFalha", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }
}
