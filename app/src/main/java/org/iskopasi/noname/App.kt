package org.iskopasi.noname

import android.app.Application
import android.arch.persistence.room.Room
import org.iskopasi.noname.database.DnsDatabase

/**
 * Created by cora32 on 23.12.2017.
 */
class App : Application() {
    companion object {
        lateinit var db: DnsDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, DnsDatabase::class.java, DnsDatabase.DNS_TABLE)
                .fallbackToDestructiveMigration() //Dropping everything on update.
                .build()
    }
}