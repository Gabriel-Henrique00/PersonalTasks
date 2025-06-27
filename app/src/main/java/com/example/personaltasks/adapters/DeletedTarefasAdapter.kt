package com.example.personaltasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.databinding.TaskBinding
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.views.OnTarefaClickListener
import com.example.personaltasks.views.DeletedTarefaViewHolder

class DeletedTarefasAdapter (
    private val listaDeTarefas: MutableList<Tarefa>,
    private val listenerDeClique: OnTarefaClickListener
) : RecyclerView.Adapter<DeletedTarefaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletedTarefaViewHolder {
        val binding = TaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DeletedTarefaViewHolder(binding, listenerDeClique)
    }

    override fun onBindViewHolder(holder: DeletedTarefaViewHolder, position: Int) {
        holder.vincularDados(listaDeTarefas[position])
    }

    override fun getItemCount(): Int = listaDeTarefas.size
}