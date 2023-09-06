package com.bkcoding.contactsyncapp.repository.di

import com.bkcoding.contactsyncapp.repository.ContactRepository
import com.bkcoding.contactsyncapp.repository.IContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindContactRepository(
        contactRepository: ContactRepository
    ): IContactRepository
}