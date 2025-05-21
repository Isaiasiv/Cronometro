package com.example.cronometro;

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var segundos = 0
    private var execucao = false
    private val handler = Handler()
    private var estavaEmexecucao = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            segundos = savedInstanceState.getInt("segundos")
            execucao = savedInstanceState.getBoolean("execucao")
        }

        val textView = findViewById<TextView>(R.id.textViewTime) // muda numeros
        val buttonStart = findViewById<Button>(R.id.buttonStart) // Atualizado
        val buttonStop = findViewById<Button>(R.id.buttonStop) // Atualizado
        val buttonReset = findViewById<Button>(R.id.buttonReset) // Atualizado

        // Iniciar o cronômetro
        buttonStart.setOnClickListener {
            execucao = true
            runTimer(textView)
        }

        // Parar o cronômetro
        buttonStop.setOnClickListener { execucao = false }

        // Resetar o cronômetro
        buttonReset.setOnClickListener {
            execucao = false
            segundos = 0
            textView.text = "00:00:00"
        }
    }

    override fun onStart() {
        super.onStart()
        // se o cronometro estava ativado, o faz funcionar novamente
        if (estavaEmexecucao) {
            execucao = true
        }
    }
    override fun onStop() {
        super.onStop()
        // registra se o cronometro estava ativado quando o onStop() foi chamado
        estavaEmexecucao = execucao
        execucao = false
    }
        //app fica em segundo plano mas visivel salva info do app
    override fun onPause() {
        super.onPause();
        // registra se o cronometro estava ativado quando o onPause() foi chamado
        estavaEmexecucao = execucao
        execucao = false
    }
    // volta onde parou no app
    override fun onResume() {
        super.onResume()
        if (estavaEmexecucao) {
            execucao = true
            val textView = findViewById<TextView>(R.id.textViewTime)
            runTimer(textView) // Continua de onde parou
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
         super.onSaveInstanceState(savedInstanceState)
         savedInstanceState.putInt("segundos", segundos)
         savedInstanceState.putBoolean("execucao", execucao)
        savedInstanceState.putBoolean("estavaEmexecucao", estavaEmexecucao)
     }

    private fun runTimer(textView: TextView) {
        handler.removeCallbacksAndMessages(null)
        handler.post(object : Runnable {
            override fun run() {
                val horas = segundos / 3600
                val minutos = (segundos % 3600) / 60
                val seg = segundos % 60

                val tempo = String.format("%02d:%02d:%02d", horas, minutos, seg)
                textView.text = tempo

                if (execucao) {
                    segundos++
                }

                handler.postDelayed(this, 1000) // Executa a cada segundo
            }
        })
    }
}
