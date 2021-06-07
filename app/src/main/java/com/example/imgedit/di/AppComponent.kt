package com.example.imgedit.di

import com.example.imgedit.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {

    fun injectMainActivity(mainActivity: MainActivity)

}