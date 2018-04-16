package com.todo.di.component;

import com.todo.TodoApplication;
import com.todo.di.module.ApplicationModule;
import com.todo.di.module.DataModule;
import com.todo.di.module.ServiceModule;
import com.todo.di.module.UtilsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class, UtilsModule.class, DataModule.class,})
public interface ApplicationComponent extends ApplicationComponentInjects, ApplicationComponentExposes {

    final class Initializer {

        public static ApplicationComponent init(final TodoApplication todoApplication) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(todoApplication))
                    .utilsModule(new UtilsModule())
                    .dataModule(new DataModule())
                    .serviceModule(new ServiceModule())
                    .build();
        }

        private Initializer() {
        }
    }
}
