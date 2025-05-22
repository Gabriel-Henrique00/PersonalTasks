package com.example.personaltasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.databinding.TaskBinding
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.views.OnTarefaClickListener
import com.example.personaltasks.views.TarefaViewHolder

class TarefasAdapter (
    private val listaDeTarefas: MutableList<Tarefa>,
    private val listenerDeClique: OnTarefaClickListener
) : RecyclerView.Adapter<TarefaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val binding = TaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, // ViewGroup pai onde o item será inserido
            false // não anexar imediatamente ao pai
        )
        // cria um ViewHolder passando o binding e o listener de clique
        return TarefaViewHolder(binding, listenerDeClique)
    }

    // associa os dados da tarefa (ViewHolder)
    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        holder.vincularDados(listaDeTarefas[position])
    }

    // quantidade total de itens na lista
    override fun getItemCount(): Int = listaDeTarefas.size
}