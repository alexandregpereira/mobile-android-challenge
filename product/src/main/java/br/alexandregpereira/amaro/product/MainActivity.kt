package br.alexandregpereira.amaro.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.alexandregpereira.amaro.product.list.ProductsFragment
import br.alexandregpereira.amaro.ui.Navigator
import br.alexandregpereira.amaro.ui.OnBackPressed

class MainActivity : AppCompatActivity(), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductsFragment(), FRAGMENT_TAG)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment is OnBackPressed && fragment.onBackPressed()) return
        super.onBackPressed()
    }

    override fun navigateTo(fragment: Fragment): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, fragment, FRAGMENT_TAG)
    }

    companion object {
        private const val FRAGMENT_TAG = "CONTAINER"
    }
}
