package com.fadhil.submissionpart2.Database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Dao;
import androidx.room.OnConflictStrategy

@Dao

interface FavoriteUserDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE
    )
     fun addToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    fun checUser(id: Int) : Int

    @Query ("DELETE FROM favorite_user WHERE favorite_user.id = :id")
   fun removeFromFavorite(id: Int) : Int

}