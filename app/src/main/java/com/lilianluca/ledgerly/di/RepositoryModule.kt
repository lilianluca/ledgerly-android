package com.lilianluca.ledgerly.di

import com.lilianluca.ledgerly.data.repository.RoomTransactionRepository
import com.lilianluca.ledgerly.data.repository.RoomWalletRepository
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import com.lilianluca.ledgerly.data.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Decides which implementation is used wherever a repository interface is requested. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: RoomTransactionRepository): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(impl: RoomWalletRepository): WalletRepository
}
