package com.example.personaltasks.controllers

import androidx.lifecycle.LiveData
import com.example.personaltasks.model.FirestoreTarefasRepository
import com.example.personaltasks.model.Tarefa

class MainController(private val firestoreRepository: FirestoreTarefasRepository) {

    fun getNonDeletedTarefas(): LiveData<List<Tarefa>> {
        return firestoreRepository.nonDeletedTarefas
    }

    fun getDeletedTarefas(): LiveData<List<Tarefa>> {
        return firestoreRepository.deletedTarefas
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

    fun markTarefaAsDeleted(tarefaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.markTarefaAsDeleted(tarefaId, onSuccess, onFailure)
    }

    fun restoreTarefa(tarefaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.restoreTarefa(tarefaId, onSuccess, onFailure)
    }

    fun deleteTarefaPermanently(tarefaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreRepository.deleteTarefaPermanently(tarefaId, onSuccess, onFailure)
    }

    fun startListening() {
        firestoreRepository.startListeningForTasks()
    }

    fun stopListening() {
        firestoreRepository.stopListening()
    }
}