package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tarefas")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = ID_TAREFA_INVALIDO,
    var titulo: String = "",
    var descricao: String = "",
    var dataVencimento: String? = ""
) : Parcelable {

    companion object {
        const val ID_TAREFA_INVALIDO = -1
        const val EXTRA_TAREFA = "EXTRA_TAREFA"
        const val EXTRA_VIEW_TAREFA = "EXTRA_VIEW_TAREFA"
    }
}