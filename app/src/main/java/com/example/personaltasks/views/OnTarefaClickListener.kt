package com.example.personaltasks.views

interface OnTarefaClickListener {
    fun onCliqueTarefa(posicao: Int)
    fun onRemoverTarefaMenuClicado(posicao: Int)
    fun onEditarTarefaMenuClicado(posicao: Int)
}