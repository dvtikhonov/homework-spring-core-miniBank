package tikhonov.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Ручная конфигурация Spring-контекста: сканирование компонентов и файл настроек.
 */
@Configuration
@ComponentScan(
        basePackages = "tikhonov.dev",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ApplicationConfiguration.class
        )
)
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {

    /**
     * Включает подстановку {@code ${...}} в {@link org.springframework.beans.factory.annotation.Value}.
     *
     * @return конфигуратор плейсхолдеров
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
