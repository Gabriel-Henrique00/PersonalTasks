package com.example.personaltasks.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.adapters.DeletedTarefasAdapter
import com.example.personaltasks.controllers.MainController
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding
import com.example.personaltasks.model.FirestoreTarefasRepository
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_TAREFA
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_VIEW_TAREFA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DeletedTasksActivity : AppCompatActivity(), OnTarefaClickListener {

    private val binding: ActivityDeletedTasksBinding by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    private val listaDeletedTarefas: MutableList<Tarefa> = mutableListOf()
    private val deletedTarefaAdapter: DeletedTarefasAdapter by lazy {
        DeletedTarefasAdapter(listaDeletedTarefas, this)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreRepository: FirestoreTarefasRepository
    private lateinit var controlador: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        val userId = auth.currentUser?.uid
        if (userId == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }
        firestoreRepository = FirestoreTarefasRepository(userId)
        controlador = MainController(firestoreRepository)

        configurarToolbar()
        configurarRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        controlador.startListening()
        carregarTarefasExcluidas()
    }

    override fun onStop() {
        super.onStop()
        controlador.stopListening()
    }

    private fun configurarToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Tarefas Excluídas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Volta para a activity anterior
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configurarRecyclerView() {
        binding.deletedTarefasRv.apply {
            adapter = deletedTarefaAdapter
            layoutManager = LinearLayoutManager(this@DeletedTasksActivity)
        }
        registerForContextMenu(binding.deletedTarefasRv)
    }

    private fun carregarTarefasExcluidas() {
        controlador.getDeletedTarefas().observe(this) { deletedList ->
            listaDeletedTarefas.clear()
            listaDeletedTarefas.addAll(deletedList)
            deletedTarefaAdapter.notifyDataSetChanged()
        }
    }

    override fun onCliqueTarefa(posicao: Int) {
        Intent(this, TarefaActivity::class.java).apply {
            putExtra(EXTRA_TAREFA, listaDeletedTarefas[posicao])
            putExtra(EXTRA_VIEW_TAREFA, true)
            startActivity(this)
        }
    }

    override fun onRestoreTarefaMenuClicado(posicao: Int) {
        val tarefaId = listaDeletedTarefas[posicao].id
        if (tarefaId != null) {
            controlador.restoreTarefa(tarefaId,
                onSuccess = {
                    Toast.makeText(this, "Tarefa reativada com sucesso!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { e ->
                    Toast.makeText(this, "Erro ao reativar tarefa: ${e.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onRemoverTarefaMenuClicado(posicao: Int) {}
    override fun onEditarTarefaMenuClicado(posicao: Int) {}
    override fun onToggleStatusMenuClicado(posicao: Int) {}
    override fun onToggleStatusDireto(posicao: Int, novoStatus: Boolean) {}

    override fun onDeletePermanentlyMenuClicado(posicao: Int) {
    }
}