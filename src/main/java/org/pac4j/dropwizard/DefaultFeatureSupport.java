package org.pac4j.dropwizard;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.BaseClient;
import org.pac4j.core.client.Client;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.LogoutLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.http.ajax.AjaxRequestResolver;
import org.pac4j.core.http.callback.CallbackUrlResolver;
import org.pac4j.core.http.url.UrlResolver;
import org.pac4j.core.logout.LogoutActionBuilder;
import org.pac4j.core.matching.matcher.Matcher;
import org.pac4j.core.profile.creator.ProfileCreator;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.core.setup.Bootstrap;
import org.pac4j.core.redirect.RedirectionActionBuilder;

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

        // for Config
        om.addMixIn(SessionStore.class, sessionStoreMixin());
        om.addMixIn(Authorizer.class, authorizerMixin());
        om.addMixIn(HttpActionAdapter.class, httpActionAdapterMixin());
        om.addMixIn(Matcher.class, matcherMixin());
        om.addMixIn(SecurityLogic.class, securityLogicMixin());
        om.addMixIn(CallbackLogic.class, callbackLogicMixin());
        om.addMixIn(LogoutLogic.class, logoutLogicMixin());

        // for Clients
        om.addMixIn(Client.class, clientMixin());
        om.addMixIn(BaseClient.class, baseClientMixin());

        // for Clients and Client subsclasses
        om.addMixIn(AjaxRequestResolver.class, ajaxRequestResolverMixin());
        om.addMixIn(UrlResolver.class, urlResolverMixin());
        om.addMixIn(CallbackUrlResolver.class, callbackUrlResolverMixin());
        om.addMixIn(AuthorizationGenerator.class,
                authorizationGeneratorMixin());

        // for Client/BaseClient
        om.addMixIn(Authenticator.class, authenticatorMixin());
        om.addMixIn(CredentialsExtractor.class, credentialExtractorMixin());
        om.addMixIn(ProfileCreator.class, profileCreatorMixin());

        // for IndirectClient
        om.addMixIn(RedirectionActionBuilder.class, redirectionActionBuilderMixin());
        om.addMixIn(LogoutActionBuilder.class, logoutActionBuilderMixin());

        // for some of the Authenticators
        om.addMixIn(PasswordEncoder.class, passwordEncoderMixin());
    }

    private Class<?> sessionStoreMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> ajaxRequestResolverMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> logoutLogicMixin() {
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

    private Class<?> urlResolverMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> callbackUrlResolverMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> redirectionActionBuilderMixin() {
        return Pac4jMixins.InstantiateByClassNameMixin.class;
    }

    private Class<?> logoutActionBuilderMixin() {
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
