package com.example.personaltasks.views

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TaskBinding
import com.example.personaltasks.model.Tarefa

class DeletedTarefaViewHolder(
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

            // Para tarefas excluídas, o checkbox não é relevante ou deve ser desabilitado
            statusCb.visibility = View.GONE
            statusCb.isChecked = false

            root.setBackgroundColor(root.context.getColor(R.color.gray_deleted_task))
        }
    }

    private fun configurarMenuDeContexto() {
        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            val context = binding.root.context
            if (context is AppCompatActivity) {
                context.menuInflater.inflate(R.menu.context_menu_deleted_tasks, menu)

                menu?.apply {
                    findItem(R.id.restore_task_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onRestoreTarefaMenuClicado(adapterPosition)
                        true
                    }

                    findItem(R.id.details_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onCliqueTarefa(adapterPosition)
                        true
                    }
                    findItem(R.id.delete_permanently_deleted_task_mi)?.setOnMenuItemClickListener {
                        cliqueListener.onDeletePermanentlyMenuClicado(adapterPosition) // Listener para exclusão permanente
                        true
                    }
                }
            }
        }
    }
}