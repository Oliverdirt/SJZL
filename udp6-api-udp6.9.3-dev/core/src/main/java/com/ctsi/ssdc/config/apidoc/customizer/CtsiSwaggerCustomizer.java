package com.ctsi.ssdc.config.apidoc.customizer;

import com.ctsi.ssdc.config.CtsiProperties;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * A swagger customizer to setup {@link Docket} with JHipster settings.
 */
public class CtsiSwaggerCustomizer implements SwaggerCustomizer, Ordered {

	/**
	 * The default order for the customizer.
	 */
	public static final int DEFAULT_ORDER = 0;

	private int order = DEFAULT_ORDER;

	private final CtsiProperties.Swagger properties;

	public CtsiSwaggerCustomizer(CtsiProperties.Swagger properties) {
		this.properties = properties;
	}

	@Override
	public void customize(Docket docket) {
		Contact contact = new Contact(properties.getContactName(), properties.getContactUrl(),
				properties.getContactEmail());

		ApiInfo apiInfo = new ApiInfo(properties.getTitle(), properties.getDescription(), properties.getVersion(),
				properties.getTermsOfServiceUrl(), contact, properties.getLicense(), properties.getLicenseUrl(),
				new ArrayList<>());

		docket.host(properties.getHost()).protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
				.securitySchemes(Arrays.asList(new ApiKey("Authorization", "Authorization", "header")))
				.securityContexts(Arrays.asList(securityContext())).apiInfo(apiInfo).forCodeGeneration(true)
				.directModelSubstitute(ByteBuffer.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.select().paths(regex(properties.getDefaultIncludePattern())).build();
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex(properties.getDefaultIncludePattern()))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}
}
