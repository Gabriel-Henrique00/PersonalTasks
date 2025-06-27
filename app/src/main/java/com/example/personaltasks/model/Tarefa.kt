package com.example.personaltasks.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tarefa(
    @DocumentId
    var id: String? = null,
    var userId: String = "",
    var titulo: String = "",
    var descricao: String = "",
    var dataVencimento: String? = "",
    var concluida: Boolean = false,
    var deleted: Boolean = false
) : Parcelable {

    companion object {
        const val EXTRA_TAREFA = "EXTRA_TAREFA"
        const val EXTRA_VIEW_TAREFA = "EXTRA_VIEW_TAREFA"
    }
}