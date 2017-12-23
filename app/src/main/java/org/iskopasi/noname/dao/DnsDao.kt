package org.iskopasi.noname.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import org.iskopasi.noname.database.DnsDatabase.Companion.DNS_TABLE
import org.iskopasi.noname.entities.DnscItem

/**
 * Created by cora32 on 23.12.2017.
 */
@Dao
interface DnsDao {
    @Query("SELECT * FROM " + DNS_TABLE)
    fun getData(): List<DnscItem>

    @Insert
    fun saveData(list: List<DnscItem>)
}