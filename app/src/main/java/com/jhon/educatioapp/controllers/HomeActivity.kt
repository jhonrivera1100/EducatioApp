package com.jhon.educatioapp.controllers

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.jhon.educatioapp.R
import com.jhon.educatioapp.adapters.HomeViewPagerAdapter
import com.jhon.educatioapp.models.ArticuloHome

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val btnVer = findViewById<Button>(R.id.btnVer) // Encuentra la referencia del botón

        val articulos = ArrayList<ArticuloHome>()

        // Agrega algunos datos de ejemplo
        articulos.add(ArticuloHome("Nosotros", "Descubre una nueva forma de aprender con  clases personalizadas y flexibilidad total, te ofrecemos la oportunidad de crecer académicamente a tu ritmo y conveniencia. En Educatio, nos esforzamos por proporcionarte una experiencia educativa sin igual.", R.drawable.logo))
        articulos.add(ArticuloHome("Clases Personalizadas", "Nuestras clases personalizadas permiten a los estudiantes aprender a su propio ritmo, recibir retroalimentación específica y utilizar recursos adaptados a sus necesidades, lo que aumenta su confianza y maximiza su potencial académico.", R.drawable.dos))
        articulos.add(ArticuloHome("Modalidad Virtual o Presencial", "Ofrecemos opciones flexibles de clases virtuales y presenciales para adaptarnos al estilo de vida de cada estudiante. Las clases virtuales brindan conveniencia, mientras que las presenciales ofrecen una experiencia más interactiva.", R.drawable.tres))
        articulos.add(ArticuloHome("Contraoferta", "En nuestra plataforma, los estudiantes tienen la libertad de publicar sus requisitos de clase y recibir múltiples ofertas de instructores. Esta modalidad única permite a los estudiantes comparar precios, ubicaciones y condiciones de enseñanza antes de comprometerse.", R.drawable.cuatro))

        val adapter = HomeViewPagerAdapter(this, articulos)
        viewPager.adapter = adapter

        // Configura el OnClickListener para el botón
        btnVer.setOnClickListener {
            // Cambia a la siguiente página en el ViewPager
            val nextPage = viewPager.currentItem + 1
            if (nextPage < adapter.count) {
                viewPager.currentItem = nextPage
            }
        }
    }
}