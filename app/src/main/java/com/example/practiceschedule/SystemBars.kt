package com.example.practiceschedule

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Начиная с Android 15 приложение всегда рисуется «от края до края»:
 * строка состояния и панель навигации накладываются поверх разметки.
 * Чтобы содержимое не пряталось под ними, системные отступы добавляются
 * вручную — один раз из onCreate() каждого экрана.
 *
 * @param root            корневой контейнер экрана; получает отступы
 *                        слева, справа и снизу
 * @param topInsetView    элемент, который получает верхний отступ
 *                        (обычно шапка экрана); по умолчанию — сам root
 * @param lightStatusBar  true, если под строкой состояния светлый фон и
 *                        её значки должны быть тёмными
 */
fun AppCompatActivity.applySystemBarsInsets(
    root: View,
    topInsetView: View = root,
    lightStatusBar: Boolean = false
) {
    WindowCompat.getInsetsController(window, window.decorView)
        .isAppearanceLightStatusBars = lightStatusBar

    // Исходные отступы запоминаем, чтобы при повторном вызове слушателя
    // (например, после поворота экрана) они не складывались друг с другом.
    val baseTop = topInsetView.paddingTop
    val baseLeft = root.paddingLeft
    val baseRight = root.paddingRight
    val baseBottom = root.paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        topInsetView.updatePadding(top = baseTop + bars.top)
        view.updatePadding(
            left = baseLeft + bars.left,
            right = baseRight + bars.right,
            bottom = baseBottom + bars.bottom
        )
        insets
    }
}
