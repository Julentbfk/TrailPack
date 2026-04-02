package com.julen.trailpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.julen.trailpack.vistas.componentes.marcogeneral.ScaffoldTrailPack


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                ScaffoldTrailPack()
        }
    }
}

