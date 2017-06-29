package com.exjetty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EchoController {

    final static Logger logger = LoggerFactory.getLogger(EchoController.class);

    @RequestMapping(value="/echo", method = RequestMethod.GET)
    @ResponseBody
    public String echo(@RequestParam(value="message", required=false, defaultValue="1") String message) throws Exception {

        Thread.sleep((int)Math.random() % 1000);

        if (System.currentTimeMillis() % 10 == 0) {
            logger.info("random exception raised");
            throw new IllegalArgumentException("random exception raised");
        }

        return message.toUpperCase();
    }

    @CrossOrigin
    @RequestMapping(value="/postecho", method = RequestMethod.POST)
    @ResponseBody
    public String postecho(@RequestParam(value="message", required=false, defaultValue="1") String message) throws Exception {

        Thread.sleep((int)Math.random() % 1000);

        if (System.currentTimeMillis() % 10 == 0) {
            logger.info("random exception raised");
            throw new IllegalArgumentException("random exception raised");
        }

        return message.toUpperCase();
    }
}
