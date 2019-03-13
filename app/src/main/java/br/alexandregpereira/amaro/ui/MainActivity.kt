package br.alexandregpereira.amaro.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.alexandregpereira.amaro.R
import br.alexandregpereira.amaro.ui.product.list.ProductsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    ProductsFragment()
                )
                .commitNow()
        }
    }
}
