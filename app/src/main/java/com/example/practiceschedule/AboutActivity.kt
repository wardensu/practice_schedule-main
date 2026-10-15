package com.example.practiceschedule

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран 4 — сведения о приложении: назначение, версия, версии SDK, автор.
 * Открывается явным Intent с [MainActivity] без дополнительных данных.
 *
 * Разметка: activity_about.xml (RelativeLayout).
 */
class AboutActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AboutActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        Log.d(TAG, "onCreate: экран создан, подключена разметка activity_about")

        // Фон экрана светлый — значки строки состояния делаем тёмными
        applySystemBarsInsets(root = findViewById(R.id.aboutRoot), lightStatusBar = true)

        findViewById<Button>(R.id.btnBackAbout).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «Назад»: вызывается finish(), возврат на MainActivity")
            finish()
        }

        // Системная кнопка «Назад» обрабатывается через OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i(TAG, "Системная кнопка «Назад»: экран закрывается")
                finish()
            }
        })
    }

    // ---- Методы жизненного цикла ----

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: экран стал видимым")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: экран на переднем плане")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: экран потерял фокус")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: экран больше не виден")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: экран уничтожен")
    }
}
