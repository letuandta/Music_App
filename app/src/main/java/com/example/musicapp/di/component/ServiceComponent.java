package com.example.musicapp.di.component;

import com.example.musicapp.di.scope.ServiceScope;
import com.example.musicapp.services.MusicPlayerService;

import dagger.Component;

@ServiceScope
@Component(dependencies = {AppComponent.class})
public interface ServiceComponent {
    void inject(MusicPlayerService service);
}
