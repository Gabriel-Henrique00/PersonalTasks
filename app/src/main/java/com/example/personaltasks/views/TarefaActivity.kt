package com.example.personaltasks.views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TarefaActivityBinding
import com.example.personaltasks.model.Tarefa
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_TAREFA
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_VIEW_TAREFA
import java.time.LocalDate
import java.util.Calendar
import com.example.personaltasks.model.Tarefa.Prioridade

class TarefaActivity : AppCompatActivity() {
    private var dataSelecionada: LocalDate? = null
    private val binding: TarefaActivityBinding by lazy {
        TarefaActivityBinding.inflate(layoutInflater)
    }
    private lateinit var spinnerPrioridade: Spinner;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarToolbar()
        configurarDatePicker()
        tratarDadosRecebidos()
        configurarBotaoSalvar()
        binding.cancelBt.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        setContentView(R.layout.)
    }

    private fun configurarToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Nova Tarefa"
    }

    private fun configurarDatePicker() {
        val calendario = Calendar.getInstance()

        binding.openDialogBt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, anoSelecionado, mesSelecionado, diaSelecionado ->
                    val dataEscolhida = LocalDate.of(
                        anoSelecionado,
                        mesSelecionado + 1,
                        diaSelecionado
                    )

                    dataSelecionada = dataEscolhida
                    binding.openDialogBt.text = dataSelecionada.toString()
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = calendario.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun tratarDadosRecebidos() {
        val tarefaRecebida = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TAREFA, Tarefa::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TAREFA)
        }

        tarefaRecebida?.let { tarefa ->
            supportActionBar?.subtitle = "Editar Tarefa"
            with(binding) {
                titleEt.setText(tarefa.titulo)
                descriptionEt.setText(tarefa.descricao)
                openDialogBt.visibility = View.VISIBLE
                dateTv.visibility = View.GONE
                tarefa.dataVencimento?.let {
                    dataSelecionada = LocalDate.parse(it)
                    openDialogBt.text = dataSelecionada.toString()
                }

                if (intent.getBooleanExtra(EXTRA_VIEW_TAREFA, false)) {
                    supportActionBar?.subtitle = "Visualizar Tarefa"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    openDialogBt.visibility = View.GONE
                    dateTv.visibility = View.VISIBLE
                    dateTv.text = tarefa.dataVencimento
                    saveBt.visibility = View.GONE
                }
            }
        }
    }

    private fun configurarBotaoSalvar() {
        binding.saveBt.setOnClickListener {
            val titulo = binding.titleEt.text.toString().trim()
            val descricao = binding.descriptionEt.text.toString().trim()

            val data = dataSelecionada?.toString() ?: run {
                val existingTarefa = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_TAREFA, Tarefa::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_TAREFA)
                }
                existingTarefa?.dataVencimento
            }

            if (titulo.isNotBlank() && descricao.isNotBlank() && data != null) {
                val tarefaExistente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_TAREFA, Tarefa::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_TAREFA)
                }

                val tarefa = Tarefa(
                    id = tarefaExistente?.id,
                    titulo = titulo,
                    descricao = descricao,
                    dataVencimento = data,
                    concluida = tarefaExistente?.concluida ?: false
                )

                Intent().apply {
                    putExtra(EXTRA_TAREFA, tarefa)
                    setResult(RESULT_OK, this)
                }
                finish()
            } else {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun definirPrio
}