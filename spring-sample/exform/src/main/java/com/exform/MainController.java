package com.exform;

import com.exform.domain.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Controller
@PropertySource("classpath:some.properties")
public class MainController {
    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Environment environment;

    @Value("${hello.msg:default}")
    private String helloMsg;

    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public ResponseEntity<String> pageNotFound() {
        return new ResponseEntity<String>("Page not found", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value="/welcome", method = RequestMethod.GET)
    public String welcome(Locale locale) {
        logger.debug(locale);
        logger.debug(this.helloMsg);
        String helloMsg = environment.getProperty("hello.msg");
        String curMsg = messageSource.getMessage("greeting.msg", null, "default", locale);
        String engMsg = messageSource.getMessage("greeting.msg", null, Locale.US);
        return "welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, ModelMap modelMap,
                        @Valid User user, BindingResult bindingResult, Locale locale) throws Exception {

        logger.debug(user);
        logger.debug(locale);

        if(bindingResult.hasErrors()){
            logger.debug("Binding Result has error!");
            List<ObjectError> errors = bindingResult.getAllErrors();
            for(ObjectError error : errors){
                logger.debug(error.getDefaultMessage());
            }

            return "login";
        }

        if (user.getPassword().equalsIgnoreCase("admin")) {
            modelMap.addAttribute("email", user.getEmail());
            session.setAttribute("email", user.getEmail());
            return "redirect:/welcome";
        }

        return "redirect:/pages/login_error.html";
    }
}

