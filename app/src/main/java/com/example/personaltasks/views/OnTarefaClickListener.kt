package com.example.personaltasks.views

interface OnTarefaClickListener {
    fun onCliqueTarefa(posicao: Int)
    fun onRemoverTarefaMenuClicado(posicao: Int)
    fun onEditarTarefaMenuClicado(posicao: Int)
    fun onToggleStatusMenuClicado(posicao: Int)
    fun onToggleStatusDireto(posicao: Int, novoStatus: Boolean)
    fun onRestoreTarefaMenuClicado(posicao: Int)
    fun onDeletePermanentlyMenuClicado(posicao: Int)
}