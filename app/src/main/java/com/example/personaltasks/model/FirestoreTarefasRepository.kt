package com.example.personaltasks.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirestoreTarefasRepository(private val userId: String) {

    private val db = FirebaseFirestore.getInstance()
    private val tarefasCollection = db.collection("tarefas")
    private var liveDataTasksRegistration: ListenerRegistration? = null

    private val _tarefas = MutableLiveData<List<List<Tarefa>>>()
    val tarefas: LiveData<List<List<Tarefa>>> get() = _tarefas

    init {
        startListeningForTasks()
    }

    private fun startListeningForTasks() {
        liveDataTasksRegistration = tarefasCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("FirestoreRepo", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val activeTasks = mutableListOf<Tarefa>()
                val completedTasks = mutableListOf<Tarefa>()

                for (doc in snapshots!!) {
                    val tarefa = doc.toObject(Tarefa::class.java)

                    if (tarefa.concluida) {
                        completedTasks.add(tarefa)
                    } else {
                        activeTasks.add(tarefa)
                    }
                }
                _tarefas.value = listOf(activeTasks, completedTasks)
            }
    }

    fun stopListening() {
        liveDataTasksRegistration?.remove()
    }

    fun addTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefa.userId = userId
        tarefasCollection.add(tarefa)
            .addOnSuccessListener { documentReference ->
                Log.d("FirestoreRepo", "Tarefa adicionada com ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepo", "Erro ao adicionar tarefa", e)
                onFailure(e)
            }
    }

    fun updateTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefa.id?.let { id ->
            tarefasCollection.document(id).set(tarefa)
                .addOnSuccessListener {
                    Log.d("FirestoreRepo", "Tarefa atualizada com sucesso.")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.w("FirestoreRepo", "Erro ao atualizar tarefa", e)
                    onFailure(e)
                }
        } ?: run {
            onFailure(Exception("ID da tarefa não pode ser nulo para atualização."))
        }
    }

    fun deleteTarefaPermanently(tarefaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefasCollection.document(tarefaId).delete()
            .addOnSuccessListener {
                Log.d("FirestoreRepo", "Tarefa excluída permanentemente: $tarefaId")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepo", "Erro ao excluir tarefa permanentemente", e)
                onFailure(e)
            }
    }

    fun getTarefaById(tarefaId: String, onResult: (Tarefa?) -> Unit) {
        tarefasCollection.document(tarefaId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val tarefa = document.toObject(Tarefa::class.java)
                    onResult(tarefa)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepo", "Erro ao buscar tarefa por ID", e)
                onResult(null)
            }
    }
}