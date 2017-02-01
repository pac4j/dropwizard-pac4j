package org.pac4j.dropwizard;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.BaseClient;
import org.pac4j.core.client.Client;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.engine.ApplicationLogoutLogic;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.CallbackUrlResolver;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.core.matching.Matcher;
import org.pac4j.core.profile.creator.ProfileCreator;
import org.pac4j.core.redirect.RedirectActionBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.setup.Bootstrap;

/**
 * This can be specialized in order to redefine some of the mixins to customize
 * Jackson's behaviour to parse pac4j's {@link Pac4jFactory} in case things are
 * missing.
 * 
 * @author Victor Noel - Linagora
 * @since 1.0.0
 *
 */
public class DefaultFeatureSupport implements Pac4jFeatureSupport {

    @Override
    public void setup(Bootstrap<?> bootstrap) {
        ObjectMapper om = bootstrap.getObjectMapper();
        om.addMixIn(Client.class, clientMixin());
        om.addMixIn(BaseClient.class, baseClientMixin());
        om.addMixIn(Authenticator.class, authenticatorMixin());
        om.addMixIn(PasswordEncoder.class, passwordEncoderMixin());
        om.addMixIn(CredentialsExtractor.class, credentialExtractorMixin());
        om.addMixIn(ProfileCreator.class, profileCreatorMixin());
        om.addMixIn(AuthorizationGenerator.class,
                authorizationGeneratorMixin());
        om.addMixIn(Authorizer.class, authorizerMixin());
        om.addMixIn(Matcher.class, matcherMixin());
        om.addMixIn(RedirectActionBuilder.class, redirectActionBuilderMixin());
        om.addMixIn(CallbackUrlResolver.class, callbackUrlResolverMixin());
        om.addMixIn(HttpActionAdapter.class, httpActionAdapterMixin());
        om.addMixIn(SecurityLogic.class, securityLogicMixin());
        om.addMixIn(CallbackLogic.class, callbackLogicMixin());
        om.addMixIn(ApplicationLogoutLogic.class,
                applicationLogoutLogicMixin());
    }

    private Class<?> applicationLogoutLogicMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> callbackLogicMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> securityLogicMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> httpActionAdapterMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> callbackUrlResolverMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> redirectActionBuilderMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> matcherMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> authorizerMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> authorizationGeneratorMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> profileCreatorMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> credentialExtractorMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> passwordEncoderMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> authenticatorMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> baseClientMixin() {
        return Pac4jMixins.BaseClientMixin.class;
    }

    private Class<?> clientMixin() {
        return Pac4jMixins.ClientMixin.class;
    }

}
