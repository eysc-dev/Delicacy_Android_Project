package com.yschang.delicacy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yschang.delicacy.entity.MyUser

// Data Access Object (DAO) interface for interacting with the 'myuser' table
// in the Room database.
@Dao
interface UserDao {

    // Query to find a user in the 'myuser' table by username.
    @Query("select * from myuser where username = :username")
    fun findByUsername(username: String): MyUser?

    // Inserts a new user into the 'myuser' table.
    @Insert
    fun insertUser(user: MyUser)

    // Updates an existing user in the 'myuser' table.
    @Update
    fun updateUser(user: MyUser)
}