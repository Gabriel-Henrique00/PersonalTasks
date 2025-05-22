package com.example.personaltasks.controllers

import androidx.room.Room
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.model.TarefaDAO
import com.example.personaltasks.model.TarefaDataBase
import com.example.personaltasks.views.MainActivity
import kotlinx.coroutines.MainScope

class MainController(
    private val mainActivity: MainActivity
) {
    private val escopoCorrotinas = MainScope()
    private val bancoDeDados: TarefaDataBase by lazy {
        Room.databaseBuilder(
            mainActivity,
            TarefaDataBase::class.java,
            "task-database"
        ).build()
    }
    private val tarefaDao: TarefaDAO by lazy { bancoDeDados.tarefaDao() }
}

