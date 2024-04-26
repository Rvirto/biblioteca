package com.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Sql({ "/cleanup.sql", "/dataset.sql"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIf(expression = "#{environment['spring.profiles.active'] == null}")
public class ApplicationTests<T extends ApplicationTests<?>> {

    private final T endpointTest = (T) this;
    private final YamlPropertiesFactoryBean yamlProperties = new YamlPropertiesFactoryBean();

    @Autowired
    protected MockMvc mockMvc;

    protected ApplicationTests() {
        super();
        yamlProperties.setResources(
                new ClassPathResource("scenarios/endPoints/" + endpointTest.getClass().getSimpleName() + ".yml"));
    }

    protected String getScenarioBody(final String scenario) {
        return yamlProperties.getObject().getProperty(scenario + ".body");
    }

    protected String getBookIdOfLocation(String location) {
        Matcher matcher = Pattern.compile(".*\\/books\\/([a-zA-Z0-9-]+)(\\/?).*").matcher(location);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    protected String getClientIdOfLocation(String location) {
        Matcher matcher = Pattern.compile(".*\\/clients\\/([a-zA-Z0-9-]+)(\\/?).*").matcher(location);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
