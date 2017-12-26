package org.iskopasi.noname.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.iskopasi.noname.database.DnsDatabase.Companion.DNS_TABLE

/**
 * Created by cora32 on 13.12.2017.
 */
@Entity(tableName = DNS_TABLE)
data class DnscItem(@PrimaryKey val id: Long,
                    val name: String,
                    val fullname: String,
                    val version: String,
                    val namecoin: String,
                    val noLogs: String,
                    val location: String,
                    val comment: String,
                    var online: Boolean,
                    var ip: String)