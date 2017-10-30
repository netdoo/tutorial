package com.exthymeleaf;

import com.exthymeleaf.domain.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("XHTML");
        resolver.setSuffix(".html");

        // 1. 탬플릿엔진 설정
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        StringWriter writer = new StringWriter();

        // 2. 탬플릿 컨택스트 설정
        Context context = new Context();

        Box redBox = new Box("1", "red");
        Box greenBox = new Box("2", "green");

        String now = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        // 3. 탬플릿에서 치환될 값 설정
        context.setVariable("date", now);
        context.setVariable("redBox", redBox);
        context.setVariable("greenBox", greenBox);
        context.setVariable("boxList", Arrays.asList(redBox, greenBox));

        // 4. 탬플릿 변환
        engine.process("boxTemplate", context, writer);

        logger.info("{}", writer.toString());
    }
}
