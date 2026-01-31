package com.boox.masterkey

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.LocaleListCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        @Suppress("SetTextI18n")
        fun addButton(textResId: Int, intent: Intent) {
            val btn = Button(this@MainActivity).apply {
                text = getString(textResId)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16)
                }

                setOnClickListener {
                    try {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (_: Exception) {
                        Toast.makeText(this@MainActivity, getString(R.string.not_available), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            layout.addView(btn)
        }

        // Etiqueta i desplegable d'idioma
        val languageLabel = TextView(this).apply {
            text = getString(R.string.language_label)
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 8)
            }
        }
        layout.addView(languageLabel)

        var isInitializing = true  // ← AFEGIT: Variable de control

        val languageSpinner = Spinner(this).apply {
            val languages = arrayOf("Català", "English")
            val spinnerAdapter = ArrayAdapter(
                this@MainActivity,
                R.layout.spinner_item,  // ← Canviat: usa el teu layout
                languages
            )
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)  // ← Canviat: usa el teu dropdown
            adapter = spinnerAdapter

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 10,32)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (isInitializing) return  // ← AFEGIT: Evita canvi durant inicialització

                    val locale = resources.configuration.locales[0]
                    val shouldBeCatalan = position == 0
                    val isCatalan = locale.language == "ca"

                    if (shouldBeCatalan != isCatalan) {
                        val newLocale = if (shouldBeCatalan) Locale.forLanguageTag("ca") else Locale.ENGLISH
                        val appLocale = LocaleListCompat.create(newLocale)
                        androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        layout.addView(languageSpinner)

        // Seleccionar idioma inicial
        val initialLocale = resources.configuration.locales[0]
        languageSpinner.setSelection(if (initialLocale.language == "ca") 0 else 1, false)
        isInitializing = false  // ← AFEGIT: Marca com a inicialitzat

        // Configuracions bàsiques
        addButton(R.string.settings, Intent(Settings.ACTION_SETTINGS))
        addButton(R.string.developer_options, Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
        addButton(R.string.app_settings, Intent(Settings.ACTION_APPLICATION_SETTINGS))

        // Navigation Ball (Naviball)
        addButton(R.string.navigation_ball,
            packageManager.getLaunchIntentForPackage("com.onyx.floatingbutton")
                ?: Intent(Settings.ACTION_SETTINGS))

        // Configuracions de pantalla i sistema
        addButton(R.string.display_settings, Intent(Settings.ACTION_DISPLAY_SETTINGS))
        addButton(R.string.battery_settings, Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS))
        addButton(R.string.wifi_settings, Intent(Settings.ACTION_WIFI_SETTINGS))
        addButton(R.string.sound_settings, Intent(Settings.ACTION_SOUND_SETTINGS))
        addButton(R.string.date_time, Intent(Settings.ACTION_DATE_SETTINGS))
        addButton(R.string.language_input, Intent(Settings.ACTION_LOCALE_SETTINGS))
        addButton(R.string.storage_settings, Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS))
        addButton(R.string.security_settings, Intent(Settings.ACTION_SECURITY_SETTINGS))
        addButton(R.string.accessibility, Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

        // Configuracions avançades
        addButton(R.string.about_device, Intent(Settings.ACTION_DEVICE_INFO_SETTINGS))
        addButton(R.string.system_updates, Intent("android.settings.SYSTEM_UPDATE_SETTINGS"))

        @Suppress("SetTextI18n")
        val exitBtn = Button(this).apply {
            text = getString(R.string.exit)

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 32, 0, 16)
            }

            setOnClickListener {
                finishAffinity()
                android.os.Process.killProcess(android.os.Process.myPid())  // ← O aquesta
            }
        }
        layout.addView(exitBtn)

        // Signatura al final
        val signature = TextView(this).apply {
            text = getString(R.string.created_by)  // ← Canvia aquesta línia
            textSize = 16f
            gravity = android.view.Gravity.CENTER
            setTextColor(android.graphics.Color.GRAY)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
        }
        layout.addView(signature)

        val scrollView = ScrollView(this).apply {
            addView(layout)
        }
        setContentView(scrollView)
    }
}
