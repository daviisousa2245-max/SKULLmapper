
package com.skullmapper.app

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {

    private val purple = Color.rgb(167, 139, 250)
    private val background = Color.rgb(10, 10, 14)
    private val card = Color.rgb(27, 27, 36)
    private val white = Color.WHITE
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = background
        window.navigationBarColor = background
        window.decorView.setBackgroundColor(background)

        createInterface()
    }

    private fun createInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 28, 22, 20)
            setBackgroundColor(background)
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val logo = TextView(this).apply {
            text = "☠ SKULL MAPPER"
            textSize = 27f
            setTextColor(purple)
            typeface = Typeface.DEFAULT_BOLD
        }
        content.addView(logo)

        content.addView(text(
            "KEYBOARD & MOUSE MAPPING",
            12f, 0xFF999999.toInt()
        ).apply {
            setPadding(0, 7, 0, 25)
        })

        content.addView(section("PAINEL PRINCIPAL"))

        content.addView(infoCard(
            "⌨",
            "Teclado físico",
            "Bluetooth ou USB OTG"
        ))

        content.addView(infoCard(
            "🖱",
            "Mouse",
            "Movimentos e cliques"
        ))

        content.addView(infoCard(
            "🎮",
            "Mapeamento de controles",
            "Configuração de teclas e posições"
        ))

        content.addView(section("PERMISSÕES"))

        status = text(
            "● Serviço de acessibilidade: verificando...",
            14f, white
        )
        status.setPadding(15, 18, 15, 18)
        status.background = rounded(card, 14)
        content.addView(status)

        val permissionButton = button(
            "CONFIGURAR ACESSIBILIDADE"
        ) {
            startActivity(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            )
        }
        content.addView(permissionButton)

        content.addView(section("SOBRE"))

        content.addView(text(
            "SKULL mapper\n" +
            "Mapeador de teclado e mouse para Android.\n\n" +
            "Versão 1.0",
            13f, 0xFFAAAAAA.toInt()
        ).apply {
            setPadding(5, 12, 5, 20)
        })

        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)
    }

    private fun section(title: String): TextView {
        return text(title, 13f, purple).apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 25, 0, 13)
        }
    }

    private fun infoCard(
        symbol: String,
        title: String,
        description: String
    ): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)
            background = rounded(card, 14)
        }

        box.addView(text(symbol, 25f, purple))

        box.addView(text(
            title, 17f, white
        ).apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 8, 0, 4)
        })

        box.addView(text(
            description, 13f, 0xFFAAAAAA.toInt()
        ))

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = 12
        box.layoutParams = params

        return box
    }

    private fun button(
        label: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            text = label
            textSize = 13f
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT_BOLD
            background = rounded(purple, 12)
            isAllCaps = false
            setOnClickListener { action() }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                58
            )
            params.topMargin = 15
            layoutParams = params
        }
    }

    private fun text(
        value: String,
        size: Float,
        color: Int
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
        }
    }

    private fun rounded(
        color: Int,
        radius: Int
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }
    }

    private fun checkAccessibility(): Boolean {
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return enabled.contains(packageName)
    }

    override fun onResume() {
        super.onResume()

        if (::status.isInitialized) {
            status.text = if (checkAccessibility()) {
                "● Serviço de acessibilidade: habilitado"
            } else {
                "● Serviço de acessibilidade: desabilitado"
            }
        }
    }
}
