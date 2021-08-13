package shr.training_camp.core.config.factory;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    // ToDo It will be used when project will be wider

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        return Objects.isNull(name) ?
                getPropertySourceFromListOfProperties(new YamlPropertySourceLoader().load(getNameForResource(resource.getResource()), resource.getResource())) :
                getPropertySourceFromListOfProperties(new YamlPropertySourceLoader().load(name, resource.getResource()));
    }

    private static String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }

    private PropertySource<?> getPropertySourceFromListOfProperties(final List<PropertySource<?>> listOfSources) {
        String profileName = System.getProperty("spring.profiles.active");
        PropertySource<?> propertySourceItem = null;
        List<PropertySource<?>> envPropertySources = listOfSources.stream().filter(propertySource ->
                Objects.nonNull(propertySource.getProperty("spring.profiles"))).collect(Collectors.toList());
        Optional<PropertySource<?>> env = envPropertySources.stream().filter(propertySource ->
                Objects.equals(propertySource.getProperty("spring.profiles"), profileName)).findAny();
        if (env.isPresent()) {
            propertySourceItem = env.get();
        }
        return propertySourceItem;
    }

}
