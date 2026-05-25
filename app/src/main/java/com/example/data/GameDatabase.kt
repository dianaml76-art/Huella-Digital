package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "resultados_juego")
data class ResultadoJuego(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: Long = System.currentTimeMillis(),
    val puntaje: Int,
    val totalPreguntas: Int = 10
)

@Dao
interface ResultadoDao {
    @Query("SELECT * FROM resultados_juego ORDER BY fecha DESC")
    fun getAllResultados(): Flow<List<ResultadoJuego>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultado(resultado: ResultadoJuego)

    @Query("DELETE FROM resultados_juego")
    suspend fun deleteAllResultados()
}

@Database(entities = [ResultadoJuego::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun resultadoDao(): ResultadoDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "huella_segura_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class GameRepository(private val resultadoDao: ResultadoDao) {
    val allResultados: Flow<List<ResultadoJuego>> = resultadoDao.getAllResultados()

    suspend fun insert(resultado: ResultadoJuego) {
        resultadoDao.insertResultado(resultado)
    }

    suspend fun clearAll() {
        resultadoDao.deleteAllResultados()
    }
}
