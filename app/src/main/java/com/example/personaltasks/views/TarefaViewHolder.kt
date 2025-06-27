package com.example.personaltasks.views

import android.view.View
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
        binding.statusCb.setOnClickListener {
            cliqueListener.onToggleStatusDireto(adapterPosition, binding.statusCb.isChecked)
        }
    }

    fun vincularDados(tarefa: Tarefa) {
        binding.apply {
            titleTv.text = tarefa.titulo
            descriptionTv.text = tarefa.descricao
            duedateTv.text = tarefa.dataVencimento
            statusCb.isChecked = tarefa.concluida
            statusCb.visibility = View.VISIBLE
            root.setBackgroundColor(root.context.getColor(
                if (tarefa.concluida) R.color.concluida else R.color.white
            ))
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
                    findItem(R.id.delete_permanently_task_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onDeletePermanentlyMenuClicado(adapterPosition)
                        true
                    }
                }
            }
        }
    }
}