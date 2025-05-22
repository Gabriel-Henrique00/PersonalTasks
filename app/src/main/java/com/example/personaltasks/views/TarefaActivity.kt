package com.example.personaltasks.views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.TarefaActivityBinding
import com.example.personaltasks.model.Tarefa.Companion.EXTRA_TAREFA
import com.example.personaltasks.model.Tarefa
import java.time.LocalDate
import java.util.Calendar

class TarefaActivity : AppCompatActivity() {
    private var dataSelecionada: LocalDate? = null
    private val binding: TarefaActivityBinding by lazy {
        TarefaActivityBinding.inflate(layoutInflater)
    }

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
    }

    private fun configurarToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Nova Tarefa"
    }

    private fun configurarDatePicker() {
        val calendario = Calendar.getInstance()

        binding.openDialogBt.setOnClickListener {
            DatePickerDialog(
                this,
                { _, anoSelecionado, mesSelecionado, diaSelecionado ->
                    dataSelecionada = LocalDate.of(
                        anoSelecionado,
                        mesSelecionado + 1,
                        diaSelecionado
                    )
                    binding.openDialogBt.text = dataSelecionada.toString()
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
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
                openDialogBt.text = tarefa.dataVencimento
                dataSelecionada = LocalDate.parse(tarefa.dataVencimento)
            }
        }
    }

    private fun configurarBotaoSalvar() {
        binding.saveBt.setOnClickListener {
            val titulo = binding.titleEt.text.toString().trim()
            val descricao = binding.descriptionEt.text.toString().trim()
            val data = dataSelecionada?.toString()
                ?: intent.getParcelableExtra<Tarefa>(EXTRA_TAREFA)?.dataVencimento

            if (titulo.isNotBlank() && descricao.isNotBlank() && data != null) {
                val tarefa = Tarefa(
                    id = intent.getParcelableExtra<Tarefa>(EXTRA_TAREFA)?.id ?: hashCode(),
                    titulo = titulo,
                    descricao = descricao,
                    dataVencimento = data
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
}
