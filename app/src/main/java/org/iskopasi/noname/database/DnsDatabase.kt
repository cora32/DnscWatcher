package org.iskopasi.noname.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.iskopasi.noname.dao.DnsDao
import org.iskopasi.noname.entities.DnscItem

/**
 * Created by cora32 on 23.12.2017.
 */
@Database(entities = [(DnscItem::class)], version = 2)
abstract class DnsDatabase : RoomDatabase() {
    companion object {
        const val DNS_TABLE = "DNS_TABLE"
    }

    abstract fun dnsDao(): DnsDao
}