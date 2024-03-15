package com.jhon.educatioapp.controllers
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.jhon.educatioapp.R
import com.jhon.educatioapp.databinding.ActivityMainBinding
import com.jhon.educatioapp.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragment, homeFragment)
            .commit()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Configurar el menú según el tipo de usuario
        configurarMenu(navView.menu, obtenerTipoUsuario())

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.Home_Fragment, R.id.solicitar_clase, R.id.mis_clases, R.id.perfil_usuario, R.id.postulacion_docente, R.id.mis_clases_docente, R.id.perfil_docente
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Detectar clics en los ítems del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            // Realizar la lógica de cambio de opciones según el ítem del menú seleccionado
            when (menuItem.itemId) {
                R.id.ser_docente -> {
                    // Cambiar a las opciones del tipo de usuario 1 (docente)
                    // Por ejemplo, navegar a un fragmento específico
                    navController.navigate(R.id.Home_Fragment)
                    configurarMenu(navView.menu, 1) // Actualizar el menú
                    true
                }
                R.id.mis_clases_docente -> {
                    // Navegar al fragmento de "mis_clases"
                    navController.navigate(R.id.mis_clases_docente)
                    true
                }
                R.id.perfil_docente -> {
                    // Navegar al fragmento de "mis_clases"
                    navController.navigate(R.id.perfil_docente)
                    true
                }
                R.id.postulacion_docente -> {
                    // Navegar al fragmento de "mis_clases"
                    navController.navigate(R.id.postulacion_docente)
                    true
                }
                R.id.Home_Fragment -> {
                    // Navegar al fragmento de "mis_clases"
                    navController.navigate(R.id.Home_Fragment)
                    true
                }
                R.id.ser_usuario -> {
                    // Cambiar a las opciones del tipo de usuario 0 (estudiante)
                    // Por ejemplo, navegar a un fragmento específico
                    navController.navigate(R.id.Home_Fragment)
                    configurarMenu(navView.menu, 0) // Actualizar el menú
                    true
                }
                R.id.solicitar_clase -> {
                    // Navegar al fragmento de "solicitar_clase"
                    navController.navigate(R.id.solicitar_clase)
                    true
                }
                R.id.mis_clases -> {
                    // Navegar al fragmento de "mis_clases"
                    navController.navigate(R.id.mis_clases)
                    true
                }
                R.id.perfil_usuario -> {
                    // Navegar al fragmento de "perfil_usuario"
                    navController.navigate(R.id.perfil_usuario)
                    true
                }
                // Agregar más casos según sea necesario para otros items del menú
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Función para obtener el tipo de usuario
    private fun obtenerTipoUsuario(): Int {
        // Aquí implementa la lógica para obtener el tipo de usuario
        // Por ejemplo, puedes obtenerlo desde una base de datos, SharedPreferences, etc.
        // Por ahora, como ejemplo, se retorna un valor estático
        return 0 // Cambia este valor según tus necesidades
    }

    // Función para configurar el menú según el tipo de usuario
    private fun configurarMenu(menu: Menu, tipoUsuario: Int) {
        menu.findItem(R.id.ser_docente)?.isVisible = (tipoUsuario == 0)
        menu.findItem(R.id.ser_usuario)?.isVisible = (tipoUsuario == 1)
        menu.findItem(R.id.solicitar_clase)?.isVisible = (tipoUsuario == 0)
        menu.findItem(R.id.postulacion_docente)?.isVisible = (tipoUsuario == 1)
        menu.findItem(R.id.mis_clases)?.isVisible = (tipoUsuario == 0)
        menu.findItem(R.id.mis_clases_docente)?.isVisible = (tipoUsuario == 1)
        menu.findItem(R.id.perfil_usuario)?.isVisible = (tipoUsuario == 0)
        menu.findItem(R.id.perfil_docente)?.isVisible = (tipoUsuario == 1)
    }
}
