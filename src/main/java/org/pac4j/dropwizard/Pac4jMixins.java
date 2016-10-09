package org.pac4j.dropwizard;

import java.util.List;

import org.pac4j.core.authorization.generator.AuthorizationGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Some mixins usable to tweak Jackson behaviour when parsing dropwizard's YAML
 * configuration file.
 * 
 * @author Victor Noel - Linagora
 * @since 1.0.0
 */
public class Pac4jMixins {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public abstract static class ClientMixin {

    }

    public abstract static class BaseClientMixin extends ClientMixin {

        @JsonProperty
        public abstract void setAuthorizationGenerators(
                List<AuthorizationGenerator> authorizationGenerators);

        @JsonIgnore
        public abstract void setAuthorizationGenerators(
                AuthorizationGenerator... authorizationGenerators);

        @JsonIgnore
        public abstract void setAuthorizationGenerator(
                AuthorizationGenerator authorizationGenerator);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public abstract static class InstantiateByClassNameMixin {

    }

}
