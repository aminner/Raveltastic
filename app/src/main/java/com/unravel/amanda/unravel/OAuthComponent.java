package com.unravel.amanda.unravel;

import com.unravel.amanda.unravel.ravelryapi.AuthenticatedRavelApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RavelOAuthModule.class})
public interface OAuthComponent {
    void inject(AuthenticatedRavelApiService authenticatedRavelApiService);
}
