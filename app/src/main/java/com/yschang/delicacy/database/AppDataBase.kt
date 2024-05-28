package com.yschang.delicacy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yschang.delicacy.dao.UserDao
import com.yschang.delicacy.entity.MyUser

//Defines a Room database with one entity and a DAO
/*This database has a version of 2 and
"does not export the schema to a file (To reduce unnecessary files and ease maintenance)"*/
@Database(entities = [MyUser::class], version = 2, exportSchema = false)

//Inherit from RoomDatabase to make AppDatabase be part of the Room framework
abstract class AppDatabase : RoomDatabase() {
    //To ensure the database has operation interfaces and is associated with entity classes
    abstract fun userDao(): UserDao
}