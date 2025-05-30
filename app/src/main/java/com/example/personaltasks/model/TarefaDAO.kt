package com.example.personaltasks.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TarefaDAO {

    @Insert
    fun inserirTarefa(tarefa: Tarefa): Long

    @Query("SELECT * FROM tarefas WHERE id = :id")
    fun obterTarefaPorId(id: Int): Tarefa

    @Query("SELECT * FROM tarefas")
    fun obterTodasTarefas(): MutableList<Tarefa>

    @Update
    fun atualizarTarefa(tarefa: Tarefa): Int

    @Delete
    fun removerTarefa(tarefa: Tarefa): Int

    @Query("UPDATE tarefas SET concluida = :concluida WHERE id = :id")
    fun atualizarStatus(id: Int, concluida: Boolean)
}