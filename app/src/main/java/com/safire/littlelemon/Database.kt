package com.safire.littlelemon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity
data class MenuItem(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String,
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM MenuItem")
    fun getAll(): LiveData<List<MenuItem>>



    @Query("SELECT (SELECT COUNT(*) FROM MenuItem) == 0")
    fun isEmpty(): Boolean

    @Insert
    fun insertAll(vararg menuItems: MenuItem)
}

@Database(entities = [MenuItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
}


@Serializable
data class MenuNetwork(
    @SerialName("menu")
    val menuItems: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String
) {
    fun toMenuItemRoom() = MenuItem(
        id = id,
        title = title,
        price = price.toDouble(),
        description = description,
        image = image,
        category = category
    )
}
