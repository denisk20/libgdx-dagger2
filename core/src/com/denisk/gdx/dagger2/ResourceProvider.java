package com.denisk.gdx.dagger2;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author denisk
 * @since 11/9/15.
 */
@Singleton
public class ResourceProvider {
    @Inject
    public ResourceProvider() {
    }

    public String getLogo() {
        return "badlogic.jpg";
    }
}
