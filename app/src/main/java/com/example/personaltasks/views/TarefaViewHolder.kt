package com.example.personaltasks.views

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TaskBinding
import com.example.personaltasks.model.Tarefa

class TarefaViewHolder(
    private val binding: TaskBinding,
    private val cliqueListener: OnTarefaClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        configurarMenuDeContexto()
    }

    fun vincularDados(tarefa: Tarefa) {
        binding.apply {
            titleTv.text = tarefa.titulo
            descriptionTv.text = tarefa.descricao
            duedateTv.text = tarefa.dataVencimento
        }
    }

    private fun configurarMenuDeContexto() {
        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            val context = binding.root.context
            if (context is AppCompatActivity) {
                context.menuInflater.inflate(R.menu.context_menu, menu)

                menu?.apply {
                    findItem(R.id.edit_task_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onEditarTarefaMenuClicado(adapterPosition)
                        true
                    }

                    findItem(R.id.remove_task_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onRemoverTarefaMenuClicado(adapterPosition)
                        true
                    }

                    findItem(R.id.details_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onCliqueTarefa(adapterPosition)
                        true
                    }
                }
            }
        }
    }
}
