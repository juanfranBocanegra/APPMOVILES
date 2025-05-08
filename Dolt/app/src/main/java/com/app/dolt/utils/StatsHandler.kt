package com.app.dolt.utils

import android.os.Handler
import android.os.Looper
import timber.log.Timber

object StatsHandler {
    private val handler = Handler(Looper.getMainLooper())
    private var task: Runnable? = null

    private var c_block: () -> Unit = {}

    fun startRepeatingTask(interval: Long = 2000L, block: () -> Unit) {
        stopRepeatingTask()  // para evitar duplicación
        task = object : Runnable {
            override fun run() {
                c_block = block
                c_block()
                handler.postDelayed(this, interval)
            }
        }
        Timber.i("Starting repeating task with interval: $interval")
        handler.post(task!!)
    }

    fun stopRepeatingTask() {
        task?.let { handler.removeCallbacks(it) }
        task = null
    }

    fun restartRepeatingTask() {
        task?.let {
            handler.removeCallbacks(it)  // Eliminar la tarea anterior
            c_block()
            Timber.i("Interval"+c_block.toString())
            it.run()  // Ejecutar la tarea inmediatamente
            Timber.i("Interval Restarting repeating task")

        }
    }
}