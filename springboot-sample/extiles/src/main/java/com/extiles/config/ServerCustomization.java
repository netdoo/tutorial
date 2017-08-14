package com.extiles.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ServerCustomization extends ServerProperties {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        super.customize(container);
        container.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/views/jsp/error/404.jsp"),
                new ErrorPage(HttpStatus.BAD_REQUEST, "/views/jsp/error/500.jsp"),
                new ErrorPage("/error")
        );
    }
}

