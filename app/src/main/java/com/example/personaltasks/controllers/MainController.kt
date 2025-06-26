package com.example.personaltasks.controllers

import androidx.lifecycle.LiveData
import com.example.personaltasks.model.FirestoreTarefasRepository
import com.example.personaltasks.model.Tarefa

class MainController(private val firestoreRepository: FirestoreTarefasRepository) {

    fun getTarefas(): LiveData<List<List<Tarefa>>> {
        return firestoreRepository.tarefas
    }

    fun getTarefaById(id: String, onResult: (Tarefa?) -> Unit) {
        firestoreRepository.getTarefaById(id, onResult)
    }

    fun addTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.addTarefa(tarefa, onSuccess, onFailure)
    }

    fun updateTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.updateTarefa(tarefa, onSuccess, onFailure)
    }

    fun deleteTarefa(tarefaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.deleteTarefaPermanently(tarefaId, onSuccess, onFailure)
    }

    fun stopListening() {
        firestoreRepository.stopListening()
    }
}