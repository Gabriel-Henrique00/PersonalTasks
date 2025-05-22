package com.example.personaltasks.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapters.TarefasAdapter
import com.example.personaltasks.controllers.MainController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_TAREFA
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_VIEW_TAREFA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnTarefaClickListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val listaTarefas: MutableList<Tarefa> = mutableListOf()
    private val tarefaAdapter: TarefasAdapter by lazy {
        TarefasAdapter(listaTarefas, this)
    }

    private lateinit var lancadorActivity: ActivityResultLauncher<Intent>
    private val controlador: MainController by lazy { MainController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configurarToolbar()
        configurarRecyclerView()
        configurarLancadorActivity()
        carregarTarefas()
    }

    private fun configurarToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Lista de Tarefas"
    }

    private fun configurarRecyclerView() {
        binding.listaTarefasRv.apply {
            adapter = tarefaAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun configurarLancadorActivity() {
        lancadorActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.obterTarefa()?.let { tarefaRecebida ->
                    atualizarListaTarefas(tarefaRecebida)
                }
            }
        }
    }

    private fun Intent.obterTarefa(): Tarefa? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(EXTRA_TAREFA, Tarefa::class.java)
        } else {
            getParcelableExtra(EXTRA_TAREFA)
        }
    }

    private fun atualizarListaTarefas(tarefa: Tarefa) {
        val posicao = listaTarefas.indexOfFirst { it.id == tarefa.id }

        if (posicao == -1) {
            listaTarefas.add(tarefa)
            tarefaAdapter.notifyItemInserted(listaTarefas.lastIndex)
            controlador.inserirTarefa(tarefa)

            Toast.makeText(this, "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()


        } else {
            listaTarefas[posicao] = tarefa
            tarefaAdapter.notifyItemChanged(posicao)
            controlador.atualizarTarefa(tarefa)
            Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_task_mi -> {
                abrirTelaAdicionarTarefa()
                true
            }
            else -> false
        }
    }

    private fun abrirTelaAdicionarTarefa() {
        lancadorActivity.launch(Intent(this, TarefaActivity::class.java))
    }

    override fun onCliqueTarefa(posicao: Int) {
        Intent(this, TarefaActivity::class.java).apply {
            putExtra(EXTRA_TAREFA, listaTarefas[posicao])
            putExtra(EXTRA_VIEW_TAREFA, true)
            startActivity(this)
        }
    }

    override fun onRemoverTarefaMenuClicado(posicao: Int) {
        val tarefa = listaTarefas[posicao]
        listaTarefas.removeAt(posicao)
        tarefaAdapter.notifyItemRemoved(posicao)
        controlador.removerTarefa(tarefa)
        Toast.makeText(this, "Tarefa removida!", Toast.LENGTH_SHORT).show()
    }

    override fun onEditarTarefaMenuClicado(posicao: Int) {
        Intent(this, TarefaActivity::class.java).apply {
            putExtra(EXTRA_TAREFA, listaTarefas[posicao])
            lancadorActivity.launch(this)
        }
    }

    private fun carregarTarefas() {
        listaTarefas.clear()

        MainScope().launch {
            val tarefas = withContext(Dispatchers.IO) {
                controlador.obterTodasTarefas()
            }

            listaTarefas.addAll(tarefas)
            tarefaAdapter.notifyDataSetChanged()
        }
    }
}