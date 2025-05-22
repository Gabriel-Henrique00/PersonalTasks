package com.example.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Tarefa::class],
    version = 1,
    exportSchema = false
)
abstract class TarefaDataBase : RoomDatabase() {
    abstract fun tarefaDao(): TarefaDAO

    companion object {
        const val NOME_BANCO_DADOS = "banco-tarefas"
    }
}