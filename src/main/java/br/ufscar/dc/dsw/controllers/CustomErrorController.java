//package br.ufscar.dc.dsw.controllers;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.time.LocalDateTime; // Para adicionar timestamp
//
//@Controller
//@RequestMapping("/error") // Mapeia este controller para o caminho /error
//public class CustomErrorController implements ErrorController {
//
//    @RequestMapping
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Integer statusCode = null;
//
//        if (status != null) {
//            statusCode = Integer.valueOf(status.toString());
//            model.addAttribute("statusCode", statusCode);
//            model.addAttribute("timestamp", LocalDateTime.now()); // Adiciona o timestamp
//            model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
//            model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
//            model.addAttribute("exception", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
//
//        }
//
//        if (statusCode == HttpStatus.NOT_FOUND.value()) {
//            return "error/error-404"; // Retorna o nome da view Thymeleaf para 404
//        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
//            return "error/error-403"; // Retorna o nome da view Thymeleaf para 403
//        } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
//            return "error/error-400"; // Retorna o nome da view Thymeleaf para 400
//        } else if (statusCode >= 500 && statusCode < 600) {
//            return "error/error-500";
//        }
//        return "error/error";
//    }
//
//
//}