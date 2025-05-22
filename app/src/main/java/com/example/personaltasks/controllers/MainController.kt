package com.example.personaltasks.controllers

import androidx.room.Room
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.model.TarefaDAO
import com.example.personaltasks.model.TarefaDataBase
import com.example.personaltasks.views.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun inserirTarefa(tarefa: Tarefa) {
        executarEmBackground { tarefaDao.inserirTarefa(tarefa) }
    }

    fun obterTodasTarefas() = tarefaDao.obterTodasTarefas()

    fun atualizarTarefa(tarefa: Tarefa) {
        executarEmBackground { tarefaDao.atualizarTarefa(tarefa) }
    }

    fun removerTarefa(tarefa: Tarefa) {
        executarEmBackground { tarefaDao.removerTarefa(tarefa) }
    }

    private fun executarEmBackground(acao: suspend () -> Unit) {
        escopoCorrotinas.launch {
            withContext(Dispatchers.IO) { acao() }
        }
    }
}

